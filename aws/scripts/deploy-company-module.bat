@echo off
setlocal enabledelayedexpansion

REM Deploy Company Module to AWS
set ENVIRONMENT=%1
set AWS_REGION=%2
set DOMAIN_NAME=%3

if "%ENVIRONMENT%"=="" set ENVIRONMENT=dev
if "%AWS_REGION%"=="" set AWS_REGION=us-east-1
if "%DOMAIN_NAME%"=="" set DOMAIN_NAME=myerp.local

echo.
echo ========================================
echo  DEPLOY COMPANY MODULE - MyERP
echo ========================================
echo Environment: %ENVIRONMENT%
echo Region: %AWS_REGION%
echo Domain: %DOMAIN_NAME%
echo.

REM Verificar AWS CLI
aws sts get-caller-identity >nul 2>&1
if errorlevel 1 (
    echo ❌ AWS CLI não configurado
    exit /b 1
)

REM Obter Account ID
for /f "tokens=*" %%i in ('aws sts get-caller-identity --query Account --output text') do set AWS_ACCOUNT_ID=%%i

REM Criar ECR repository
echo 📦 Criando ECR repository...
aws ecr create-repository --repository-name myerp-company --region %AWS_REGION% >nul 2>&1

REM Build Docker image
echo 🐳 Building Docker image...
cd ..\..\modules\company-module
docker build -t myerp-company:latest .
if errorlevel 1 exit /b 1

REM Push to ECR
echo 📤 Push para ECR...
for /f "tokens=*" %%i in ('aws ecr get-login-password --region %AWS_REGION%') do set ECR_PASSWORD=%%i
echo %ECR_PASSWORD% | docker login --username AWS --password-stdin %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com
docker tag myerp-company:latest %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/myerp-company:latest
docker push %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com/myerp-company:latest

REM Deploy CloudFormation
echo 🚀 Deploy CloudFormation...
cd ..\..\aws
aws cloudformation deploy ^
    --template-file cloudformation\company-module-resources.yml ^
    --stack-name %ENVIRONMENT%-myerp-company ^
    --parameter-overrides Environment=%ENVIRONMENT% ^
    --capabilities CAPABILITY_IAM ^
    --region %AWS_REGION%

echo ✅ Deploy concluído!

REM Criar rollback script
echo @echo off > rollback-company-module.bat
echo aws cloudformation delete-stack --stack-name %ENVIRONMENT%-myerp-company --region %AWS_REGION% >> rollback-company-module.bat
echo aws ecr delete-repository --repository-name myerp-company --region %AWS_REGION% --force >> rollback-company-module.bat

endlocal