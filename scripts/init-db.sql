-- Script de inicialização do banco MyERP
-- Cria databases separados para cada módulo

-- Databases para todos os módulos MyERP
CREATE DATABASE myerp_auth;
CREATE DATABASE myerp_rh;
CREATE DATABASE myerp_biometria;
CREATE DATABASE myerp_company;
CREATE DATABASE myerp_financial;
CREATE DATABASE myerp_monitoring;

-- Databases para módulos futuros
CREATE DATABASE myerp_vendas;
CREATE DATABASE myerp_estoque;
CREATE DATABASE myerp_compras;

-- Criar usuário específico para cada serviço (opcional)
CREATE USER auth_user WITH PASSWORD 'auth_pass';
CREATE USER rh_user WITH PASSWORD 'rh_pass';
CREATE USER biometria_user WITH PASSWORD 'biometria_pass';

-- Conceder permissões ao usuário principal
GRANT ALL PRIVILEGES ON DATABASE myerp_auth TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_rh TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_biometria TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_company TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_financial TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_monitoring TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_vendas TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_estoque TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_compras TO myerp_user;

-- Conceder permissões aos usuários específicos
GRANT ALL PRIVILEGES ON DATABASE myerp_auth TO auth_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_rh TO rh_user;
GRANT ALL PRIVILEGES ON DATABASE myerp_biometria TO biometria_user;