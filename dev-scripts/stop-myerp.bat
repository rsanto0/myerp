@echo off
echo ========================================
echo      PARANDO SISTEMA MyERP
echo ========================================
echo.

set /p STOP_MONITORING="Deseja parar o modulo de Monitoramento? (s/n): "
echo.

if /i "%STOP_MONITORING%"=="s" (
    echo Parando Monitoring Module...
    for /f "tokens=5" %%a in ('netstat -aon ^| find ":8084"') do (
        taskkill /f /pid %%a >nul 2>&1
    )
    echo Monitoring Module parado.
    echo.
)

echo Parando servicos Spring Boot...
taskkill /f /im java.exe 2>nul
echo Servicos Spring Boot parados.

echo.
echo Parando PostgreSQL...
docker-compose down
echo PostgreSQL parado.

echo.
echo ========================================
echo     SISTEMA MyERP PARADO!
echo ========================================
pause