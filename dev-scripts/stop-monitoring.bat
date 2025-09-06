@echo off
echo ========================================
echo    MyERP - Parando Modulo Monitoramento
echo ========================================
echo.

echo [INFO] Parando Monitoring Module...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8084"') do (
    echo Finalizando processo %%a na porta 8084
    taskkill /f /pid %%a >nul 2>&1
)

echo.
echo ========================================
echo   Monitoring Module parado!
echo ========================================
echo.
pause