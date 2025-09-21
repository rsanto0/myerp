@echo off
echo ========================================
echo    DEPLOY SIMPLES AWS - MyERP
echo ========================================
echo.

REM Verificar se AWS_ACCOUNT_ID está definido
if "%AWS_ACCOUNT_ID%"=="" (
    echo ERRO: Defina AWS_ACCOUNT_ID primeiro!
    echo.
    echo Execute: setup-aws.bat
    echo Ou: set AWS_ACCOUNT_ID=123456789012
    pause
    exit /b 1
)

set ENVIRONMENT=%1
set REGION=%2

if "%ENVIRONMENT%"=="" set ENVIRONMENT=dev
if "%REGION%"=="" set REGION=us-east-1

echo Conta AWS: %AWS_ACCOUNT_ID%
echo Ambiente: %ENVIRONMENT%
echo Regiao: %REGION%
echo ========================================
echo.

echo [INFO] Deploy simples para desenvolvimento
echo.
echo ⚠️  IMPORTANTE: Este é um deploy BÁSICO
echo ⚠️  Para produção, use CloudFormation completo
echo.

echo [1/3] Criando repositórios ECR...
aws ecr create-repository --repository-name myerp/api-gateway --region %REGION% 2>nul
aws ecr create-repository --repository-name myerp/auth-service --region %REGION% 2>nul
aws ecr create-repository --repository-name myerp/rh-module --region %REGION% 2>nul
aws ecr create-repository --repository-name myerp/biometria-module --region %REGION% 2>nul
aws ecr create-repository --repository-name myerp/monitoring-module --region %REGION% 2>nul
aws ecr create-repository --repository-name myerp/company-module --region %REGION% 2>nul
aws ecr create-repository --repository-name myerp/financial-module --region %REGION% 2>nul

echo.
echo [2/3] Configurando Parameter Store básico...
call parameter-store-setup.bat %ENVIRONMENT% %REGION%

echo.
echo [3/3] Deploy básico concluído!
echo ========================================
echo    DEPLOY SIMPLES CONCLUÍDO!
echo ========================================
echo.
echo ✅ Repositórios ECR criados
echo ✅ Parameter Store configurado
echo.
echo 📋 PRÓXIMOS PASSOS MANUAIS:
echo.
echo 1. Build das imagens Docker:
echo    docker build -t myerp/api-gateway infrastructure/api-gateway/
echo.
echo 2. Tag e push para ECR:
echo    docker tag myerp/api-gateway %AWS_ACCOUNT_ID%.dkr.ecr.%REGION%.amazonaws.com/myerp/api-gateway
echo    docker push %AWS_ACCOUNT_ID%.dkr.ecr.%REGION%.amazonaws.com/myerp/api-gateway
echo.
echo 3. Criar ECS Cluster manualmente no console AWS
echo.
echo 4. Criar Task Definitions no console AWS
echo.
echo 💡 Para deploy completo automatizado, aguarde implementação do CloudFormation
echo.
pause