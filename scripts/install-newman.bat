@echo off
echo ========================================
echo    INSTALANDO NEWMAN CLI
echo ========================================
echo.

REM Verificar se Node.js está instalado
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Node.js não instalado!
    echo.
    echo Baixe em: https://nodejs.org/
    echo Após instalar, execute este script novamente
    pause
    exit /b 1
)

echo ✅ Node.js encontrado
node --version

echo.
echo 📦 Instalando Newman globalmente...
npm install -g newman

echo.
echo 📦 Instalando Newman HTML Reporter...
npm install -g newman-reporter-html

echo.
echo ✅ Verificando instalação...
newman --version

echo.
echo 🎉 Newman instalado com sucesso!
echo.
echo 💡 Agora você pode:
echo   - Executar collections: newman run collection.json
echo   - Gerar relatórios HTML: newman run collection.json -r html
echo   - Sincronizar com GitHub via scripts
echo.
pause