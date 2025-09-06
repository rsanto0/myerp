@echo off
echo ========================================
echo    CORRIGIR AUTENTICAÇÃO GITHUB
echo ========================================
echo.

echo 🔍 Verificando configuração atual...
git config --list | findstr user

echo.
echo 🔧 OPÇÕES PARA CORRIGIR:
echo.
echo [1] Reconfigurar credenciais
echo [2] Usar Personal Access Token
echo [3] Verificar remote URL
echo [4] Limpar cache de credenciais
echo.

set /p opcao="Escolha uma opção [1-4]: "

if "%opcao%"=="1" goto :config_user
if "%opcao%"=="2" goto :access_token
if "%opcao%"=="3" goto :check_remote
if "%opcao%"=="4" goto :clear_cache

:config_user
echo.
echo 👤 Configurando usuário Git...
set /p username="Digite seu username GitHub: "
set /p email="Digite seu email GitHub: "

git config --global user.name "%username%"
git config --global user.email "%email%"
git config --global credential.helper manager-core

echo ✅ Configuração atualizada!
goto :end

:access_token
echo.
echo 🔑 PERSONAL ACCESS TOKEN:
echo.
echo 1. Acesse: https://github.com/settings/tokens
echo 2. Generate new token (classic)
echo 3. Selecione: repo, workflow, write:packages
echo 4. Copie o token gerado
echo 5. Use como senha no próximo push
echo.
echo 💡 Username: seu-username-github
echo 💡 Password: ghp_xxxxxxxxxxxxxxxxxxxx (token)
goto :end

:check_remote
echo.
echo 🔍 Verificando remote URL...
git remote -v
echo.
echo 💡 Se usar HTTPS: git remote set-url origin https://github.com/usuario/repo.git
echo 💡 Se usar SSH: git remote set-url origin git@github.com:usuario/repo.git
goto :end

:clear_cache
echo.
echo 🧹 Limpando cache de credenciais...
git config --global --unset credential.helper
git config --global credential.helper manager-core
cmdkey /list | findstr git
echo.
echo ✅ Cache limpo! Próximo push pedirá credenciais novamente.
goto :end

:end
echo.
echo 🚀 Teste agora:
echo git add .
echo git commit -m "test auth"
echo git push origin main
echo.
pause