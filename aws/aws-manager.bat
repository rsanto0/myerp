@echo off
setlocal enabledelayedexpansion

echo ========================================
echo    AWS MANAGER - MyERP
echo ========================================
echo.

set COMMAND=%1
set PARAM1=%2
set PARAM2=%3
set PARAM3=%4

if "%COMMAND%"=="" goto :show_help

REM Verificar se AWS CLI está instalado
aws --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERRO: AWS CLI não instalado!
    echo.
    echo Baixe em: https://aws.amazon.com/cli/
    pause
    exit /b 1
)

REM Processar comandos
if /i "%COMMAND%"=="setup" goto :setup
if /i "%COMMAND%"=="deploy" goto :deploy
if /i "%COMMAND%"=="rollback" goto :rollback
if /i "%COMMAND%"=="status" goto :status
if /i "%COMMAND%"=="full-deploy" goto :full_deploy
if /i "%COMMAND%"=="help" goto :show_help

echo ❌ Comando inválido: %COMMAND%
goto :show_help

:setup
echo 🔧 Executando setup AWS...
call setup-aws.bat
goto :end

:deploy
if "%PARAM1%"=="" (
    echo ❌ ERRO: Ambiente não especificado!
    echo Uso: aws-manager.bat deploy [AMBIENTE] [REGIÃO] [DOMÍNIO]
    goto :end
)

echo 🚀 Executando deploy simples...
echo Ambiente: %PARAM1%
echo Região: %PARAM2%
echo.

REM Verificar se setup foi feito
if "%AWS_ACCOUNT_ID%"=="" (
    echo ⚠️  AWS_ACCOUNT_ID não definido. Executando setup primeiro...
    call setup-aws.bat
    if %errorlevel% neq 0 (
        echo ❌ Setup falhou!
        goto :end
    )
)

call deploy-aws.bat %PARAM1% %PARAM2%
goto :end

:rollback
if "%PARAM1%"=="" (
    echo ❌ ERRO: Ambiente não especificado!
    echo Uso: aws-manager.bat rollback [AMBIENTE] [REGIÃO]
    goto :end
)

echo 🗑️  Executando rollback...
call rollback-aws.bat %PARAM1% %PARAM2%
goto :end

:status
echo 📊 Status dos recursos AWS...
echo.

REM Verificar se AWS está configurado
aws sts get-caller-identity >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ AWS CLI não configurado!
    echo Execute: aws-manager.bat setup
    goto :end
)

echo 🔍 Verificando stacks CloudFormation...
aws cloudformation list-stacks --query "StackSummaries[?contains(StackName, 'myerp')].[StackName,StackStatus,CreationTime]" --output table

echo.
echo 🔍 Verificando repositórios ECR...
aws ecr describe-repositories --query "repositories[?contains(repositoryName, 'myerp')].[repositoryName,createdAt]" --output table 2>nul

echo.
echo 🔍 Verificando Parameter Store...
aws ssm describe-parameters --query "Parameters[?contains(Name, 'myerp')].[Name,LastModifiedDate]" --output table 2>nul

goto :end

:full_deploy
if "%PARAM1%"=="" (
    echo ❌ ERRO: Ambiente não especificado!
    echo Uso: aws-manager.bat full-deploy [AMBIENTE] [REGIÃO]
    goto :end
)

echo 🚀 Executando deploy completo simples (setup + deploy)...
echo.

echo [1/2] Setup AWS...
call setup-aws.bat
if %errorlevel% neq 0 (
    echo ❌ Setup falhou!
    goto :end
)

echo.
echo [2/2] Deploy simples...
call deploy-aws.bat %PARAM1% %PARAM2%
goto :end

:show_help
echo 📖 AWS Manager - Orquestrador de Scripts AWS
echo.
echo 🎯 COMANDOS DISPONÍVEIS:
echo.
echo   setup                           - Configuração inicial AWS
echo   deploy [env] [region]           - Deploy simples (ECR + Parameter Store)
echo   rollback [env] [region]         - Rollback simples
echo   status                          - Status dos recursos
echo   full-deploy [env] [region]      - Setup + Deploy simples
echo   help                            - Esta ajuda
echo.
echo 💡 EXEMPLOS:
echo.
echo   aws-manager.bat setup
echo   aws-manager.bat deploy dev us-east-1
echo   aws-manager.bat rollback dev us-east-1
echo   aws-manager.bat status
echo   aws-manager.bat full-deploy dev us-east-1
echo.
echo 📋 FLUXO RECOMENDADO:
echo.
echo   1. aws-manager.bat setup          (uma vez)
echo   2. aws-manager.bat deploy ...     (quantas vezes precisar)
echo   3. aws-manager.bat rollback ...   (quando necessário)
echo.
echo 🔗 SCRIPTS INDIVIDUAIS:
echo.
echo   setup-aws.bat      - Setup manual
echo   deploy-aws.bat     - Deploy manual  
echo   rollback-aws.bat   - Rollback manual
echo.

:end
echo.
pause