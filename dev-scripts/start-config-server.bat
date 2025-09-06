@echo off
echo ===============================================
echo    INICIANDO CONFIG SERVER - MyERP
echo ===============================================
echo.
echo ⚙️ Config Server (Porta 8888)
echo    - Configuracoes Centralizadas
echo    - Gestao de Profiles
echo    - Refresh Dinamico
echo.

cd /d "%~dp0\..\infrastructure\config-server"

echo [INFO] Verificando se a porta 8888 esta disponivel...
netstat -an | find "8888" >nul
if %errorlevel% == 0 (
    echo [WARN] Porta 8888 ja esta em uso!
    echo [INFO] Tentando finalizar processo existente...
    for /f "tokens=5" %%a in ('netstat -ano ^| find "8888" ^| find "LISTENING"') do (
        echo [INFO] Finalizando processo %%a
        taskkill /PID %%a /F >nul 2>&1
    )
    timeout /t 3 >nul
)

echo [INFO] Iniciando Config Server...
echo [INFO] Aguarde, carregando configuracoes...
echo.

start "Config Server" cmd /k "echo Config Server iniciado na porta 8888 && echo. && echo Endpoints disponiveis: && echo - Configuracoes: http://localhost:8888/{service}/{profile} && echo - Health: http://localhost:8888/actuator/health && echo - Refresh: http://localhost:8888/actuator/refresh && echo. && mvn spring-boot:run -Dspring.profiles.active=native"

echo [INFO] Config Server iniciando em nova janela...
echo [INFO] Aguarde alguns segundos para inicializacao completa
echo.
echo ===============================================
echo  CONFIG SERVER EM EXECUCAO
echo ===============================================
echo.
echo 📊 URLs Importantes:
echo    Configuracoes: http://localhost:8888/{service}/{profile}
echo    Health Check: http://localhost:8888/actuator/health
echo    Refresh: http://localhost:8888/actuator/refresh
echo.
echo ⚙️ Servicos Configurados:
echo    ✅ API Gateway (api-gateway)
echo    ✅ Financial Service (financial-service)
echo    ✅ Company Service (company-service)
echo    ✅ Biometria Service (biometria-service)
echo    ✅ Configuracoes Globais (application)
echo.
echo 🔄 Exemplos de Uso:
echo    http://localhost:8888/api-gateway/default
echo    http://localhost:8888/financial-service/local
echo    http://localhost:8888/application/default
echo.
echo Pressione qualquer tecla para voltar...
pause >nul