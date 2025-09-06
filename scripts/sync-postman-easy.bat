@echo off
echo ========================================
echo    SYNC POSTMAN - MODO FÁCIL
echo ========================================
echo.

REM Verificar se arquivo de config existe
if exist postman-config.bat (
    echo ✅ Carregando configurações salvas...
    call postman-config.bat
) else (
    echo 🔑 Primeira execução - configure suas credenciais:
    echo.
    
    set /p POSTMAN_API_KEY="Digite sua POSTMAN_API_KEY: "
    if "!POSTMAN_API_KEY!"=="" (
        echo ❌ API Key é obrigatória!
        pause
        exit /b 1
    )
    
    set /p COLLECTION_UID="Digite o COLLECTION_UID [myerp-system-collection]: "
    if "!COLLECTION_UID!"=="" set COLLECTION_UID=myerp-system-collection
    
    REM Salvar para próximas execuções
    echo set POSTMAN_API_KEY=!POSTMAN_API_KEY! > postman-config.bat
    echo set COLLECTION_UID=!COLLECTION_UID! >> postman-config.bat
    
    echo ✅ Configurações salvas!
)

echo.
echo 📤 Sincronizando com Postman Cloud...
echo API Key: %POSTMAN_API_KEY:~0,10%...
echo Collection: %COLLECTION_UID%

echo.
echo 📤 Enviando collection...

curl -X PUT ^
    "https://api.getpostman.com/collections/%COLLECTION_UID%" ^
    -H "X-API-Key: %POSTMAN_API_KEY%" ^
    -H "Content-Type: application/json" ^
    -d @../postman/MyERP-System.postman_collection.json

if %errorlevel% neq 0 (
    echo ❌ Falha no upload da collection!
    pause
    exit /b 1
)

echo.
echo ✅ Sincronização concluída!
echo 🌐 Acesse: https://app.postman.com/
echo.
pause