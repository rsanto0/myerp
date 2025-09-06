-- Script para criar todos os bancos MyERP
-- Execute: docker exec -i myerp-postgres psql -U myerp_user -d postgres -f /tmp/create-databases.sql

-- Criar bancos para módulos ativos
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

-- Listar bancos criados
\l