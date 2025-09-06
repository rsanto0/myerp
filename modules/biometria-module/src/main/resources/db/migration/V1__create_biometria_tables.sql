-- Criação das tabelas do módulo biometria

-- Tabela de configuração de câmeras
CREATE TABLE IF NOT EXISTS configuracao_camera (
    id BIGSERIAL PRIMARY KEY,
    nome_configuracao VARCHAR(255) NOT NULL,
    alias VARCHAR(100) NOT NULL UNIQUE,
    marca_modelo VARCHAR(255),
    tipo_camera VARCHAR(50) NOT NULL CHECK (tipo_camera IN ('USB', 'IP', 'SERIAL', 'SDK')),
    resolucao VARCHAR(50),
    sistema_operacional VARCHAR(100),
    
    -- Campos para câmera USB
    indice_usb INTEGER,
    vid_pid VARCHAR(50),
    
    -- Campos para câmera IP
    endereco_ip VARCHAR(255),
    porta INTEGER,
    protocolo VARCHAR(50),
    usuario VARCHAR(100),
    senha VARCHAR(255),
    url_stream VARCHAR(500),
    
    -- Configurações gerais
    fps INTEGER DEFAULT 30,
    qualidade_jpeg INTEGER DEFAULT 80,
    timeout_conexao INTEGER DEFAULT 5000,
    
    -- Status
    ativa BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    ultimo_teste TIMESTAMP,
    status_ultimo_teste VARCHAR(50)
);

-- Tabela de biometria de usuários
CREATE TABLE IF NOT EXISTS biometria_usuario (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    
    -- Tipos de biometria
    tipo_biometria VARCHAR(50) NOT NULL CHECK (tipo_biometria IN ('FACIAL', 'DIGITAL', 'IRIS', 'VOICE')),
    
    -- Dados biométricos (Base64)
    dados_biometricos TEXT NOT NULL,
    
    -- Metadados
    qualidade DECIMAL(5,2),
    confianca DECIMAL(5,2),
    algoritmo VARCHAR(100),
    versao_algoritmo VARCHAR(50),
    
    -- Status
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    deletada BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Auditoria
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    data_exclusao TIMESTAMP,
    
    -- Índices
    CONSTRAINT uk_usuario_tipo UNIQUE (usuario_id, tipo_biometria)
);

-- Tabela de eventos de detecção biométrica
CREATE TABLE IF NOT EXISTS evento_deteccao_biometrica (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT,
    nome_funcionario VARCHAR(255) NOT NULL,
    
    -- Detecção
    tipo_movimento VARCHAR(20) NOT NULL CHECK (tipo_movimento IN ('ENTRADA', 'SAIDA', 'SAIDA_ALMOCO', 'RETORNO_ALMOCO')),
    data_hora_deteccao TIMESTAMP NOT NULL,
    confianca_deteccao DECIMAL(5,2),
    
    -- Processamento
    dentro_tolerancia BOOLEAN NOT NULL DEFAULT FALSE,
    ponto_registrado_automaticamente BOOLEAN NOT NULL DEFAULT FALSE,
    sugestao_criada_para_rh BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Status
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'PROCESSADO', 'APROVADO', 'REJEITADO')),
    
    -- Observações
    observacoes TEXT,
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_processamento TIMESTAMP
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_configuracao_camera_ativa ON configuracao_camera(ativa);
CREATE INDEX IF NOT EXISTS idx_configuracao_camera_tipo ON configuracao_camera(tipo_camera);
CREATE INDEX IF NOT EXISTS idx_configuracao_camera_alias ON configuracao_camera(alias);

CREATE INDEX IF NOT EXISTS idx_biometria_usuario_id ON biometria_usuario(usuario_id);
CREATE INDEX IF NOT EXISTS idx_biometria_tipo ON biometria_usuario(tipo_biometria);
CREATE INDEX IF NOT EXISTS idx_biometria_ativa ON biometria_usuario(ativa);

CREATE INDEX IF NOT EXISTS idx_evento_usuario_id ON evento_deteccao_biometrica(usuario_id);
CREATE INDEX IF NOT EXISTS idx_evento_data_deteccao ON evento_deteccao_biometrica(data_hora_deteccao);
CREATE INDEX IF NOT EXISTS idx_evento_status ON evento_deteccao_biometrica(status);
CREATE INDEX IF NOT EXISTS idx_evento_tipo_movimento ON evento_deteccao_biometrica(tipo_movimento);

-- Comentários nas tabelas
COMMENT ON TABLE configuracao_camera IS 'Configurações de câmeras para captura biométrica';
COMMENT ON TABLE biometria_usuario IS 'Dados biométricos dos usuários do sistema';
COMMENT ON TABLE evento_deteccao_biometrica IS 'Eventos de detecção biométrica para controle de ponto';

-- Comentários em campos importantes
COMMENT ON COLUMN configuracao_camera.alias IS 'Identificador único amigável (ex: camera_entrada, camera_saida)';
COMMENT ON COLUMN biometria_usuario.dados_biometricos IS 'Dados biométricos codificados em Base64';
COMMENT ON COLUMN evento_deteccao_biometrica.dentro_tolerancia IS 'Se a detecção ocorreu dentro da tolerância de horário';