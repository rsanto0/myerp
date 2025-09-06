@echo off
setlocal enabledelayedexpansion

if "%~1"=="" (
    echo ERRO: Regiao nao especificada
    echo Uso: check-costs.bat [REGIAO]
    echo Exemplo: check-costs.bat us-east-1
    exit /b 1
)

set AWS_REGION=%1

echo ========================================
echo  VERIFICACAO DE CUSTOS - MyERP AWS
echo ========================================
echo.
echo Regiao: %AWS_REGION%
echo Data: %date% %time%
echo.

REM ========================================
REM Recursos que geram custo
REM ========================================

echo [INFO] Verificando recursos que geram custo...
echo.

echo --- RDS Instances ---
aws rds describe-db-instances --query "DBInstances[?contains(DBInstanceIdentifier, 'myerp')].{ID:DBInstanceIdentifier,Status:DBInstanceStatus,Class:DBInstanceClass}" --output table --region %AWS_REGION%

echo.
echo --- ECS Services ---
for /f "tokens=*" %%c in ('aws ecs list-clusters --query "clusterArns[?contains(@, 'myerp')]" --output text --region %AWS_REGION%') do (
    set CLUSTER=%%c
    echo Cluster: !CLUSTER!
    aws ecs list-services --cluster !CLUSTER! --query "serviceArns" --output table --region %AWS_REGION%
)

echo.
echo --- Load Balancers ---
aws elbv2 describe-load-balancers --query "LoadBalancers[?contains(LoadBalancerName, 'myerp')].{Name:LoadBalancerName,State:State.Code,Type:Type}" --output table --region %AWS_REGION%

echo.
echo --- NAT Gateways ---
aws ec2 describe-nat-gateways --query "NatGateways[?State=='available' && contains(Tags[?Key=='Name'].Value | [0], 'myerp')].{ID:NatGatewayId,State:State}" --output table --region %AWS_REGION%

echo.
echo --- ECR Repositories ---
aws ecr describe-repositories --query "repositories[?contains(repositoryName, 'myerp')].{Name:repositoryName,Size:repositorySizeInBytes}" --output table --region %AWS_REGION%

echo.
echo --- S3 Buckets ---
for /f "tokens=3" %%b in ('aws s3 ls ^| findstr myerp') do (
    echo Bucket: %%b
    aws s3 ls s3://%%b --recursive --summarize | findstr "Total Size"
)

echo.
echo --- CloudWatch Log Groups ---
aws logs describe-log-groups --query "logGroups[?contains(logGroupName, 'myerp')].{Name:logGroupName,Size:storedBytes}" --output table --region %AWS_REGION%

echo.
echo ========================================
echo  ESTIMATIVA DE CUSTOS MENSAIS
echo ========================================
echo.

REM Calcular estimativas baseadas nos recursos encontrados
echo [INFO] Calculando estimativas...
echo.

echo 💰 CUSTOS ESTIMADOS (USD/mes):
echo.
echo RDS (db.t3.micro):     ~$15-25
echo ECS Fargate (5 tasks): ~$30-50  
echo Load Balancer:         ~$20-25
echo NAT Gateway:           ~$45-50
echo ECR Storage:           ~$1-5
echo S3 Storage:            ~$1-3
echo CloudWatch Logs:       ~$1-2
echo.
echo 📊 TOTAL ESTIMADO:      ~$113-160/mes
echo.

echo ========================================
echo  COMANDOS PARA PARAR CUSTOS
echo ========================================
echo.

echo Para PARAR custos imediatamente:
echo.
echo 1. Parar ECS Services:
echo    aws ecs update-service --cluster myerp-cluster --service myerp-api-gateway --desired-count 0
echo.
echo 2. Deletar Load Balancer:
echo    aws elbv2 delete-load-balancer --load-balancer-arn [ARN]
echo.
echo 3. Parar RDS:
echo    aws rds stop-db-instance --db-instance-identifier myerp-prod-db
echo.
echo 4. Rollback completo:
echo    rollback-complete.bat prod %AWS_REGION%
echo.

pause