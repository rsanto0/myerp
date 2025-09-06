@echo off
echo ========================================
echo    SYNC POSTMAN CLOUD
echo ========================================
echo.

REM Verificar variáveis de ambiente
if "%POSTMAN_API_KEY%"=="" (
    echo ❌ POSTMAN_API_KEY não definido!
    echo.
    echo Configure:
    echo set POSTMAN_API_KEY=sua-api-key-aqui
    pause
    exit /b 1
)

if "%COLLECTION_UID%"=="" (
    echo ❌ COLLECTION_UID não definido!
    echo.
    echo Configure:
    echo set COLLECTION_UID=myerp-system-collection
    pause
    exit /b 1
)

echo ✅ Variáveis configuradas
echo API Key: %POSTMAN_API_KEY:~0,10%...
echo Collection UID: %COLLECTION_UID%

echo.
echo 📤 Enviando collection para Postman Cloud...

curl -X PUT ^
    "https://api.getpostman.com/collections/%COLLECTION_UID%" ^
    -H "X-API-Key: %POSTMAN_API_KEY%" ^
    -H "Content-Type: application/json" ^
    -d @../postman/MyERP-System.postman_collection.json

if %errorlevel% neq 0 (
    echo ❌ Falha no upload!
    pause
    exit /b 1
)

echo.
echo 📤 Enviando environment para Postman Cloud...

curl -X PUT ^
    "https://api.getpostman.com/environments/myerp-environment" ^
    -H "X-API-Key: %POSTMAN_API_KEY%" ^
    -H "Content-Type: application/json" ^
    -d @../postman/MyERP-Environment.postman_environment.json

echo.
echo ✅ Sincronização com Postman Cloud concluída!
echo 🌐 Acesse: https://app.postman.com/
echo.
pause