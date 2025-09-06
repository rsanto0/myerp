@echo off
echo ===============================================
echo    INICIANDO FINANCIAL MODULE - MyERP
echo ===============================================
echo.
echo 💰 Financial Module (Porta 8086)
echo    - Boletos Bancarios
echo    - Transacoes PIX  
echo    - Conciliacao Bancaria
echo    - Dashboard Financeiro
echo.

cd /d "%~dp0\..\modules\financial-module"

echo [INFO] Verificando se a porta 8086 esta disponivel...
netstat -an | find "8086" >nul
if %errorlevel% == 0 (
    echo [WARN] Porta 8086 ja esta em uso!
    echo [INFO] Tentando finalizar processo existente...
    for /f "tokens=5" %%a in ('netstat -ano ^| find "8086" ^| find "LISTENING"') do (
        echo [INFO] Finalizando processo %%a
        taskkill /PID %%a /F >nul 2>&1
    )
    timeout /t 3 >nul
)

echo [INFO] Iniciando Financial Module...
echo [INFO] Aguarde, carregando dependencias...
echo.

start "Financial Module" cmd /k "echo Financial Module iniciado na porta 8086 && echo. && echo Endpoints disponiveis: && echo - API: http://localhost:8086/api/financial && echo - H2 Console: http://localhost:8086/h2-console && echo - Dashboard: http://localhost:8086/api/financial/dashboard/empresa/1 && echo - Health: http://localhost:8086/actuator/health && echo. && mvn spring-boot:run -Dspring.profiles.active=local"

echo [INFO] Financial Module iniciando em nova janela...
echo [INFO] Aguarde alguns segundos para inicializacao completa
echo.
echo ===============================================
echo  FINANCIAL MODULE EM EXECUCAO
echo ===============================================
echo.
echo 📊 URLs Importantes:
echo    API Base: http://localhost:8086/api/financial
echo    H2 Console: http://localhost:8086/h2-console
echo    Dashboard: http://localhost:8086/api/financial/dashboard/empresa/1
echo    Health Check: http://localhost:8086/actuator/health
echo.
echo 💰 Funcionalidades:
echo    ✅ Boletos Bancarios
echo    ✅ Transacoes PIX
echo    ✅ Conciliacao Bancaria  
echo    ✅ Dashboard Financeiro
echo    ✅ Integracao Company Module
echo.
echo 🧪 Testes:
echo    Collection: financial-module.postman_collection.json
echo    Dados de exemplo ja incluidos
echo.
echo Pressione qualquer tecla para voltar...
pause >nul