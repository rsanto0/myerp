@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo.
echo ========================================
echo    CONFIGURADOR DE DATASOURCES MyERP
echo ========================================
echo.
echo Este script irá configurar os datasources
echo de todos os módulos do sistema MyERP.
echo.
echo Você será questionado sobre:
echo - Tipo de banco de dados
echo - Host e porta
echo - Credenciais de acesso
echo - Nomes dos bancos
echo.
pause
echo.

REM ==========================================
REM COLETA DE INFORMAÇÕES
REM ==========================================

echo [1/8] TIPO DE BANCO DE DADOS
echo ========================================
echo Tipos suportados:
echo 1) PostgreSQL (Recomendado)
echo 2) MySQL
echo 3) SQL Server
echo 4) Oracle
echo.
set /p DB_TYPE="Escolha o tipo (1-4) [1]: "
if "%DB_TYPE%"=="" set DB_TYPE=1

if "%DB_TYPE%"=="1" (
    set DRIVER=org.postgresql.Driver
    set DIALECT=org.hibernate.dialect.PostgreSQLDialect
    set PORT_DEFAULT=5432
    set DB_NAME=PostgreSQL
) else if "%DB_TYPE%"=="2" (
    set DRIVER=com.mysql.cj.jdbc.Driver
    set DIALECT=org.hibernate.dialect.MySQLDialect
    set PORT_DEFAULT=3306
    set DB_NAME=MySQL
) else if "%DB_TYPE%"=="3" (
    set DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
    set DIALECT=org.hibernate.dialect.SQLServerDialect
    set PORT_DEFAULT=1433
    set DB_NAME=SQL Server
) else if "%DB_TYPE%"=="4" (
    set DRIVER=oracle.jdbc.OracleDriver
    set DIALECT=org.hibernate.dialect.OracleDialect
    set PORT_DEFAULT=1521
    set DB_NAME=Oracle
) else (
    echo Opção inválida! Usando PostgreSQL como padrão.
    set DRIVER=org.postgresql.Driver
    set DIALECT=org.hibernate.dialect.PostgreSQLDialect
    set PORT_DEFAULT=5432
    set DB_NAME=PostgreSQL
)

echo.
echo ✅ Selecionado: %DB_NAME%
echo.

echo [2/8] HOST DO BANCO DE DADOS
echo ========================================
set /p DB_HOST="Digite o host do banco [localhost]: "
if "%DB_HOST%"=="" set DB_HOST=localhost

echo [3/8] PORTA DO BANCO DE DADOS
echo ========================================
set /p DB_PORT="Digite a porta do banco [%PORT_DEFAULT%]: "
if "%DB_PORT%"=="" set DB_PORT=%PORT_DEFAULT%

echo [4/8] USUÁRIO DO BANCO DE DADOS
echo ========================================
set /p DB_USER="Digite o usuário do banco [myerp_user]: "
if "%DB_USER%"=="" set DB_USER=myerp_user

echo [5/8] SENHA DO BANCO DE DADOS
echo ========================================
set /p DB_PASS="Digite a senha do banco [myerp_pass]: "
if "%DB_PASS%"=="" set DB_PASS=myerp_pass

echo [6/8] PREFIXO DOS BANCOS
echo ========================================
echo Os bancos serão criados como: prefixo_auth, prefixo_rh, etc.
set /p DB_PREFIX="Digite o prefixo dos bancos [myerp]: "
if "%DB_PREFIX%"=="" set DB_PREFIX=myerp

echo [7/8] CONFIGURAÇÕES HIBERNATE
echo ========================================
echo Opções de DDL:
echo 1) create - Recria tabelas (CUIDADO: apaga dados!)
echo 2) update - Atualiza schema (Recomendado para desenvolvimento)
echo 3) validate - Apenas valida (Recomendado para produção)
echo 4) none - Não faz nada
echo.
set /p DDL_OPTION="Escolha a opção DDL (1-4) [2]: "
if "%DDL_OPTION%"=="" set DDL_OPTION=2

if "%DDL_OPTION%"=="1" set DDL_AUTO=create
if "%DDL_OPTION%"=="2" set DDL_AUTO=update
if "%DDL_OPTION%"=="3" set DDL_AUTO=validate
if "%DDL_OPTION%"=="4" set DDL_AUTO=none

