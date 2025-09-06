-- Adicionar tabelas de contas bancárias ao Company Module

-- Tabela principal de contas bancárias
CREATE TABLE IF NOT EXISTS conta_bancaria (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    
    -- Dados básicos da conta
    nome_conta VARCHAR(100) NOT NULL,
    banco VARCHAR(50) NOT NULL,
    agencia VARCHAR(10) NOT NULL,
    conta VARCHAR(20) NOT NULL,
    digito_verificador VARCHAR(2),
    tipo_conta VARCHAR(20) NOT NULL CHECK (tipo_conta IN ('CORRENTE', 'POUPANCA', 'PAGAMENTO', 'SALARIO', 'INVESTIMENTO')),
    
    -- Dados do titular
    titular VARCHAR(255),
    cpf_cnpj_titular VARCHAR(18),
    
    -- Saldos e limites
    saldo_atual DECIMAL(15,2) DEFAULT 0.00,
    limite_credito DECIMAL(15,2) DEFAULT 0.00,
    
    -- Configurações de integração API
    client_id VARCHAR(255),
    client_secret TEXT, -- Será criptografado
    certificado_digital TEXT, -- Base64
    chave_privada TEXT, -- Base64 criptografado
    ambiente VARCHAR(20) DEFAULT 'SANDBOX' CHECK (ambiente IN ('SANDBOX', 'PRODUCAO')),
    
    -- Funcionalidades habilitadas
    ativa_boletos BOOLEAN NOT NULL DEFAULT FALSE,
    ativa_pix BOOLEAN NOT NULL DEFAULT FALSE,
    ativa_ted_doc BOOLEAN NOT NULL DEFAULT FALSE,
    ativa_conciliacao BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Status e controle
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    conta_principal BOOLEAN NOT NULL DEFAULT FALSE,
    observacoes TEXT,
    
    -- Auditoria
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    ultima_sincronizacao TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uk_conta_empresa_agencia_conta UNIQUE (empresa_id, agencia, conta),
    CONSTRAINT uk_conta_principal_por_empresa UNIQUE (empresa_id, conta_principal) DEFERRABLE INITIALLY DEFERRED
);

-- Tabela de configurações específicas do banco
CREATE TABLE IF NOT EXISTS conta_bancaria_configuracao (
    conta_id BIGINT NOT NULL REFERENCES conta_bancaria(id) ON DELETE CASCADE,
    chave VARCHAR(100) NOT NULL,
    valor TEXT,
    PRIMARY KEY (conta_id, chave)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_conta_bancaria_empresa_id ON conta_bancaria(empresa_id);
CREATE INDEX IF NOT EXISTS idx_conta_bancaria_banco ON conta_bancaria(banco);
CREATE INDEX IF NOT EXISTS idx_conta_bancaria_ativa ON conta_bancaria(ativa);
CREATE INDEX IF NOT EXISTS idx_conta_bancaria_principal ON conta_bancaria(conta_principal);
CREATE INDEX IF NOT EXISTS idx_conta_bancaria_boletos ON conta_bancaria(ativa_boletos);
CREATE INDEX IF NOT EXISTS idx_conta_bancaria_pix ON conta_bancaria(ativa_pix);
CREATE INDEX IF NOT EXISTS idx_conta_bancaria_ambiente ON conta_bancaria(ambiente);

CREATE INDEX IF NOT EXISTS idx_conta_config_conta_id ON conta_bancaria_configuracao(conta_id);

-- Comentários nas tabelas
COMMENT ON TABLE conta_bancaria IS 'Contas bancárias das empresas para integração financeira';
COMMENT ON TABLE conta_bancaria_configuracao IS 'Configurações específicas por banco/conta';

-- Comentários em campos importantes
COMMENT ON COLUMN conta_bancaria.banco IS 'Enum BancoBrasil (BANCO_DO_BRASIL, ITAU, etc)';
COMMENT ON COLUMN conta_bancaria.client_secret IS 'Client Secret da API do banco (criptografado)';
COMMENT ON COLUMN conta_bancaria.certificado_digital IS 'Certificado digital em Base64 para APIs que exigem';
COMMENT ON COLUMN conta_bancaria.ambiente IS 'SANDBOX para testes, PRODUCAO para uso real';
COMMENT ON COLUMN conta_bancaria.conta_principal IS 'Apenas uma conta principal por empresa';
COMMENT ON COLUMN conta_bancaria.ativa_boletos IS 'Se a conta está habilitada para emissão de boletos';
COMMENT ON COLUMN conta_bancaria.ativa_pix IS 'Se a conta está habilitada para PIX';

-- Dados de exemplo para as empresas existentes
-- Conta para Padaria do João (empresa_id = 1)
INSERT INTO conta_bancaria (
    empresa_id, nome_conta, banco, agencia, conta, digito_verificador, tipo_conta,
    titular, ativa_boletos, conta_principal, ambiente
) VALUES (
    1, 'Conta Principal', 'CAIXA', '1234', '12345', '6', 'CORRENTE',
    'Padaria do João Ltda', true, true, 'SANDBOX'
) ON CONFLICT (empresa_id, agencia, conta) DO NOTHING;

-- Conta para Oficina do Pedro (empresa_id = 2)
INSERT INTO conta_bancaria (
    empresa_id, nome_conta, banco, agencia, conta, digito_verificador, tipo_conta,
    titular, ativa_boletos, ativa_pix, conta_principal, ambiente
) VALUES (
    2, 'Conta Movimento', 'BANCO_DO_BRASIL', '5678', '67890', '1', 'CORRENTE',
    'Oficina Pedro Auto Ltda', true, true, true, 'SANDBOX'
) ON CONFLICT (empresa_id, agencia, conta) DO NOTHING;

-- Conta para Consultoria da Ana (empresa_id = 3)
INSERT INTO conta_bancaria (
    empresa_id, nome_conta, banco, agencia, conta, digito_verificador, tipo_conta,
    titular, ativa_boletos, ativa_pix, ativa_ted_doc, conta_principal, ambiente
) VALUES (
    3, 'Conta Empresarial', 'ITAU', '9012', '11111', '2', 'CORRENTE',
    'Ana Consultoria Empresarial S/A', true, true, true, true, 'PRODUCAO'
) ON CONFLICT (empresa_id, agencia, conta) DO NOTHING;

-- Configurações de exemplo
INSERT INTO conta_bancaria_configuracao (conta_id, chave, valor)
SELECT c.id, 'CONVENIO_BOLETO', '123456'
FROM conta_bancaria c WHERE c.empresa_id = 1
ON CONFLICT (conta_id, chave) DO NOTHING;

INSERT INTO conta_bancaria_configuracao (conta_id, chave, valor)
SELECT c.id, 'CARTEIRA_BOLETO', '18'
FROM conta_bancaria c WHERE c.empresa_id = 1
ON CONFLICT (conta_id, chave) DO NOTHING;

INSERT INTO conta_bancaria_configuracao (conta_id, chave, valor)
SELECT c.id, 'CONVENIO_BOLETO', '789012'
FROM conta_bancaria c WHERE c.empresa_id = 2
ON CONFLICT (conta_id, chave) DO NOTHING;

INSERT INTO conta_bancaria_configuracao (conta_id, chave, valor)
SELECT c.id, 'CHAVE_PIX', 'pedro@oficinapedro.com.br'
FROM conta_bancaria c WHERE c.empresa_id = 2
ON CONFLICT (conta_id, chave) DO NOTHING;