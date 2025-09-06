@echo off
REM Script de deploy completo para AWS - Windows

echo ==========================================
echo     DEPLOY MyERP PARA AWS
echo ==========================================
echo.

REM Verificar se AWS_ACCOUNT_ID está definido
if "%AWS_ACCOUNT_ID%"=="" (
    echo ERRO: Defina AWS_ACCOUNT_ID primeiro!
    echo.
    echo Opções:
    echo 1. set AWS_ACCOUNT_ID=123456789012
    echo 2. Execute: setup-aws.bat
    echo 3. Execute: aws-manager.bat setup
    echo.
    set /p AUTO_SETUP="Executar setup automaticamente? (S/N) [N]: "
    if /i "!AUTO_SETUP!"=="S" (
        echo Executando setup-aws.bat...
        call setup-aws.bat
        if !errorlevel! neq 0 (
            echo Setup falhou!
            pause
            exit /b 1
        )
    ) else (
        pause
        exit /b 1
    )
)

REM Verificar se AWS CLI está configurado
aws sts get-caller-identity >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: AWS CLI nao configurado!
    echo Execute: aws configure
    pause
    exit /b 1
)

set ENVIRONMENT=%1
set REGION=%2
set DOMAIN=%3

if "%ENVIRONMENT%"=="" set ENVIRONMENT=prod
if "%REGION%"=="" set REGION=us-east-1
if "%DOMAIN%"=="" set DOMAIN=myerp.com

echo Conta AWS: %AWS_ACCOUNT_ID%
echo Ambiente: %ENVIRONMENT%
echo Regiao: %REGION%
echo Dominio: %DOMAIN%
echo ==========================================
echo.

echo [1/6] Criando infraestrutura AWS...
aws cloudformation deploy ^
    --template-file cloudformation-infrastructure.yml ^
    --stack-name myerp-infrastructure-%ENVIRONMENT% ^
    --parameter-overrides Environment=%ENVIRONMENT% ^
    --capabilities CAPABILITY_IAM ^
    --region %REGION%

if %errorlevel% neq 0 (
    echo ERRO: Falha ao criar infraestrutura
    pause
    exit /b 1
)

echo.
echo [2/6] Aguardando infraestrutura ficar pronta...
aws cloudformation wait stack-create-complete ^
    --stack-name myerp-infrastructure-%ENVIRONMENT% ^
    --region %REGION%

echo.
echo [3/6] Configurando Parameter Store...
call parameter-store-setup.bat %ENVIRONMENT% %REGION%

echo.
echo [4/6] Configurando DNS (Route 53)...
REM Obter DNS do Load Balancer
for /f "tokens=*" %%i in ('aws cloudformation describe-stacks --stack-name myerp-infrastructure-%ENVIRONMENT% --query "Stacks[0].Outputs[?OutputKey==`LoadBalancerDNS`].OutputValue" --output text --region %REGION%') do set ALB_DNS=%%i

aws cloudformation deploy ^
    --template-file route53-setup.yml ^
    --stack-name myerp-dns-%ENVIRONMENT% ^
    --parameter-overrides Environment=%ENVIRONMENT% DomainName=%DOMAIN% LoadBalancerDNS=%ALB_DNS% ^
    --region %REGION%

echo.
echo [5/6] Fazendo build das aplicacoes...
cd ..
mvn clean package -DskipTests

echo.
echo [6/6] Deploy concluido!
echo ==========================================
echo     DEPLOY CONCLUIDO COM SUCESSO!
echo ==========================================
echo.
echo URLs disponiveis:
echo - API Gateway: https://api.%ENVIRONMENT%.%DOMAIN%
echo - Eureka: https://eureka.%ENVIRONMENT%.%DOMAIN%
echo - Monitoring: https://monitoring.%ENVIRONMENT%.%DOMAIN%
echo ==========================================
echo.
pause