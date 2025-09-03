@echo off
echo ========================================
echo      INICIANDO SISTEMA MyERP
echo ========================================
echo.

REM Verificar se PostgreSQL está rodando
echo [1/7] Verificando PostgreSQL...
docker ps | findstr myerp-postgres >nul
if %errorlevel% neq 0 (
    echo PostgreSQL nao encontrado. Iniciando...
    docker-compose up -d postgres pgadmin
    echo Aguardando PostgreSQL inicializar...
    ping 127.0.0.1 -n 16 >nul
) else (
    echo PostgreSQL ja esta rodando!
)

echo.
echo [2/7] Iniciando Service Discovery (Eureka)...
echo Porta: 8761
start "Eureka Server" cmd /k "cd infrastructure\service-discovery && mvn spring-boot:run"
echo Aguardando Eureka inicializar...
ping 127.0.0.1 -n 31 >nul

echo.
echo [3/7] Iniciando Config Server...
echo Porta: 8888
start "Config Server" cmd /k "cd infrastructure\config-server && mvn spring-boot:run"
echo Aguardando Config Server inicializar...
ping 127.0.0.1 -n 21 >nul

echo.
echo [4/7] Iniciando Auth Service...
echo Porta: 8081
start "Auth Service" cmd /k "cd infrastructure\auth-service && mvn spring-boot:run"
echo Aguardando Auth Service inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [5/7] Iniciando API Gateway...
echo Porta: 8080
start "API Gateway" cmd /k "cd infrastructure\api-gateway && mvn spring-boot:run"
echo Aguardando API Gateway inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [6/7] Iniciando RH Module...
echo Porta: 8082
start "RH Module" cmd /k "cd modules\rh-module\sistema-ponto && mvn spring-boot:run"
echo Aguardando RH Module inicializar...
ping 127.0.0.1 -n 26 >nul

echo.
echo [7/7] Iniciando Biometria Module...
echo Porta: 8083
start "Biometria Module" cmd /k "cd modules\biometria-module && mvn spring-boot:run"

echo.
echo ========================================
echo       SISTEMA MyERP INICIADO!
echo ========================================
echo.
echo URLs Disponiveis:
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - Auth Service: http://localhost:8081
echo - RH Module: http://localhost:8082
echo - Biometria Module: http://localhost:8083
echo - pgAdmin: http://localhost:5050
echo.
echo PostgreSQL:
echo - Host: localhost:5432
echo - User: myerp_user
echo - Pass: myerp_pass
echo.
echo Pressione qualquer tecla para sair...
pause >nul