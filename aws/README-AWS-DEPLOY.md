# MyERP - Deploy AWS Completo

## 🏗️ Arquitetura AWS

### **Analogia: Cidade Empresarial**
Imagine que estamos construindo uma **cidade empresarial completa**:

- **🏢 VPC** = Terreno da empresa (isolado e seguro)
- **🛣️ Subnets** = Bairros (público para visitantes, privado para funcionários)
- **🚪 ALB** = Portaria principal (recebe todas as visitas)
- **🏪 ECS** = Prédios onde funcionam os departamentos
- **🗄️ RDS** = Cofre central (banco de dados)
- **📋 Parameter Store** = Quadro de avisos (configurações)
- **🔐 Secrets Manager** = Cofre de senhas
- **📍 Route 53** = Placa de endereços (DNS)

## 📋 Pré-requisitos

### 1. **AWS CLI Configurado**
```bash
aws configure
# AWS Access Key ID: [sua-key]
# AWS Secret Access Key: [sua-secret]
# Default region: us-east-1
# Default output format: json
```

### 2. **Variáveis de Ambiente**
```bash
export AWS_ACCOUNT_ID=123456789012
export ENVIRONMENT=prod
export DOMAIN_NAME=myerp.com
```

### 3. **Permissões IAM Necessárias**
- CloudFormation (FullAccess)
- EC2 (FullAccess)
- RDS (FullAccess)
- ECS (FullAccess)
- Route53 (FullAccess)
- Systems Manager (FullAccess)
- Secrets Manager (FullAccess)

## 🚀 Deploy Passo a Passo

### **Opção 1: Deploy Automático (Recomendado)**
```bash
cd aws/
chmod +x deploy-aws.sh
./deploy-aws.sh prod us-east-1 myerp.com
```

### **Opção 2: Deploy Manual**

#### **1. Infraestrutura Base**
```bash
aws cloudformation deploy \
    --template-file cloudformation-infrastructure.yml \
    --stack-name myerp-infrastructure-prod \
    --parameter-overrides Environment=prod \
    --capabilities CAPABILITY_IAM \
    --region us-east-1
```

#### **2. Parameter Store**
```bash
chmod +x parameter-store-setup.sh
./parameter-store-setup.sh prod us-east-1
```

#### **3. DNS (Route 53)**
```bash
aws cloudformation deploy \
    --template-file route53-setup.yml \
    --stack-name myerp-dns-prod \
    --parameter-overrides \
        Environment=prod \
        DomainName=myerp.com \
        LoadBalancerDNS=myerp-alb-prod-123456789.us-east-1.elb.amazonaws.com
```

#### **4. ECS Services**
```bash
aws cloudformation deploy \
    --template-file ecs-task-definitions.yml \
    --stack-name myerp-ecs-prod \
    --parameter-overrides Environment=prod \
    --capabilities CAPABILITY_IAM
```

## 🔧 Configurações por Ambiente

### **Desenvolvimento (dev)**
```bash
./deploy-aws.sh dev us-east-1 dev.myerp.com
```
- **Instâncias**: t3.micro
- **RDS**: db.t3.micro (single-AZ)
- **Logs**: 3 dias retenção

### **Staging (staging)**
```bash
./deploy-aws.sh staging us-east-1 staging.myerp.com
```
- **Instâncias**: t3.small
- **RDS**: db.t3.small (single-AZ)
- **Logs**: 7 dias retenção

### **Produção (prod)**
```bash
./deploy-aws.sh prod us-east-1 myerp.com
```
- **Instâncias**: t3.medium
- **RDS**: db.t3.medium (multi-AZ)
- **Logs**: 30 dias retenção

## 🌐 URLs Após Deploy

### **Produção**
- **API Gateway**: https://api.prod.myerp.com
- **Eureka Dashboard**: https://eureka.prod.myerp.com
- **Auth Service**: https://auth.prod.myerp.com
- **RH Module**: https://rh.prod.myerp.com
- **Biometria**: https://biometria.prod.myerp.com
- **Monitoring**: https://monitoring.prod.myerp.com

### **Desenvolvimento**
- **API Gateway**: https://api.dev.myerp.com
- **Eureka Dashboard**: https://eureka.dev.myerp.com
- etc...

## 🔐 Segurança

### **Parameter Store**
```bash
# Listar parâmetros
aws ssm get-parameters-by-path \
    --path /myerp/prod \
    --recursive

# Ver parâmetro específico
aws ssm get-parameter \
    --name /myerp/prod/database/endpoint
```

### **Secrets Manager**
```bash
# Ver secrets
aws secretsmanager list-secrets

# Obter secret
aws secretsmanager get-secret-value \
    --secret-id /myerp/prod/jwt/secret
```

## 📊 Monitoramento

### **CloudWatch Logs**
```bash
# Ver logs do Auth Service
aws logs tail /aws/ecs/myerp-auth-prod --follow

# Ver logs do API Gateway
aws logs tail /aws/ecs/myerp-gateway-prod --follow
```

### **Health Checks**
- **ALB Health Checks**: Automático
- **Route 53 Health Checks**: Configurado
- **ECS Health Checks**: Container level

## 💰 Custos Estimados (us-east-1)

### **Desenvolvimento**
- **EC2**: ~$20/mês
- **RDS**: ~$15/mês
- **ALB**: ~$20/mês
- **Route 53**: ~$1/mês
- **Total**: ~$56/mês

### **Produção**
- **EC2**: ~$80/mês
- **RDS**: ~$60/mês (multi-AZ)
- **ALB**: ~$20/mês
- **Route 53**: ~$1/mês
- **Total**: ~$161/mês

## 🔄 Rollback

### **Rollback Completo**
```bash
# Deletar stacks na ordem inversa
aws cloudformation delete-stack --stack-name myerp-ecs-prod
aws cloudformation delete-stack --stack-name myerp-dns-prod
aws cloudformation delete-stack --stack-name myerp-infrastructure-prod
```

### **Rollback de Aplicação**
```bash
# Atualizar task definition com versão anterior
aws ecs update-service \
    --cluster myerp-cluster-prod \
    --service myerp-auth-service \
    --task-definition myerp-auth-prod:1
```

## 🚨 Troubleshooting

### **Problemas Comuns**

#### **1. Stack Creation Failed**
```bash
# Ver eventos do CloudFormation
aws cloudformation describe-stack-events \
    --stack-name myerp-infrastructure-prod
```

#### **2. ECS Task Não Inicia**
```bash
# Ver logs do container
aws logs tail /aws/ecs/myerp-auth-prod --follow

# Verificar task definition
aws ecs describe-tasks \
    --cluster myerp-cluster-prod \
    --tasks [task-arn]
```

#### **3. DNS Não Resolve**
```bash
# Verificar Route 53
dig api.prod.myerp.com

# Verificar health check
aws route53 get-health-check --health-check-id [id]
```

## 📞 Suporte

Para problemas específicos:
1. Verificar CloudWatch Logs
2. Verificar CloudFormation Events
3. Verificar ECS Service Events
4. Verificar Route 53 Health Checks

**Lembre-se**: AWS é como uma **cidade grande** - tem muitas partes, mas cada uma tem sua função específica!