@echo off
echo ========================================
echo    ROLLBACK AWS - MyERP
echo ========================================
echo.

REM Verificar parâmetros
set ENVIRONMENT=%1
set REGION=%2

if "%ENVIRONMENT%"=="" (
    echo ERRO: Ambiente não especificado!
    echo.
    echo Uso: rollback-aws.bat [AMBIENTE] [REGIÃO]
    echo Exemplo: rollback-aws.bat prod us-east-1
    pause
    exit /b 1
)

if "%REGION%"=="" set REGION=us-east-1

echo ⚠️  ATENÇÃO: Esta operação irá REMOVER TODOS os recursos do ambiente %ENVIRONMENT%!
echo ⚠️  DADOS SERÃO PERDIDOS PERMANENTEMENTE!
echo.
echo Ambiente: %ENVIRONMENT%
echo Região: %REGION%
echo.
set /p CONFIRM="Digite 'CONFIRMO' para continuar: "
if not "%CONFIRM%"=="CONFIRMO" (
    echo Operação cancelada.
    pause
    exit /b 0
)

echo.
echo [1/4] Removendo ECS Services e Tasks...
aws cloudformation delete-stack --stack-name myerp-ecs-%ENVIRONMENT% --region %REGION%
if %errorlevel% neq 0 (
    echo AVISO: Stack ECS não encontrada ou já removida
)

echo.
echo [2/4] Aguardando remoção do ECS...
aws cloudformation wait stack-delete-complete --stack-name myerp-ecs-%ENVIRONMENT% --region %REGION% 2>nul

echo.
echo [3/4] Removendo DNS (Route 53)...
aws cloudformation delete-stack --stack-name myerp-dns-%ENVIRONMENT% --region %REGION%
if %errorlevel% neq 0 (
    echo AVISO: Stack DNS não encontrada ou já removida
)

echo.
echo Aguardando remoção do DNS...
aws cloudformation wait stack-delete-complete --stack-name myerp-dns-%ENVIRONMENT% --region %REGION% 2>nul

echo.
echo [4/4] Removendo Infraestrutura (VPC, RDS, ALB)...
aws cloudformation delete-stack --stack-name myerp-infrastructure-%ENVIRONMENT% --region %REGION%
if %errorlevel% neq 0 (
    echo AVISO: Stack Infrastructure não encontrada ou já removida
)

echo.
echo Aguardando remoção da infraestrutura (pode demorar 10-15 minutos)...
aws cloudformation wait stack-delete-complete --stack-name myerp-infrastructure-%ENVIRONMENT% --region %REGION%

echo.
echo [LIMPEZA] Removendo recursos órfãos...

REM ECR Repositories
echo Removendo repositórios ECR...
aws ecr delete-repository --repository-name myerp-api-gateway --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp-auth-service --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp-rh-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp-biometria-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp-company-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp-financial-module --force --region %REGION% 2>nul
aws ecr delete-repository --repository-name myerp-monitoring-module --force --region %REGION% 2>nul

REM Parameter Store
echo Removendo parâmetros...
aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/database/password --region %REGION% 2>nul
aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/jwt/secret --region %REGION% 2>nul

REM CloudWatch Logs
echo Removendo logs...
aws logs delete-log-group --log-group-name /aws/ecs/myerp-api-gateway --region %REGION% 2>nul
aws logs delete-log-group --log-group-name /aws/ecs/myerp-auth-service --region %REGION% 2>nul
aws logs delete-log-group --log-group-name /aws/ecs/myerp-rh-module --region %REGION% 2>nul
aws logs delete-log-group --log-group-name /aws/ecs/myerp-biometria-module --region %REGION% 2>nul
aws logs delete-log-group --log-group-name /aws/ecs/myerp-company-module --region %REGION% 2>nul
aws logs delete-log-group --log-group-name /aws/ecs/myerp-financial-module --region %REGION% 2>nul
aws logs delete-log-group --log-group-name /aws/ecs/myerp-monitoring-module --region %REGION% 2>nul

echo.
echo [VERIFICAÇÃO] Verificando remoção...
echo.
echo Stacks restantes:
aws cloudformation list-stacks --query "StackSummaries[?contains(StackName, 'myerp') && StackStatus != 'DELETE_COMPLETE'].[StackName,StackStatus]" --output table --region %REGION%

echo.
echo ========================================
echo    ROLLBACK CONCLUÍDO!
echo ========================================
echo.
echo ✅ Ambiente %ENVIRONMENT% removido da região %REGION%
echo ✅ Recursos órfãos limpos
echo ✅ Cobrança AWS deve parar em algumas horas
echo.
echo 💡 Dica: Monitore sua fatura AWS para confirmar
echo.
pause