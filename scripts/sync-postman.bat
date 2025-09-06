@echo off
REM Script para sincronizar collections Postman com GitHub

echo ========================================
echo    POSTMAN + GITHUB SYNC
echo ========================================
echo.

REM Verificar se Newman está instalado
newman --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Newman não instalado!
    echo.
    echo Instale com: npm install -g newman
    pause
    exit /b 1
)

REM Validar collections
echo 🧪 Validando collections...
newman run postman/MyERP-System.postman_collection.json ^
    -e postman/MyERP-Environment.postman_environment.json ^
    --reporters cli,html ^
    --reporter-html-export reports/sync-validation.html

if %errorlevel% neq 0 (
    echo ❌ Collection inválida!
    echo 📊 Veja o relatório: reports/sync-validation.html
    pause
    exit /b 1
)

REM Commit automático
echo 📤 Fazendo commit das collections...
git add postman/
git commit -m "chore: Atualizar collections Postman"

REM Push para GitHub
echo 🚀 Enviando para GitHub...
git push origin main

echo.
echo ✅ Sincronização concluída!
echo 📡 GitHub Actions irá sincronizar com Postman Cloud
echo.
pause