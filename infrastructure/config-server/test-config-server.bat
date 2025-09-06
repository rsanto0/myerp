@echo off
echo ========================================
echo    TESTANDO CONFIG SERVER - MyERP
echo ========================================
echo.

echo [INFO] Compilando Config Server...
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Falha na compilacao
    goto :error
)
echo ✅ Compilacao bem-sucedida

echo.
echo [INFO] Iniciando Config Server (teste de 15 segundos)...
start /min "Config Server Test" cmd /c "mvn spring-boot:run"

echo [INFO] Aguardando inicializacao...
ping 127.0.0.1 -n 16 >nul

echo [INFO] Testando endpoint de configuracoes...
curl -s http://localhost:8888/application/default >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Config Server respondendo corretamente
) else (
    echo ❌ Config Server nao esta respondendo
)

echo.
echo [INFO] Parando Config Server...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8888"') do (
    taskkill /f /pid %%a >nul 2>&1
)

echo.
echo ========================================
echo   ✅ TESTE CONCLUIDO
echo ========================================
goto :end

:error
echo.
echo ========================================
echo   ❌ ERRO NO TESTE
echo ========================================

:end
pause