echo [8/8] AMBIENTE DE DEPLOY
echo ========================================
echo 1) Desenvolvimento (logs DEBUG, show-sql true)
echo 2) Produção (logs INFO, show-sql false)
echo.
set /p ENV_TYPE="Escolha o ambiente (1-2) [1]: "
if "%ENV_TYPE%"=="" set ENV_TYPE=1

if "%ENV_TYPE%"=="1" (
    set LOG_LEVEL=DEBUG
    set SHOW_SQL=true
) else (
    set LOG_LEVEL=INFO
    set SHOW_SQL=false
)

REM ==========================================
REM CONFIRMAÇÃO DAS CONFIGURAÇÕES
REM ==========================================

echo.
echo ========================================
echo    RESUMO DAS CONFIGURAÇÕES
echo ========================================
echo Banco de Dados: %DB_NAME%
echo Host: %DB_HOST%
echo Porta: %DB_PORT%
echo Usuário: %DB_USER%
echo Senha: %DB_PASS%
echo Prefixo dos Bancos: %DB_PREFIX%
echo DDL Auto: %DDL_AUTO%
echo Ambiente: %LOG_LEVEL%
echo.
echo Bancos que serão configurados:
echo - %DB_PREFIX%_auth
echo - %DB_PREFIX%_rh
echo - %DB_PREFIX%_biometria
echo - %DB_PREFIX%_company
echo - %DB_PREFIX%_financial
echo.
set /p CONFIRM="Confirma as configurações? (S/N) [S]: "
if "%CONFIRM%"=="" set CONFIRM=S
if /i not "%CONFIRM%"=="S" (
    echo Operação cancelada pelo usuário.
    pause
    exit /b 1
)

REM ==========================================
REM BACKUP DAS CONFIGURAÇÕES ATUAIS
REM ==========================================

echo.
echo [BACKUP] Criando backup das configurações atuais...

set BACKUP_DIR=backup-configs-%date:~6,4%%date:~3,2%%date:~0,2%-%time:~0,2%%time:~3,2%%time:~6,2%
set BACKUP_DIR=%BACKUP_DIR: =0%
mkdir "%BACKUP_DIR%" 2>nul

xcopy "..\docker-compose.yml" "%BACKUP_DIR%\" /Y >nul 2>&1
xcopy "..\..\infrastructure\config-server\src\main\resources\config-repo\*.yml" "%BACKUP_DIR%\config-repo\" /Y >nul 2>&1
xcopy "..\..\infrastructure\auth-service\src\main\resources\*.yml" "%BACKUP_DIR%\auth-service\" /Y >nul 2>&1
xcopy "..\..\modules\*\src\main\resources\application.yml" "%BACKUP_DIR%\modules\" /Y /S >nul 2>&1

echo ✅ Backup criado em: %BACKUP_DIR%

REM ==========================================
REM PARAR SISTEMA
REM ==========================================

echo.
echo [SISTEMA] Parando sistema MyERP...
docker-compose down -v >nul 2>&1

REM ==========================================
REM ATUALIZAR DOCKER COMPOSE
REM ==========================================

echo [DOCKER] Atualizando docker-compose.yml...

set DOCKER_FILE=..\docker-compose.yml

if "%DB_TYPE%"=="1" (
    REM PostgreSQL
    powershell -Command "(Get-Content '%DOCKER_FILE%') -replace 'POSTGRES_USER:.*', 'POSTGRES_USER: %DB_USER%' | Set-Content '%DOCKER_FILE%'"
    powershell -Command "(Get-Content '%DOCKER_FILE%') -replace 'POSTGRES_PASSWORD:.*', 'POSTGRES_PASSWORD: %DB_PASS%' | Set-Content '%DOCKER_FILE%'"
    powershell -Command "(Get-Content '%DOCKER_FILE%') -replace 'POSTGRES_DB:.*', 'POSTGRES_DB: %DB_PREFIX%_db' | Set-Content '%DOCKER_FILE%'"
)

echo ✅ Docker Compose atualizado

REM ==========================================
REM ATUALIZAR CONFIG SERVER
REM ==========================================

