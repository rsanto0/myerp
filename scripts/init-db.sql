-- Script de inicialização do banco MyERP
-- Cria databases separados para cada módulo

-- Database para Auth Service
CREATE DATABASE auth_db;

-- Database para RH Module (Sistema Ponto)
CREATE DATABASE rh_db;

-- Database para Biometria Module
CREATE DATABASE biometria_db;

-- Database para outros módulos futuros
CREATE DATABASE financeiro_db;
CREATE DATABASE vendas_db;
CREATE DATABASE estoque_db;
CREATE DATABASE compras_db;

-- Criar usuário específico para cada serviço (opcional)
CREATE USER auth_user WITH PASSWORD 'auth_pass';
CREATE USER rh_user WITH PASSWORD 'rh_pass';
CREATE USER biometria_user WITH PASSWORD 'biometria_pass';

-- Conceder permissões
GRANT ALL PRIVILEGES ON DATABASE auth_db TO auth_user;
GRANT ALL PRIVILEGES ON DATABASE rh_db TO rh_user;
GRANT ALL PRIVILEGES ON DATABASE biometria_db TO biometria_user;

-- Conceder permissões ao usuário principal também
GRANT ALL PRIVILEGES ON DATABASE auth_db TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE rh_db TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE biometria_db TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE financeiro_db TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE vendas_db TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE estoque_db TO myerp_user;
GRANT ALL PRIVILEGES ON DATABASE compras_db TO myerp_user;