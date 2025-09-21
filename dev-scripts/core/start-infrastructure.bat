@echo off
echo ========================================
echo 🏗️  MyERP - Iniciando Infraestrutura
echo ========================================
echo.

REM Definir cores
set GREEN=[92m
set YELLOW=[93m
set RED=[91m
set BLUE=[94m
set RESET=[0m

echo %BLUE%📋 Componentes da Infraestrutura:%RESET%
echo   - PostgreSQL (Banco de dados)
echo   - Eureka Server (Service Discovery)
echo   - Config Server (Configurações)
echo   - API Gateway (Roteamento)
echo.

REM Verificar se PostgreSQL está rodando
echo %YELLOW%🔍 Verificando PostgreSQL...%RESET%
netstat -an | find "5432" >nul
if %errorlevel% neq 0 (
    echo %RED%❌ PostgreSQL não está rodando!%RESET%
    echo %YELLOW%🚀 Iniciando PostgreSQL...%RESET%
    call "%~dp0..\database\criar-container-postgres-docker.bat"
    if %errorlevel% neq 0 (
        echo %RED%❌ Falha ao iniciar PostgreSQL%RESET%
        pause
        exit /b 1
    )
) else (
    echo %GREEN%✅ PostgreSQL já está rodando%RESET%
)

echo.
echo %YELLOW%🚀 Iniciando serviços de infraestrutura...%RESET%
echo.

REM Iniciar Eureka Server
echo %BLUE%📋 1/3 - Iniciando Eureka Server (Service Discovery)...%RESET%
cd /d "%~dp0..\..\infrastructure\service-discovery"
start "Eureka Server" cmd /k "mvn spring-boot:run -Dspring.profiles.active=local"

REM Aguardar Eureka inicializar
echo %YELLOW%⏳ Aguardando Eureka Server inicializar (30s)...%RESET%
timeout /t 30 /nobreak >nul

REM Iniciar Config Server
echo %BLUE%⚙️  2/3 - Iniciando Config Server...%RESET%
cd /d "%~dp0..\..\infrastructure\config-server"
start "Config Server" cmd /k "mvn spring-boot:run -Dspring.profiles.active=local"

REM Aguardar Config Server inicializar
echo %YELLOW%⏳ Aguardando Config Server inicializar (20s)...%RESET%
timeout /t 20 /nobreak >nul

REM Iniciar API Gateway
echo %BLUE%🚪 3/3 - Iniciando API Gateway...%RESET%
cd /d "%~dp0..\..\infrastructure\api-gateway"
start "API Gateway" cmd /k "mvn spring-boot:run -Dspring.profiles.active=local"

echo.
echo %GREEN%✅ Infraestrutura iniciada com sucesso!%RESET%
echo.
echo %BLUE%📊 URLs de Acesso:%RESET%
echo   - Eureka Server: http://localhost:8761
echo   - Config Server: http://localhost:8888
echo   - API Gateway:   http://localhost:8080
echo   - PostgreSQL:    localhost:5432
echo.
echo %YELLOW%⏳ Aguardando todos os serviços ficarem prontos (30s)...%RESET%
timeout /t 30 /nobreak >nul

echo.
echo %GREEN%🎉 Infraestrutura pronta para desenvolvimento!%RESET%
echo %BLUE%💡 Agora você pode iniciar qualquer módulo individualmente%RESET%
echo.
pause