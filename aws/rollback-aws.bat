@echo off
echo ========================================
echo    ROLLBACK SIMPLES AWS - MyERP
echo ========================================
echo.

set ENVIRONMENT=%1
set REGION=%2

if "%ENVIRONMENT%"=="" set ENVIRONMENT=dev
if "%REGION%"=="" set REGION=us-east-1

echo ⚠️  ATENÇÃO: Esta operação irá remover recursos básicos do ambiente %ENVIRONMENT%
echo.
echo Ambiente: %ENVIRONMENT%
echo Região: %REGION%
echo.
set /p CONFIRM="Digite 'SIM' para continuar: "
if not "%CONFIRM%"=="SIM" (
    echo Operação cancelada.
    pause
    exit /b 0
)

echo.
echo [1/3] Removendo repositórios ECR...
aws ecr delete-repository --repository-name myerp/api-gateway --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp/auth-service --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp/rh-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp/biometria-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp/monitoring-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp/company-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp/financial-module --force --region %REGION% 2>nul

echo.
echo [2/3] Removendo parâmetros...
aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/database/port --region %REGION% 2>nul
aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/database/name --region %REGION% 2>nul
aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/services/eureka/url --region %REGION% 2>nul
aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/services/auth/url --region %REGION% 2>nul
aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/logging/level --region %REGION% 2>nul

echo.
echo [3/3] Limpeza concluída!
echo ========================================
echo    ROLLBACK SIMPLES CONCLUÍDO!
echo ========================================
echo.
echo ✅ Repositórios ECR removidos
echo ✅ Parâmetros removidos
echo.
echo 💡 IMPORTANTE: Recursos criados manualmente (ECS, RDS, etc.)
echo    devem ser removidos manualmente no console AWS
echo.
pause