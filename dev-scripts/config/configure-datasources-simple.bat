@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo.
echo ========================================
echo   CONFIGURADOR SIMPLES DE DATASOURCES
echo ========================================
echo.
echo Este é um configurador simplificado que
echo altera apenas usuário e senha do PostgreSQL.
echo.

REM ==========================================
REM COLETA DE INFORMAÇÕES BÁSICAS
REM ==========================================

echo [1/3] USUÁRIO DO BANCO
echo ========================================
echo Usuário atual: myerp_user
set /p NEW_USER="Digite o novo usuário [myerp_user]: "
if "%NEW_USER%"=="" set NEW_USER=myerp_user

echo [2/3] SENHA DO BANCO
echo ========================================
echo Senha atual: myerp_pass
set /p NEW_PASS="Digite a nova senha [myerp_pass]: "
if "%NEW_PASS%"=="" set NEW_PASS=myerp_pass

echo [3/3] CONFIRMAÇÃO
echo ========================================
echo Novo usuário: %NEW_USER%
echo Nova senha: %NEW_PASS%
echo.
set /p CONFIRM="Confirma a alteração? (S/N) [S]: "
if "%CONFIRM%"=="" set CONFIRM=S
if /i not "%CONFIRM%"=="S" (
    echo Operação cancelada.
    pause
    exit /b 1
)

REM ==========================================
REM BACKUP E ALTERAÇÃO
REM ==========================================

echo.
echo [BACKUP] Criando backup...
set BACKUP_DIR=backup-simple-%date:~6,4%%date:~3,2%%date:~0,2%-%time:~0,2%%time:~3,2%%time:~6,2%
set BACKUP_DIR=%BACKUP_DIR: =0%
mkdir "%BACKUP_DIR%" 2>nul
xcopy "..\docker-compose.yml" "%BACKUP_DIR%\" /Y >nul

echo [SISTEMA] Parando sistema...
cd ..
docker-compose down -v >nul 2>&1
cd dev-scripts

echo [ALTERAÇÃO] Atualizando credenciais...

REM Docker Compose
powershell -Command "(Get-Content '..\docker-compose.yml') -replace 'POSTGRES_USER: myerp_user', 'POSTGRES_USER: %NEW_USER%' | Set-Content '..\docker-compose.yml'"
powershell -Command "(Get-Content '..\docker-compose.yml') -replace 'POSTGRES_PASSWORD: myerp_pass', 'POSTGRES_PASSWORD: %NEW_PASS%' | Set-Content '..\docker-compose.yml'"

REM Config Server - Global
powershell -Command "(Get-Content '..\infrastructure\config-server\src\main\resources\config-repo\application.yml') -replace 'username: myerp_user', 'username: %NEW_USER%' | Set-Content '..\infrastructure\config-server\src\main\resources\config-repo\application.yml'"
powershell -Command "(Get-Content '..\infrastructure\config-server\src\main\resources\config-repo\application.yml') -replace 'password: myerp_pass', 'password: %NEW_PASS%' | Set-Content '..\infrastructure\config-server\src\main\resources\config-repo\application.yml'"

REM Config Server - Serviços
for %%s in (auth-service rh-service biometria-service company-service financial-service) do (
    powershell -Command "(Get-Content '..\infrastructure\config-server\src\main\resources\config-repo\%%s.yml') -replace 'username: myerp_user', 'username: %NEW_USER%' | Set-Content '..\infrastructure\config-server\src\main\resources\config-repo\%%s.yml'"
    powershell -Command "(Get-Content '..\infrastructure\config-server\src\main\resources\config-repo\%%s.yml') -replace 'password: myerp_pass', 'password: %NEW_PASS%' | Set-Content '..\infrastructure\config-server\src\main\resources\config-repo\%%s.yml'"
)

REM Configurações Locais
powershell -Command "(Get-Content '..\infrastructure\auth-service\src\main\resources\application.yml') -replace 'username: myerp_user', 'username: %NEW_USER%' | Set-Content '..\infrastructure\auth-service\src\main\resources\application.yml'"
powershell -Command "(Get-Content '..\infrastructure\auth-service\src\main\resources\application.yml') -replace 'password: myerp_pass', 'password: %NEW_PASS%' | Set-Content '..\infrastructure\auth-service\src\main\resources\application.yml'"

powershell -Command "(Get-Content '..\infrastructure\auth-service\src\main\resources\application-local.yml') -replace 'username: myerp_user', 'username: %NEW_USER%' | Set-Content '..\infrastructure\auth-service\src\main\resources\application-local.yml'"
powershell -Command "(Get-Content '..\infrastructure\auth-service\src\main\resources\application-local.yml') -replace 'password: myerp_pass', 'password: %NEW_PASS%' | Set-Content '..\infrastructure\auth-service\src\main\resources\application-local.yml'"

for %%m in (rh-module biometria-module company-module financial-module) do (
    powershell -Command "(Get-Content '..\modules\%%m\src\main\resources\application.yml') -replace 'username: myerp_user', 'username: %NEW_USER%' | Set-Content '..\modules\%%m\src\main\resources\application.yml'"
    powershell -Command "(Get-Content '..\modules\%%m\src\main\resources\application.yml') -replace 'password: myerp_pass', 'password: %NEW_PASS%' | Set-Content '..\modules\%%m\src\main\resources\application.yml'"
)

echo [BANCO] Iniciando PostgreSQL...
cd ..
docker-compose up -d postgres
cd dev-scripts

echo Aguardando PostgreSQL...
ping 127.0.0.1 -n 16 >nul

echo [BANCO] Criando bancos...
docker cp create-databases.sql myerp-postgres:/tmp/create-databases.sql 2>nul
docker exec myerp-postgres psql -U %NEW_USER% -d postgres -f /tmp/create-databases.sql >nul 2>&1

echo [SISTEMA] Iniciando MyERP...
start-myerp.bat

echo.
echo ========================================
echo    ALTERAÇÃO CONCLUÍDA!
echo ========================================
echo.
echo ✅ Usuário alterado: %NEW_USER%
echo ✅ Senha alterada: %NEW_PASS%
echo ✅ Backup em: %BACKUP_DIR%
echo ✅ Sistema reiniciado
echo.
echo Aguarde 2 minutos e verifique:
echo http://localhost:8084/api/monitoring/dashboard
echo.
pause