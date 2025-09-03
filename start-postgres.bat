@echo off
echo Iniciando PostgreSQL para MyERP...
echo.

echo Verificando se Docker esta rodando...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Docker nao encontrado. Instale o Docker primeiro.
    pause
    exit /b 1
)

echo Parando containers existentes...
docker-compose down

echo Iniciando PostgreSQL e pgAdmin...
docker-compose up -d postgres pgadmin

echo.
echo ========================================
echo PostgreSQL iniciado com sucesso!
echo ========================================
echo.
echo Conexoes disponiveis:
echo - PostgreSQL: localhost:5432
echo   - Database: myerp_db
echo   - Usuario: myerp_user
echo   - Senha: myerp_pass
echo.
echo - pgAdmin: http://localhost:5050
echo   - Email: admin@myerp.com
echo   - Senha: admin123
echo.
echo Aguardando inicializacao...
timeout /t 10 /nobreak >nul

echo Verificando status dos containers...
docker-compose ps

echo.
echo Pronto para usar!
pause