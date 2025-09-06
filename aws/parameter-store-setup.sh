#!/bin/bash
# Script para configurar AWS Parameter Store

ENVIRONMENT=${1:-prod}
REGION=${2:-us-east-1}

echo "Configurando Parameter Store para ambiente: $ENVIRONMENT"
echo "Região: $REGION"

# Database Parameters
aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/database/endpoint" \
    --value "myerp-db-$ENVIRONMENT.cluster-xyz.us-east-1.rds.amazonaws.com" \
    --type "String" \
    --region $REGION

aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/database/port" \
    --value "5432" \
    --type "String" \
    --region $REGION

aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/database/name" \
    --value "myerp_db" \
    --type "String" \
    --region $REGION

# JWT Secret (Secrets Manager)
aws secretsmanager create-secret \
    --name "/myerp/$ENVIRONMENT/jwt/secret" \
    --description "JWT Secret for MyERP $ENVIRONMENT" \
    --secret-string "$(openssl rand -base64 32)" \
    --region $REGION

# Database Credentials (Secrets Manager)
aws secretsmanager create-secret \
    --name "/myerp/$ENVIRONMENT/database/credentials" \
    --description "Database credentials for MyERP $ENVIRONMENT" \
    --secret-string '{"username":"myerp_user","password":"'$(openssl rand -base64 16)'"}' \
    --region $REGION

# Service URLs
aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/services/eureka/url" \
    --value "http://eureka.$ENVIRONMENT.myerp.internal:8761/eureka/" \
    --type "String" \
    --region $REGION

aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/services/auth/url" \
    --value "http://auth.$ENVIRONMENT.myerp.internal:8081" \
    --type "String" \
    --region $REGION

# Application Configuration
aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/logging/level" \
    --value "INFO" \
    --type "String" \
    --region $REGION

aws ssm put-parameter \
    --name "/myerp/$ENVIRONMENT/monitoring/enabled" \
    --value "true" \
    --type "String" \
    --region $REGION

echo "Parameter Store configurado com sucesso!"
echo "Verifique os parâmetros criados:"
echo "aws ssm get-parameters-by-path --path /myerp/$ENVIRONMENT --recursive --region $REGION"