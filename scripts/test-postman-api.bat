@echo off
echo ========================================
echo    TESTAR POSTMAN API KEY
echo ========================================
echo.

if "%POSTMAN_API_KEY%"=="" (
    echo ❌ POSTMAN_API_KEY não definido!
    set /p POSTMAN_API_KEY="Digite sua API Key: "
)

echo 🔍 Testando API Key: %POSTMAN_API_KEY:~0,15%...
echo.

curl -X GET ^
    "https://api.getpostman.com/me" ^
    -H "X-API-Key: %POSTMAN_API_KEY%"

echo.
echo.
echo 📋 Se retornou dados do usuário = API Key válida
echo 📋 Se retornou erro = API Key inválida
echo.
pause