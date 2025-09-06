@echo off
echo ========================================
echo    CONFIGURACAO INICIAL AWS - MyERP
echo ========================================
echo.

echo [PASSO 1] Verificando AWS CLI...
aws --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: AWS CLI nao instalado!
    echo Baixe em: https://aws.amazon.com/cli/
    pause
    exit /b 1
)
echo ✓ AWS CLI encontrado

echo.
echo [PASSO 2] Configurando credenciais AWS...
set /p AWS_ACCESS_KEY="Digite sua AWS Access Key ID: "
set /p AWS_SECRET_KEY="Digite sua AWS Secret Access Key: "
set /p AWS_REGION="Digite a regiao AWS (ex: us-east-1): "

echo.
echo Configurando AWS CLI...
aws configure set aws_access_key_id %AWS_ACCESS_KEY%
aws configure set aws_secret_access_key %AWS_SECRET_KEY%
aws configure set default.region %AWS_REGION%
aws configure set default.output json

echo.
echo [PASSO 3] Testando conexao...
aws sts get-caller-identity >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Credenciais invalidas!
    pause
    exit /b 1
)

echo ✓ Conexao AWS OK

echo.
echo [PASSO 4] Obtendo ID da conta...
for /f "tokens=*" %%i in ('aws sts get-caller-identity --query Account --output text') do set AWS_ACCOUNT_ID=%%i

echo.
echo ========================================
echo    CONFIGURACAO CONCLUIDA!
echo ========================================
echo.
echo Suas informacoes:
echo - Conta AWS: %AWS_ACCOUNT_ID%
echo - Regiao: %AWS_REGION%
echo.
echo IMPORTANTE: Anote seu ID da conta!
echo.
echo Proximo passo:
echo 1. set AWS_ACCOUNT_ID=%AWS_ACCOUNT_ID%
echo 2. cd aws
echo 3. deploy-aws.bat prod %AWS_REGION% myerp.com
echo.
pause