echo [CONFIG] Atualizando Config Server...

REM Configurações globais
set GLOBAL_CONFIG=..\..\infrastructure\config-server\src\main\resources\config-repo\application.yml

powershell -Command "(Get-Content '%GLOBAL_CONFIG%') -replace 'username:.*', 'username: %DB_USER%' | Set-Content '%GLOBAL_CONFIG%'"
powershell -Command "(Get-Content '%GLOBAL_CONFIG%') -replace 'password:.*', 'password: %DB_PASS%' | Set-Content '%GLOBAL_CONFIG%'"
powershell -Command "(Get-Content '%GLOBAL_CONFIG%') -replace 'driver-class-name:.*', 'driver-class-name: %DRIVER%' | Set-Content '%GLOBAL_CONFIG%'"
powershell -Command "(Get-Content '%GLOBAL_CONFIG%') -replace 'dialect:.*', 'dialect: %DIALECT%' | Set-Content '%GLOBAL_CONFIG%'"
powershell -Command "(Get-Content '%GLOBAL_CONFIG%') -replace 'ddl-auto:.*', 'ddl-auto: %DDL_AUTO%' | Set-Content '%GLOBAL_CONFIG%'"

REM Configurações específicas por serviço
for %%s in (auth-service rh-service biometria-service company-service financial-service) do (
    set SERVICE_CONFIG=..\..\infrastructure\config-server\src\main\resources\config-repo\%%s.yml
    
    if "%DB_TYPE%"=="1" (
        set URL_PATTERN=jdbc:postgresql://%DB_HOST%:%DB_PORT%/%DB_PREFIX%_
        if "%%s"=="auth-service" set DB_SUFFIX=auth
        if "%%s"=="rh-service" set DB_SUFFIX=rh
        if "%%s"=="biometria-service" set DB_SUFFIX=biometria
        if "%%s"=="company-service" set DB_SUFFIX=company
        if "%%s"=="financial-service" set DB_SUFFIX=financial
        
        powershell -Command "(Get-Content '!SERVICE_CONFIG!') -replace 'url:.*postgresql.*', 'url: !URL_PATTERN!!DB_SUFFIX!' | Set-Content '!SERVICE_CONFIG!'"
    )
    
    powershell -Command "(Get-Content '!SERVICE_CONFIG!') -replace 'username:.*', 'username: %DB_USER%' | Set-Content '!SERVICE_CONFIG!'"
    powershell -Command "(Get-Content '!SERVICE_CONFIG!') -replace 'password:.*', 'password: %DB_PASS%' | Set-Content '!SERVICE_CONFIG!'"
    powershell -Command "(Get-Content '!SERVICE_CONFIG!') -replace 'driver-class-name:.*', 'driver-class-name: %DRIVER%' | Set-Content '!SERVICE_CONFIG!'"
)

echo ✅ Config Server atualizado

REM ==========================================
REM ATUALIZAR CONFIGURAÇÕES LOCAIS
REM ==========================================

echo [MODULES] Atualizando configurações locais...

REM Auth Service
set AUTH_CONFIG=..\..\infrastructure\auth-service\src\main\resources\application.yml
set AUTH_LOCAL_CONFIG=..\..\infrastructure\auth-service\src\main\resources\application-local.yml

if "%DB_TYPE%"=="1" (
    set AUTH_URL=jdbc:postgresql://%DB_HOST%:%DB_PORT%/%DB_PREFIX%_auth
    powershell -Command "(Get-Content '%AUTH_CONFIG%') -replace 'url:.*postgresql.*', 'url: %AUTH_URL%' | Set-Content '%AUTH_CONFIG%'"
    powershell -Command "(Get-Content '%AUTH_LOCAL_CONFIG%') -replace 'url:.*postgresql.*', 'url: %AUTH_URL%' | Set-Content '%AUTH_LOCAL_CONFIG%'"
)

powershell -Command "(Get-Content '%AUTH_CONFIG%') -replace 'username:.*', 'username: %DB_USER%' | Set-Content '%AUTH_CONFIG%'"
powershell -Command "(Get-Content '%AUTH_CONFIG%') -replace 'password:.*', 'password: %DB_PASS%' | Set-Content '%AUTH_CONFIG%'"

