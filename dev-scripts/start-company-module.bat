@echo off
echo.
echo ========================================
echo  INICIANDO COMPANY MODULE - MyERP
echo ========================================
echo.

REM Verificar se PostgreSQL está rodando
echo 🔍 Verificando PostgreSQL...
pg_isready -h localhost -p 5432 >nul 2>&1
if errorlevel 1 (
    echo ❌ PostgreSQL não está rodando
    echo Execute: dev-scripts\start-postgres.bat
    pause
    exit /b 1
)

REM Verificar se Eureka está rodando
echo 🔍 Verificando Eureka Server...
curl -s http://localhost:8761/actuator/health >nul 2>&1
if errorlevel 1 (
    echo ⚠️ Eureka Server não está rodando
    echo O módulo funcionará, mas sem service discovery
)

echo.
echo 🚀 Iniciando Company Module...
echo 📍 Porta: 8085
echo 🗄️ Database: myerp_company
echo 🌐 URL: http://localhost:8085
echo.

cd modules\company-module

REM Criar banco se não existir
echo 🗄️ Verificando banco de dados...
createdb -h localhost -U myerp_user myerp_company >nul 2>&1

REM Iniciar aplicação
echo ✅ Iniciando aplicação...
mvn spring-boot:run -Dspring-boot.run.profiles=local

echo.
echo ✅ Company Module iniciado com sucesso!
echo.
echo 🧪 Para testar:
echo curl http://localhost:8085/actuator/health
echo curl http://localhost:8085/api/company/empresas
echo.
echo 📊 Collection Postman: company-module.postman_collection.json
echo.
pause