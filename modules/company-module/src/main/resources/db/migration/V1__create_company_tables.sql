-- Criação das tabelas do módulo company

-- Tabela principal de empresas
CREATE TABLE IF NOT EXISTS empresa (
    id BIGSERIAL PRIMARY KEY,
    
    -- Identificação
    razao_social VARCHAR(255) NOT NULL,
    nome_fantasia VARCHAR(255),
    cnpj VARCHAR(18) UNIQUE,
    inscricao_estadual VARCHAR(20),
    inscricao_municipal VARCHAR(20),
    
    -- Endereço
    endereco VARCHAR(500),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    
    -- Contato
    telefone VARCHAR(20),
    email VARCHAR(100),
    website VARCHAR(255),
    
    -- Configurações
    logo TEXT,
    timezone VARCHAR(50) DEFAULT 'America/Sao_Paulo',
    moeda VARCHAR(3) DEFAULT 'BRL',
    
    -- Multi-tenant
    tipo_plano VARCHAR(20) NOT NULL DEFAULT 'BASIC' CHECK (tipo_plano IN ('BASIC', 'STANDARD', 'PREMIUM', 'ENTERPRISE')),
    instancia_dedicada BOOLEAN DEFAULT FALSE,
    grupo_manutencao VARCHAR(1) DEFAULT 'C' CHECK (grupo_manutencao IN ('A', 'B', 'C')),
    versao_sistema VARCHAR(10) DEFAULT '1.0.0',
    
    -- Status
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    data_fundacao TIMESTAMP,
    data_vencimento_plano TIMESTAMP,
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP
);

-- Tabela de configurações da empresa
CREATE TABLE IF NOT EXISTS empresa_configuracao (
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    chave VARCHAR(100) NOT NULL,
    valor TEXT,
    PRIMARY KEY (empresa_id, chave)
);

-- Tabela de feature flags da empresa
CREATE TABLE IF NOT EXISTS empresa_feature_flag (
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    feature VARCHAR(100) NOT NULL,
    ativa BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (empresa_id, feature)
);

-- Tabela de departamentos
CREATE TABLE IF NOT EXISTS departamento (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    codigo VARCHAR(10),
    centro_custo VARCHAR(20),
    responsavel_id BIGINT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    
    -- Constraint única por empresa
    CONSTRAINT uk_departamento_empresa_nome UNIQUE (empresa_id, nome)
);

-- Tabela de cargos
CREATE TABLE IF NOT EXISTS cargo (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    nome VARCHAR(100) NOT NULL,
    tipo_cargo VARCHAR(50),
    descricao VARCHAR(1000),
    nivel_hierarquico INTEGER,
    salario_base DECIMAL(10,2),
    carga_horaria_semanal INTEGER DEFAULT 40,
    departamento_id BIGINT REFERENCES departamento(id),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    
    -- Constraint única por empresa
    CONSTRAINT uk_cargo_empresa_nome UNIQUE (empresa_id, nome)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_empresa_cnpj ON empresa(cnpj);
CREATE INDEX IF NOT EXISTS idx_empresa_ativa ON empresa(ativa);
CREATE INDEX IF NOT EXISTS idx_empresa_tipo_plano ON empresa(tipo_plano);
CREATE INDEX IF NOT EXISTS idx_empresa_grupo_manutencao ON empresa(grupo_manutencao);

CREATE INDEX IF NOT EXISTS idx_departamento_empresa_id ON departamento(empresa_id);
CREATE INDEX IF NOT EXISTS idx_departamento_ativo ON departamento(ativo);
CREATE INDEX IF NOT EXISTS idx_departamento_responsavel ON departamento(responsavel_id);

CREATE INDEX IF NOT EXISTS idx_cargo_empresa_id ON cargo(empresa_id);
CREATE INDEX IF NOT EXISTS idx_cargo_departamento_id ON cargo(departamento_id);
CREATE INDEX IF NOT EXISTS idx_cargo_ativo ON cargo(ativo);
CREATE INDEX IF NOT EXISTS idx_cargo_nivel_hierarquico ON cargo(nivel_hierarquico);

CREATE INDEX IF NOT EXISTS idx_empresa_config_empresa_id ON empresa_configuracao(empresa_id);
CREATE INDEX IF NOT EXISTS idx_empresa_feature_empresa_id ON empresa_feature_flag(empresa_id);

-- Comentários nas tabelas
COMMENT ON TABLE empresa IS 'Empresas do sistema multi-tenant';
COMMENT ON TABLE empresa_configuracao IS 'Configurações específicas por empresa';
COMMENT ON TABLE empresa_feature_flag IS 'Feature flags por empresa';
COMMENT ON TABLE departamento IS 'Departamentos das empresas';
COMMENT ON TABLE cargo IS 'Cargos disponíveis nas empresas';

-- Comentários em campos importantes
COMMENT ON COLUMN empresa.tipo_plano IS 'Plano da empresa (BASIC, STANDARD, PREMIUM, ENTERPRISE)';
COMMENT ON COLUMN empresa.grupo_manutencao IS 'Grupo de manutenção (A, B, C) para escalonamento';
COMMENT ON COLUMN empresa.instancia_dedicada IS 'Se a empresa tem instância dedicada (Premium/Enterprise)';
COMMENT ON COLUMN cargo.nivel_hierarquico IS 'Nível hierárquico do cargo (1=mais alto)';
COMMENT ON COLUMN departamento.centro_custo IS 'Centro de custo para controle financeiro';