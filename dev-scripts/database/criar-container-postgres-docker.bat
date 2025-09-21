@echo off
echo ========================================
echo 🗄️  MyERP - Iniciando PostgreSQL
echo ========================================
echo.

REM Definir cores
set GREEN=[92m
set YELLOW=[93m
set RED=[91m
set BLUE=[94m
set RESET=[0m

echo %BLUE%🗄️  Verificando PostgreSQL...%RESET%

REM Verificar se PostgreSQL já está rodando
netstat -an | find "5432" >nul
if %errorlevel% equ 0 (
    echo %GREEN%✅ PostgreSQL já está rodando na porta 5432%RESET%
    goto :end
)

echo %YELLOW%🚀 Tentando iniciar PostgreSQL...%RESET%

REM Tentar iniciar serviço do PostgreSQL
net start postgresql-x64-15 >nul 2>&1
if %errorlevel% equ 0 (
    echo %GREEN%✅ PostgreSQL iniciado via serviço Windows%RESET%
    goto :verify
)

REM Tentar outras versões comuns
net start postgresql-x64-14 >nul 2>&1
if %errorlevel% equ 0 (
    echo %GREEN%✅ PostgreSQL 14 iniciado via serviço Windows%RESET%
    goto :verify
)

net start postgresql-x64-13 >nul 2>&1
if %errorlevel% equ 0 (
    echo %GREEN%✅ PostgreSQL 13 iniciado via serviço Windows%RESET%
    goto :verify
)

REM Se não conseguiu via serviço, tentar Docker
echo %YELLOW%🐳 Tentando iniciar via Docker...%RESET%
docker run -d --name myerp-postgres ^
    -e POSTGRES_DB=myerp ^
    -e POSTGRES_USER=myerp_user ^
    -e POSTGRES_PASSWORD=myerp_pass ^
    -p 5432:5432 ^
    postgres:15 >nul 2>&1

if %errorlevel% equ 0 (
    echo %GREEN%✅ PostgreSQL iniciado via Docker%RESET%
    echo %YELLOW%⏳ Aguardando PostgreSQL inicializar...%RESET%
    timeout /t 10 /nobreak >nul
    goto :verify
)

echo %RED%❌ Não foi possível iniciar PostgreSQL%RESET%
echo %YELLOW%💡 Opções:%RESET%
echo   1. Instalar PostgreSQL: https://www.postgresql.org/download/
echo   2. Instalar Docker: https://www.docker.com/products/docker-desktop
echo   3. Iniciar manualmente o serviço PostgreSQL
echo.
pause
exit /b 1

:verify
echo %YELLOW%🔍 Verificando conexão...%RESET%
timeout /t 5 /nobreak >nul

netstat -an | find "5432" >nul
if %errorlevel% equ 0 (
    echo %GREEN%✅ PostgreSQL rodando na porta 5432%RESET%
) else (
    echo %RED%❌ PostgreSQL não está respondendo na porta 5432%RESET%
    exit /b 1
)

:end
echo.
echo %BLUE%📊 Informações de Conexão:%RESET%
echo   - Host: localhost
echo   - Porta: 5432
echo   - Usuário: myerp_user
echo   - Senha: myerp_pass
echo   - Databases: myerp_auth, myerp_rh, myerp_biometria, myerp_devices, etc.
echo.
echo %GREEN%🎉 PostgreSQL pronto para uso!%RESET%