@echo off
echo ========================================
echo    TESTE LOCAL POSTMAN COLLECTIONS
echo ========================================
echo.

REM Verificar se Newman está instalado
newman --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Newman não instalado!
    echo Execute: scripts\install-newman.bat
    pause
    exit /b 1
)

echo ✅ Newman encontrado
newman --version

echo.
echo 🧪 Testando Collection Principal...
newman run postman/MyERP-System.postman_collection.json ^
    -e postman/MyERP-Environment.postman_environment.json ^
    --reporters cli,html ^
    --reporter-html-export reports/myerp-test-report.html

if %errorlevel% neq 0 (
    echo ❌ Testes falharam!
    pause
    exit /b 1
)

echo.
echo 🧪 Testando Collection de Testes...
newman run postman/MyERP-Tests.postman_collection.json ^
    -e postman/MyERP-Environment.postman_environment.json ^
    --reporters cli,html ^
    --reporter-html-export reports/myerp-automated-tests.html

echo.
echo ✅ Testes concluídos!
echo 📊 Relatórios gerados em: reports/
echo.
pause