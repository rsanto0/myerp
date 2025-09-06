@echo off
setlocal enabledelayedexpansion

if "%~1"=="" (
    echo ERRO: Ambiente nao especificado
    echo Uso: rollback-complete.bat [AMBIENTE] [REGIAO]
    echo Exemplo: rollback-complete.bat prod us-east-1
    exit /b 1
)

if "%~2"=="" (
    echo ERRO: Regiao nao especificada
    echo Uso: rollback-complete.bat [AMBIENTE] [REGIAO]
    echo Exemplo: rollback-complete.bat prod us-east-1
    exit /b 1
)

set ENVIRONMENT=%1
set AWS_REGION=%2

echo ========================================
echo  ROLLBACK COMPLETO - MyERP AWS
echo ========================================
echo.
echo Ambiente: %ENVIRONMENT%
echo Regiao: %AWS_REGION%
echo.
echo ⚠️  ATENCAO: TODOS OS RECURSOS SERAO DELETADOS!
echo ⚠️  DADOS SERAO PERDIDOS PERMANENTEMENTE!
echo.
set /p CONFIRM="Digite 'DELETE' para confirmar: "

if not "%CONFIRM%"=="DELETE" (
    echo Operacao cancelada.
    exit /b 0
)

echo.
echo [INFO] Iniciando rollback completo...
echo.

REM ========================================
REM PASSO 1: Deletar CloudFormation Stacks
REM ========================================

echo [STEP 1] Deletando stacks CloudFormation...

echo [1/3] Deletando ECS Stack...
aws cloudformation delete-stack --stack-name myerp-ecs-%ENVIRONMENT% --region %AWS_REGION%
if !errorlevel! neq 0 (
    echo [WARN] Erro ao deletar ECS stack ou stack nao existe
) else (
    echo [INFO] Aguardando conclusao da delecao ECS...
    aws cloudformation wait stack-delete-complete --stack-name myerp-ecs-%ENVIRONMENT% --region %AWS_REGION%
    echo [OK] ECS Stack deletada
)

echo [2/3] Deletando DNS Stack...
aws cloudformation delete-stack --stack-name myerp-dns-%ENVIRONMENT% --region %AWS_REGION%
if !errorlevel! neq 0 (
    echo [WARN] Erro ao deletar DNS stack ou stack nao existe
) else (
    echo [INFO] Aguardando conclusao da delecao DNS...
    aws cloudformation wait stack-delete-complete --stack-name myerp-dns-%ENVIRONMENT% --region %AWS_REGION%
    echo [OK] DNS Stack deletada
)

echo [3/3] Deletando Infrastructure Stack...
aws cloudformation delete-stack --stack-name myerp-infrastructure-%ENVIRONMENT% --region %AWS_REGION%
if !errorlevel! neq 0 (
    echo [WARN] Erro ao deletar Infrastructure stack ou stack nao existe
) else (
    echo [INFO] Aguardando conclusao da delecao Infrastructure...
    aws cloudformation wait stack-delete-complete --stack-name myerp-infrastructure-%ENVIRONMENT% --region %AWS_REGION%
    echo [OK] Infrastructure Stack deletada
)

REM ========================================
REM PASSO 2: Limpar Recursos Orfaos
REM ========================================

echo.
echo [STEP 2] Limpando recursos orfaos...

echo [INFO] Deletando ECR repositories...
for %%r in (api-gateway auth-service rh-module biometria-module monitoring-module) do (
    aws ecr delete-repository --repository-name myerp-%%r --force --region %AWS_REGION% 2>nul
    if !errorlevel! equ 0 (
        echo [OK] ECR myerp-%%r deletado
    ) else (
        echo [WARN] ECR myerp-%%r nao encontrado
    )
)

echo [INFO] Deletando S3 buckets...
for /f "tokens=*" %%b in ('aws s3 ls ^| findstr myerp-%ENVIRONMENT%') do (
    set BUCKET_LINE=%%b
    for %%a in (!BUCKET_LINE!) do set BUCKET=%%a
    echo [INFO] Deletando bucket: !BUCKET!
    aws s3 rm s3://!BUCKET! --recursive --region %AWS_REGION% 2>nul
    aws s3 rb s3://!BUCKET! --region %AWS_REGION% 2>nul
    if !errorlevel! equ 0 (
        echo [OK] Bucket !BUCKET! deletado
    )
)

echo [INFO] Deletando CloudWatch Log Groups...
for %%l in (api-gateway auth-service rh-module biometria-module monitoring-module) do (
    aws logs delete-log-group --log-group-name /aws/ecs/myerp-%%l --region %AWS_REGION% 2>nul
    if !errorlevel! equ 0 (
        echo [OK] Log group myerp-%%l deletado
    ) else (
        echo [WARN] Log group myerp-%%l nao encontrado
    )
)

echo [INFO] Deletando Parameter Store...
for %%p in (database/password jwt/secret) do (
    aws ssm delete-parameter --name /myerp/%ENVIRONMENT%/%%p --region %AWS_REGION% 2>nul
    if !errorlevel! equ 0 (
        echo [OK] Parameter %%p deletado
    ) else (
        echo [WARN] Parameter %%p nao encontrado
    )
)

echo [INFO] Deletando Secrets Manager...
aws secretsmanager delete-secret --secret-id myerp-%ENVIRONMENT%-db-credentials --force-delete-without-recovery --region %AWS_REGION% 2>nul
if !errorlevel! equ 0 (
    echo [OK] Secret db-credentials deletado
) else (
    echo [WARN] Secret db-credentials nao encontrado
)

REM ========================================
REM PASSO 3: Verificacao Final
REM ========================================

echo.
echo [STEP 3] Verificacao final...

echo [INFO] Verificando stacks restantes...
aws cloudformation list-stacks --query "StackSummaries[?contains(StackName, 'myerp') && StackStatus != 'DELETE_COMPLETE'].{Name:StackName,Status:StackStatus}" --output table --region %AWS_REGION%

echo.
echo ========================================
echo  ROLLBACK COMPLETO FINALIZADO
echo ========================================
echo.
echo ✅ Todos os recursos AWS do MyERP foram removidos
echo ✅ Cobranca AWS deve parar em algumas horas
echo ✅ Dados foram permanentemente deletados
echo.
echo ⚠️  Verifique sua conta AWS para confirmar
echo ⚠️  Monitore a fatura para confirmar parada da cobranca
echo.
pause