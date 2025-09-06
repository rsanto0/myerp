@echo off
echo ========================================
echo    SYNC POSTMAN - DIRETO DA RAIZ
echo ========================================
echo.

REM Verificar variáveis
if "%POSTMAN_API_KEY%"=="" (
    echo ❌ POSTMAN_API_KEY não definido!
    echo Execute: set POSTMAN_API_KEY=sua-api-key
    pause
    exit /b 1
)

if "%COLLECTION_UID%"=="" (
    echo ❌ COLLECTION_UID não definido!
    echo Execute: set COLLECTION_UID=myerp-system-collection
    pause
    exit /b 1
)

echo ✅ Variáveis OK
echo API Key: %POSTMAN_API_KEY:~0,10%...
echo Collection: %COLLECTION_UID%

echo.
echo 📤 Enviando collection...

curl -X PUT ^
    "https://api.getpostman.com/collections/%COLLECTION_UID%" ^
    -H "X-API-Key: %POSTMAN_API_KEY%" ^
    -H "Content-Type: application/json" ^
    -d @postman/MyERP-System.postman_collection.json

if %errorlevel% neq 0 (
    echo ❌ Falha no upload!
    pause
    exit /b 1
)

echo.
echo ✅ Collection sincronizada!
echo 🌐 Acesse: https://app.postman.com/
echo.
pause