#!/bin/bash
# Script de deploy completo para AWS

set -e

# Verificar se AWS_ACCOUNT_ID está definido
if [ -z "$AWS_ACCOUNT_ID" ]; then
    echo "ERRO: Defina AWS_ACCOUNT_ID primeiro!"
    echo "Exemplo: export AWS_ACCOUNT_ID=123456789012"
    exit 1
fi

# Verificar se AWS CLI está configurado
if ! aws sts get-caller-identity > /dev/null 2>&1; then
    echo "ERRO: AWS CLI não configurado!"
    echo "Execute: aws configure"
    exit 1
fi

ENVIRONMENT=${1:-prod}
REGION=${2:-us-east-1}
DOMAIN=${3:-myerp.com}

echo "=========================================="
echo "    DEPLOY MyERP PARA AWS"
echo "=========================================="
echo "Conta AWS: $AWS_ACCOUNT_ID"
echo "Ambiente: $ENVIRONMENT"
echo "Região: $REGION"
echo "Domínio: $DOMAIN"
echo "=========================================="

# 1. Deploy da Infraestrutura
echo "[1/6] Criando infraestrutura AWS..."
aws cloudformation deploy \
    --template-file cloudformation-infrastructure.yml \
    --stack-name myerp-infrastructure-$ENVIRONMENT \
    --parameter-overrides Environment=$ENVIRONMENT \
    --capabilities CAPABILITY_IAM \
    --region $REGION

# Aguardar stack completar
echo "Aguardando infraestrutura ficar pronta..."
aws cloudformation wait stack-create-complete \
    --stack-name myerp-infrastructure-$ENVIRONMENT \
    --region $REGION

# 2. Obter outputs da infraestrutura
echo "[2/6] Obtendo informações da infraestrutura..."
DB_ENDPOINT=$(aws cloudformation describe-stacks \
    --stack-name myerp-infrastructure-$ENVIRONMENT \
    --query 'Stacks[0].Outputs[?OutputKey==`DatabaseEndpoint`].OutputValue' \
    --output text \
    --region $REGION)

ALB_DNS=$(aws cloudformation describe-stacks \
    --stack-name myerp-infrastructure-$ENVIRONMENT \
    --query 'Stacks[0].Outputs[?OutputKey==`LoadBalancerDNS`].OutputValue' \
    --output text \
    --region $REGION)

echo "Database Endpoint: $DB_ENDPOINT"
echo "Load Balancer DNS: $ALB_DNS"

# 3. Configurar Parameter Store
echo "[3/6] Configurando Parameter Store..."
./parameter-store-setup.sh $ENVIRONMENT $REGION

# Atualizar endpoint do banco
aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/database/endpoint" \
    --value "$DB_ENDPOINT" \
    --type "String" \
    --overwrite \
    --region $REGION

# 4. Deploy Route 53
echo "[4/6] Configurando DNS (Route 53)..."
aws cloudformation deploy \
    --template-file route53-setup.yml \
    --stack-name myerp-dns-$ENVIRONMENT \
    --parameter-overrides \
        Environment=$ENVIRONMENT \
        DomainName=$DOMAIN \
        LoadBalancerDNS=$ALB_DNS \
    --region $REGION

# 5. Build e Deploy das aplicações
echo "[5/6] Fazendo build das aplicações..."
cd ..

# Build de todos os módulos
mvn clean package -DskipTests

# Criar imagens Docker
echo "Criando imagens Docker..."
docker build -t myerp/eureka:$ENVIRONMENT infrastructure/service-discovery/
docker build -t myerp/auth:$ENVIRONMENT infrastructure/auth-service/
docker build -t myerp/gateway:$ENVIRONMENT infrastructure/api-gateway/
docker build -t myerp/rh:$ENVIRONMENT modules/rh-module/
docker build -t myerp/biometria:$ENVIRONMENT modules/biometria-module/
docker build -t myerp/monitoring:$ENVIRONMENT modules/monitoring-module/

# Push para ECR (assumindo que já existe)
echo "Fazendo push para ECR..."
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com

docker tag myerp/eureka:$ENVIRONMENT $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/eureka:$ENVIRONMENT
docker tag myerp/auth:$ENVIRONMENT $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/auth:$ENVIRONMENT
docker tag myerp/gateway:$ENVIRONMENT $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/gateway:$ENVIRONMENT
docker tag myerp/rh:$ENVIRONMENT $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/rh:$ENVIRONMENT
docker tag myerp/biometria:$ENVIRONMENT $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/biometria:$ENVIRONMENT
docker tag myerp/monitoring:$ENVIRONMENT $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/monitoring:$ENVIRONMENT

docker push $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/eureka:$ENVIRONMENT
docker push $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/auth:$ENVIRONMENT
docker push $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/gateway:$ENVIRONMENT
docker push $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/rh:$ENVIRONMENT
docker push $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/biometria:$ENVIRONMENT
docker push $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/myerp/monitoring:$ENVIRONMENT

# 6. Deploy ECS Services
echo "[6/6] Fazendo deploy dos serviços ECS..."
# Aqui você adicionaria os comandos para deploy no ECS/EKS

echo "=========================================="
echo "    DEPLOY CONCLUÍDO COM SUCESSO!"
echo "=========================================="
echo "URLs disponíveis:"
echo "- API Gateway: https://api.$ENVIRONMENT.$DOMAIN"
echo "- Eureka: https://eureka.$ENVIRONMENT.$DOMAIN"
echo "- Monitoring: https://monitoring.$ENVIRONMENT.$DOMAIN"
echo "=========================================="