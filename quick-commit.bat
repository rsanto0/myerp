@echo off
echo ========================================
echo    COMMIT RÁPIDO - MyERP
echo ========================================
echo.

echo 📝 Fazendo commit das alterações...

git add .

set /p message="Digite a mensagem do commit [Update MyERP]: "
if "%message%"=="" set message=Update MyERP

git commit -m "%message%"

echo.
echo 🚀 Fazendo push...
git push origin main

if %errorlevel% neq 0 (
    echo.
    echo ❌ Erro no push! Possíveis soluções:
    echo.
    echo 1. Execute: scripts\fix-github-auth.bat
    echo 2. Ou configure Personal Access Token
    echo 3. Ou use GitHub Desktop
    echo.
    pause
    exit /b 1
)

echo.
echo ✅ Commit realizado com sucesso!
echo 🌐 Verifique: https://github.com/seu-usuario/seu-repo
echo.
pause