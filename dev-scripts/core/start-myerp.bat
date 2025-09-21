@echo off
echo ========================================
echo      INICIANDO SISTEMA MyERP
echo ========================================
echo.

set /p START_MONITORING="Deseja iniciar o modulo de Monitoramento? (s/n): "
echo.

REM Limpeza de arquivos antigos
echo [INFO] Removendo arquivos .properties antigos...
del /q "..\infrastructure\auth-service\src\main\resources\application.properties" 2>nul
del /q "..\modules\rh-module\src\main\resources\application.properties" 2>nul

REM Verificar se PostgreSQL está rodando
echo [1/8] Verificando PostgreSQL...
docker ps | findstr myerp-postgres >nul
if %errorlevel% neq 0 (
    echo PostgreSQL nao encontrado. Iniciando...
    docker-compose up -d postgres pgadmin
    echo Aguardando PostgreSQL inicializar...
    ping 127.0.0.1 -n 16 >nul
    echo Criando bancos necessarios...
    docker cp create-databases.sql myerp-postgres:/tmp/create-databases.sql
    docker exec myerp-postgres psql -U myerp_user -d postgres -f /tmp/create-databases.sql >nul 2>&1
    echo Bancos criados com sucesso!
) else (
    echo PostgreSQL ja esta rodando!
)

echo.
echo [2/8] Iniciando Service Discovery (Eureka)...
echo Porta: 8761
start "Eureka Server" cmd /k "cd ..\infrastructure\service-discovery && mvn spring-boot:run"
echo Aguardando Eureka inicializar...
ping 127.0.0.1 -n 31 >nul

echo.
echo [3/8] Iniciando Config Server...
echo Porta: 8888
start "Config Server" cmd /k "cd ..\infrastructure\config-server && mvn spring-boot:run"
echo Aguardando Config Server inicializar...
ping 127.0.0.1 -n 21 >nul

echo.
echo [4/8] Iniciando Auth Service...
echo Porta: 8081
echo Profile: local (usando application.yml)
start "Auth Service" cmd /k "cd ..\infrastructure\auth-service && mvn spring-boot:run -Dspring.profiles.active=local"
echo Aguardando Auth Service inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [5/8] Iniciando API Gateway...
echo Porta: 8080
start "API Gateway" cmd /k "cd ..\infrastructure\api-gateway && mvn spring-boot:run"
echo Aguardando API Gateway inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [6/8] Iniciando RH Module...
echo Porta: 8082
echo Profile: local (usando application.yml)
start "RH Module" cmd /k "cd ..\modules\rh-module && mvn spring-boot:run -Dspring.profiles.active=local"
echo Aguardando RH Module inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [7/8] Iniciando Biometria Module...
echo Porta: 8083
echo Profile: local (usando application.yml)
start "Biometria Module" cmd /k "cd ..\modules\biometria-module && mvn spring-boot:run -Dspring.profiles.active=local"
echo Aguardando Biometria Module inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [8/8] Iniciando Company Module...
echo Porta: 8085
echo Profile: local (usando application.yml)
start "Company Module" cmd /k "cd ..\modules\company-module && mvn spring-boot:run -Dspring.profiles.active=local"
echo Aguardando Company Module inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [EXTRA] Iniciando Financial Module...
echo Porta: 8086
echo Profile: local (usando application.yml)
start "Financial Module" cmd /k "cd ..\modules\financial-module && mvn spring-boot:run -Dspring.profiles.active=local"
echo Aguardando Financial Module inicializar...
ping 127.0.0.1 -n 26 >nul

if /i "%START_MONITORING%"=="s" (
    echo.
    echo [EXTRA] Iniciando Monitoring Module...
    echo Porta: 8084
    echo Aguardando todos os servicos estarem prontos...
    ping 127.0.0.1 -n 16 >nul
    start "Monitoring Module" cmd /k "cd ..\modules\monitoring-module && mvn spring-boot:run -Dspring.profiles.active=local"
    set MONITORING_URL=- Monitoring Dashboard: http://localhost:8084/api/monitoring/dashboard
) else (
    set MONITORING_URL=- Monitoring: NAO INICIADO (use dev-scripts/start-monitoring.bat)
)

echo.
echo ========================================
echo       SISTEMA MyERP INICIADO!
echo ========================================
echo.
echo URLs Disponiveis:
echo - Eureka Dashboard: http://localhost:8761
echo - Config Server: http://localhost:8888
echo - API Gateway: http://localhost:8080
echo - Auth Service: http://localhost:8081
echo - RH Module: http://localhost:8082
echo - Biometria Module: http://localhost:8083
echo - Company Module: http://localhost:8085
echo - Financial Module: http://localhost:8086
echo %MONITORING_URL%
echo - pgAdmin: http://localhost:5050
echo.
echo PostgreSQL:
echo - Host: localhost:5432
echo - User: myerp_user
echo - Pass: myerp_pass
echo.
echo Pressione qualquer tecla para sair...
pause >nul