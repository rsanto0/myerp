@echo off
echo ========================================
echo    CONFIGURAR POSTMAN ENVIRONMENT
echo ========================================
echo.

echo 🔑 Configure suas credenciais Postman:
echo.

set /p POSTMAN_API_KEY="Digite sua POSTMAN_API_KEY: "
if "%POSTMAN_API_KEY%"=="" (
    echo ❌ API Key é obrigatória!
    pause
    exit /b 1
)

set /p COLLECTION_UID="Digite o COLLECTION_UID [myerp-system-collection]: "
if "%COLLECTION_UID%"=="" set COLLECTION_UID=myerp-system-collection

echo.
echo ✅ Configurações:
echo API Key: %POSTMAN_API_KEY:~0,10%...
echo Collection UID: %COLLECTION_UID%

echo.
echo 💾 Salvando configurações...

REM Criar arquivo de configuração
echo set POSTMAN_API_KEY=%POSTMAN_API_KEY% > postman-config.bat
echo set COLLECTION_UID=%COLLECTION_UID% >> postman-config.bat

echo.
echo ✅ Configurações salvas em: postman-config.bat
echo.
echo 🚀 Agora execute:
echo   call postman-config.bat
echo   sync-postman-cloud.bat
echo.
pause