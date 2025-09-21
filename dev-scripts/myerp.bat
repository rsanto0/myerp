@echo off
setlocal

echo ========================================
echo        MyERP MANAGER v1.0
echo ========================================
echo.
echo 1) Iniciar PostgreSQL
echo 2) Iniciar Sistema Completo
echo 3) Parar Sistema
echo 4) Status dos Servicos
echo 5) Sair
echo.
set /p choice="Escolha uma opcao (1-5): "

if "%choice%"=="1" goto postgres
if "%choice%"=="2" goto start_all
if "%choice%"=="3" goto stop_all
if "%choice%"=="4" goto status
if "%choice%"=="5" goto end

:postgres
echo Iniciando PostgreSQL...
net start postgresql-x64-15 >nul 2>&1 || docker run -d --name myerp-postgres -e POSTGRES_DB=myerp -e POSTGRES_USER=myerp_user -e POSTGRES_PASSWORD=myerp_pass -p 5432:5432 postgres:15 >nul 2>&1
echo PostgreSQL iniciado!
goto end

:start_all
echo Iniciando sistema completo...
call core\start-myerp.bat
goto end

:stop_all
echo Parando sistema...
call core\stop-myerp.bat
goto end

:status
echo Status dos servicos:
netstat -an | find ":5432" >nul && echo PostgreSQL: RODANDO || echo PostgreSQL: PARADO
netstat -an | find ":8761" >nul && echo Eureka: RODANDO || echo Eureka: PARADO
netstat -an | find ":8080" >nul && echo Gateway: RODANDO || echo Gateway: PARADO
netstat -an | find ":8081" >nul && echo Auth: RODANDO || echo Auth: PARADO
goto end

:end
pause