@echo off
echo ========================================
echo  MyERP - Docker Installation
echo ========================================
echo.

echo Escolha o metodo de instalacao:
echo 1. Download Docker Desktop (Recomendado)
echo 2. Instalar via Chocolatey
echo 3. Instalar via Winget
echo.

set /p choice="Escolha uma opcao (1-3): "

if "%choice%"=="1" goto download
if "%choice%"=="2" goto chocolatey  
if "%choice%"=="3" goto winget
goto end

:download
echo Abrindo pagina de download do Docker Desktop...
start https://www.docker.com/products/docker-desktop/
echo.
echo Apos baixar:
echo 1. Execute o instalador
echo 2. Reinicie o computador se solicitado
echo 3. Abra Docker Desktop
echo 4. Execute: docker --version
goto end

:chocolatey
echo Verificando se Chocolatey esta instalado...
choco --version >nul 2>&1
if errorlevel 1 (
    echo Chocolatey nao encontrado. Instalando...
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
)
echo Instalando Docker Desktop via Chocolatey...
choco install docker-desktop -y
goto success

:winget
echo Instalando Docker Desktop via Winget...
winget install Docker.DockerDesktop
goto success

:success
echo.
echo ========================================
echo  Docker instalado com sucesso!
echo ========================================
echo.
echo Proximos passos:
echo 1. Reinicie o computador se necessario
echo 2. Abra Docker Desktop
echo 3. Teste: docker --version
echo 4. Execute PostgreSQL: docker run --name myerp-postgres -e POSTGRES_DB=myerp -e POSTGRES_USER=myerp -e POSTGRES_PASSWORD=myerp123 -p 5432:5432 -d postgres:15
echo.

:end
pause