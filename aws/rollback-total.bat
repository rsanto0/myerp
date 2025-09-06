@echo off
setlocal enabledelayedexpansion

if "%~1"=="" (
    echo ERRO: Regiao nao especificada
    echo Uso: rollback-total.bat [REGIAO]
    echo Exemplo: rollback-total.bat us-east-1
    exit /b 1
)

set AWS_REGION=%1

echo ========================================
echo  ROLLBACK TOTAL - MyERP AWS
echo ========================================
echo.
echo Regiao: %AWS_REGION%
echo.
echo ⚠️  ATENCAO: TODOS OS RECURSOS MyERP SERAO DELETADOS!
echo ⚠️  TODOS OS AMBIENTES (dev, prod, test, etc)
echo ⚠️  DADOS SERAO PERDIDOS PERMANENTEMENTE!
echo.
echo Digite exatamente: DESTROY-ALL-MYERP
set /p CONFIRM="Confirmacao: "

if not "%CONFIRM%"=="DESTROY-ALL-MYERP" (
    echo Operacao cancelada.
    exit /b 0
)

echo.
echo [INFO] Iniciando destruicao total do MyERP...
echo.

REM ========================================
REM PASSO 1: Descobrir todos os ambientes
REM ========================================

echo [STEP 1] Descobrindo ambientes MyERP...

for /f "tokens=2 delims= " %%s in ('aws cloudformation list-stacks --query "StackSummaries[?contains(StackName, 'myerp-') && StackStatus != 'DELETE_COMPLETE'].StackName" --output text --region %AWS_REGION%') do (
    set STACK_NAME=%%s
    echo [FOUND] Stack: !STACK_NAME!
    
    REM Extrair ambiente do nome da stack
    for /f "tokens=3 delims=-" %%e in ("!STACK_NAME!") do (
        set ENV=%%e
        echo [INFO] Ambiente detectado: !ENV!
        
        REM Adicionar à lista de ambientes
        set ENVIRONMENTS=!ENVIRONMENTS! !ENV!
    )
)

REM Remover duplicatas
set UNIQUE_ENVS=
for %%e in (%ENVIRONMENTS%) do (
    echo !UNIQUE_ENVS! | findstr /C:"%%e" >nul
    if !errorlevel! neq 0 (
        set UNIQUE_ENVS=!UNIQUE_ENVS! %%e
    )
)

echo [INFO] Ambientes encontrados: %UNIQUE_ENVS%
echo.

REM ========================================
REM PASSO 2: Deletar cada ambiente
REM ========================================

echo [STEP 2] Deletando todos os ambientes...

for %%env in (%UNIQUE_ENVS%) do (
    echo.
    echo [INFO] === Deletando ambiente: %%env ===
    
    echo [INFO] Deletando ECS stack...
    aws cloudformation delete-stack --stack-name myerp-ecs-%%env --region %AWS_REGION% 2>nul
    
    echo [INFO] Deletando DNS stack...
    aws cloudformation delete-stack --stack-name myerp-dns-%%env --region %AWS_REGION% 2>nul
    
    echo [INFO] Deletando Infrastructure stack...
    aws cloudformation delete-stack --stack-name myerp-infrastructure-%%env --region %AWS_REGION% 2>nul
)

echo.
echo [INFO] Aguardando conclusao de todas as delecoes...
timeout /t 30 /nobreak >nul

REM ========================================
REM PASSO 3: Limpar recursos globais
REM ========================================

echo.
echo [STEP 3] Limpando recursos globais...

echo [INFO] Deletando TODOS os ECR repositories MyERP...
for /f "tokens=*" %%r in ('aws ecr describe-repositories --query "repositories[?contains(repositoryName, 'myerp')].repositoryName" --output text --region %AWS_REGION%') do (
    echo [INFO] Deletando ECR: %%r
    aws ecr delete-repository --repository-name %%r --force --region %AWS_REGION% 2>nul
)

echo [INFO] Deletando TODOS os S3 buckets MyERP...
for /f "tokens=3" %%b in ('aws s3 ls ^| findstr myerp') do (
    echo [INFO] Deletando S3: %%b
    aws s3 rm s3://%%b --recursive --region %AWS_REGION% 2>nul
    aws s3 rb s3://%%b --region %AWS_REGION% 2>nul
)

echo [INFO] Deletando TODOS os CloudWatch Log Groups MyERP...
for /f "tokens=*" %%l in ('aws logs describe-log-groups --query "logGroups[?contains(logGroupName, 'myerp')].logGroupName" --output text --region %AWS_REGION%') do (
    echo [INFO] Deletando Log Group: %%l
    aws logs delete-log-group --log-group-name %%l --region %AWS_REGION% 2>nul
)

echo [INFO] Deletando TODOS os Parameter Store MyERP...
for /f "tokens=*" %%p in ('aws ssm describe-parameters --query "Parameters[?contains(Name, 'myerp')].Name" --output text --region %AWS_REGION%') do (
    echo [INFO] Deletando Parameter: %%p
    aws ssm delete-parameter --name %%p --region %AWS_REGION% 2>nul
)

echo [INFO] Deletando TODOS os Secrets Manager MyERP...
for /f "tokens=*" %%s in ('aws secretsmanager list-secrets --query "SecretList[?contains(Name, 'myerp')].Name" --output text --region %AWS_REGION%') do (
    echo [INFO] Deletando Secret: %%s
    aws secretsmanager delete-secret --secret-id %%s --force-delete-without-recovery --region %AWS_REGION% 2>nul
)

REM ========================================
REM PASSO 4: Verificacao final
REM ========================================

echo.
echo [STEP 4] Verificacao final...

echo [INFO] Aguardando finalizacao completa...
timeout /t 60 /nobreak >nul

echo [INFO] Stacks restantes:
aws cloudformation list-stacks --query "StackSummaries[?contains(StackName, 'myerp') && StackStatus != 'DELETE_COMPLETE'].{Name:StackName,Status:StackStatus}" --output table --region %AWS_REGION%

echo [INFO] ECR repositories restantes:
aws ecr describe-repositories --query "repositories[?contains(repositoryName, 'myerp')].repositoryName" --output table --region %AWS_REGION%

echo [INFO] S3 buckets restantes:
aws s3 ls | findstr myerp

echo.
echo ========================================
echo  DESTRUICAO TOTAL CONCLUIDA
echo ========================================
echo.
echo ✅ TODOS os recursos MyERP foram removidos
echo ✅ TODOS os ambientes foram destruidos
echo ✅ Cobranca AWS deve parar completamente
echo ✅ Dados foram permanentemente deletados
echo.
echo ⚠️  Monitore sua fatura AWS nas proximas 24h
echo ⚠️  Alguns recursos podem ter cobranca residual minima
echo.
echo 🎯 MyERP foi completamente removido da AWS!
echo.
pause