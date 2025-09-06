@echo off
echo ========================================
echo      PARANDO SISTEMA MyERP
echo ========================================
echo.

set /p STOP_MONITORING="Deseja parar o modulo de Monitoramento? (s/n): "
echo.

echo Parando todos os modulos MyERP...
echo.

echo [1/9] Parando Financial Module (8086)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8086"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo [2/9] Parando Company Module (8085)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8085"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo [3/9] Parando Biometria Module (8083)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8083"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo [4/9] Parando RH Module (8082)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8082"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo [5/9] Parando API Gateway (8080)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo [6/9] Parando Auth Service (8081)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8081"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo [7/9] Parando Config Server (8888)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8888"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo [8/9] Parando Eureka Server (8761)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8761"') do (
    taskkill /f /pid %%a >nul 2>&1
)

if /i "%STOP_MONITORING%"=="s" (
    echo [9/9] Parando Monitoring Module (8084)...
    for /f "tokens=5" %%a in ('netstat -aon ^| find ":8084"') do (
        taskkill /f /pid %%a >nul 2>&1
    )
) else (
    echo [9/9] Monitoring Module nao sera parado (nao foi solicitado)
)

echo.
echo Finalizando processos Java restantes...
taskkill /f /im java.exe >nul 2>&1
echo Todos os servicos Spring Boot parados.

echo.
echo Parando PostgreSQL...
docker-compose down
echo PostgreSQL parado.

echo.
echo ========================================
echo     SISTEMA MyERP PARADO!
echo ========================================
pause