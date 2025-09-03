@echo off
echo ========================================
echo  MyERP - PostgreSQL Setup
echo ========================================
echo.

echo Opcoes de instalacao:
echo 1. Instalar PostgreSQL via Chocolatey
echo 2. Download manual PostgreSQL
echo 3. Usar Docker (se disponivel)
echo 4. Continuar com H2 (desenvolvimento)
echo.

set /p choice="Escolha uma opcao (1-4): "

if "%choice%"=="1" goto chocolatey
if "%choice%"=="2" goto manual
if "%choice%"=="3" goto docker
if "%choice%"=="4" goto h2
goto end

:chocolatey
echo Instalando PostgreSQL via Chocolatey...
choco install postgresql --params '/Password:myerp123'
echo Criando database...
createdb -U postgres myerp
psql -U postgres -c "CREATE USER myerp WITH PASSWORD 'myerp123';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE myerp TO myerp;"
goto success

:manual
echo Baixe PostgreSQL em: https://www.postgresql.org/download/windows/
echo Apos instalar, execute:
echo   createdb -U postgres myerp
echo   psql -U postgres -c "CREATE USER myerp WITH PASSWORD 'myerp123';"
echo   psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE myerp TO myerp;"
pause
goto end

:docker
echo Executando PostgreSQL via Docker...
docker run --name myerp-postgres -e POSTGRES_DB=myerp -e POSTGRES_USER=myerp -e POSTGRES_PASSWORD=myerp123 -p 5432:5432 -d postgres:15
goto success

:h2
echo Continuando com H2 para desenvolvimento...
echo Para usar PostgreSQL depois, execute com: --spring.profiles.active=postgres
goto end

:success
echo.
echo ========================================
echo  PostgreSQL configurado com sucesso!
echo ========================================
echo Database: myerp
echo Usuario: myerp  
echo Senha: myerp123
echo Porta: 5432
echo.
echo Para usar PostgreSQL, execute os modulos com:
echo mvn spring-boot:run -Dspring-boot.run.profiles=postgres
echo.

:end
pause