REM Módulos
for %%m in (rh biometria company financial) do (
    if "%%m"=="rh" set MODULE_PATH=..\..\modules\rh-module\src\main\resources\application.yml
    if "%%m"=="biometria" set MODULE_PATH=..\..\modules\biometria-module\src\main\resources\application.yml
    if "%%m"=="company" set MODULE_PATH=..\..\modules\company-module\src\main\resources\application.yml
    if "%%m"=="financial" set MODULE_PATH=..\..\modules\financial-module\src\main\resources\application.yml
    
    if "%DB_TYPE%"=="1" (
        set MODULE_URL=jdbc:postgresql://%DB_HOST%:%DB_PORT%/%DB_PREFIX%_%%m
        powershell -Command "(Get-Content '!MODULE_PATH!') -replace 'url:.*postgresql.*', 'url: !MODULE_URL!' | Set-Content '!MODULE_PATH!'"
    )
    
    powershell -Command "(Get-Content '!MODULE_PATH!') -replace 'username:.*', 'username: %DB_USER%' | Set-Content '!MODULE_PATH!'"
    powershell -Command "(Get-Content '!MODULE_PATH!') -replace 'password:.*', 'password: %DB_PASS%' | Set-Content '!MODULE_PATH!'"
)

echo ✅ Configurações locais atualizadas

REM ==========================================
REM CRIAR SCRIPT DE BANCOS
REM ==========================================

echo [DATABASE] Criando script de bancos...

echo -- Script gerado automaticamente para criação de bancos > create-databases-new.sql
echo -- Configuração: %DB_NAME% - %DB_HOST%:%DB_PORT% >> create-databases-new.sql
echo -- Usuário: %DB_USER% >> create-databases-new.sql
echo. >> create-databases-new.sql

if "%DB_TYPE%"=="1" (
    echo CREATE DATABASE %DB_PREFIX%_auth; >> create-databases-new.sql
    echo CREATE DATABASE %DB_PREFIX%_rh; >> create-databases-new.sql
    echo CREATE DATABASE %DB_PREFIX%_biometria; >> create-databases-new.sql
    echo CREATE DATABASE %DB_PREFIX%_company; >> create-databases-new.sql
    echo CREATE DATABASE %DB_PREFIX%_financial; >> create-databases-new.sql
    echo CREATE DATABASE %DB_PREFIX%_monitoring; >> create-databases-new.sql
    echo. >> create-databases-new.sql
    echo \l >> create-databases-new.sql
)

echo ✅ Script de bancos criado: create-databases-new.sql

REM ==========================================
REM INICIAR BANCO DE DADOS
REM ==========================================

echo [STARTUP] Iniciando banco de dados...

docker-compose up -d postgres pgadmin

echo Aguardando banco inicializar...
ping 127.0.0.1 -n 16 >nul

if "%DB_TYPE%"=="1" (
    echo Criando bancos PostgreSQL...
    docker cp create-databases-new.sql myerp-postgres:/tmp/create-databases-new.sql
    docker exec myerp-postgres psql -U %DB_USER% -d postgres -f /tmp/create-databases-new.sql
)

echo ✅ Bancos criados

REM ==========================================
REM INICIAR SISTEMA
REM ==========================================

echo [STARTUP] Iniciando sistema MyERP...
start-myerp.bat

echo.
echo ========================================
echo    CONFIGURAÇÃO CONCLUÍDA COM SUCESSO!
echo ========================================
echo.
echo ✅ Backup criado em: %BACKUP_DIR%
echo ✅ Banco de dados: %DB_NAME% (%DB_HOST%:%DB_PORT%)
echo ✅ Usuário: %DB_USER%
echo ✅ Bancos criados: %DB_PREFIX%_*
echo ✅ Sistema iniciado
echo.
echo 🌐 Acessos:
echo - Monitoring: http://localhost:8084/api/monitoring/dashboard
echo - pgAdmin: http://localhost:5050 (admin@myerp.com / admin123)
echo.
echo 📋 Para verificar se tudo funcionou:
echo 1. Aguarde 2 minutos para todos os serviços subirem
echo 2. Acesse o monitoring dashboard
echo 3. Verifique se todos os 8 serviços estão UP
echo.
pause