@echo off
echo ========================================
echo    CRIANDO BANCOS MyERP - PostgreSQL
echo ========================================
echo.

echo [INFO] Conectando ao PostgreSQL e criando bancos...
echo.

docker exec -i myerp-postgres psql -U myerp_user -d postgres << EOF
-- Criar todos os bancos necessários
CREATE DATABASE myerp_auth;
CREATE DATABASE myerp_rh;
CREATE DATABASE myerp_biometria;
CREATE DATABASE myerp_company;
CREATE DATABASE myerp_financial;
CREATE DATABASE myerp_monitoring;

-- Bancos para módulos futuros
CREATE DATABASE myerp_vendas;
CREATE DATABASE myerp_estoque;
CREATE DATABASE myerp_compras;

-- Verificar bancos criados
\l
EOF

echo.
echo ========================================
echo   BANCOS CRIADOS COM SUCESSO!
echo ========================================
echo.
echo Bancos disponíveis:
echo - myerp_auth (Auth Service)
echo - myerp_rh (RH Module)
echo - myerp_biometria (Biometria Module)
echo - myerp_company (Company Module)
echo - myerp_financial (Financial Module)
echo - myerp_monitoring (Monitoring Module)
echo.
pause