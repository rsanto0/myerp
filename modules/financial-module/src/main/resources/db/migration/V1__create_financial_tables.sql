-- Financial Module Database Schema

-- Tabela de Boletos
CREATE TABLE boletos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    conta_bancaria_id BIGINT NOT NULL,
    numero_documento VARCHAR(100) NOT NULL,
    linha_digitavel VARCHAR(47) UNIQUE NOT NULL,
    codigo_barras VARCHAR(44) UNIQUE NOT NULL,
    valor DECIMAL(15,2) NOT NULL CHECK (valor > 0),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    sacado_nome VARCHAR(200) NOT NULL,
    sacado_documento VARCHAR(18) NOT NULL,
    instrucoes VARCHAR(500),
    status VARCHAR(20) DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'PAGO', 'VENCIDO', 'CANCELADO', 'PROCESSANDO')),
    nosso_numero VARCHAR(20),
    valor_pago DECIMAL(15,2),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Transações PIX
CREATE TABLE transacoes_pix (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    conta_bancaria_id BIGINT NOT NULL,
    end_to_end_id VARCHAR(32) UNIQUE NOT NULL,
    txid VARCHAR(35) NOT NULL,
    valor DECIMAL(15,2) NOT NULL CHECK (valor > 0),
    chave_pix VARCHAR(200) NOT NULL,
    descricao VARCHAR(140),
    pagador_nome VARCHAR(200) NOT NULL,
    pagador_documento VARCHAR(18) NOT NULL,
    recebedor_nome VARCHAR(200),
    recebedor_documento VARCHAR(18),
    status VARCHAR(20) DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'APROVADO', 'REJEITADO', 'CANCELADO', 'DEVOLVIDO')),
    qr_code TEXT,
    payload_pix TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_processamento TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Conciliação Bancária
CREATE TABLE conciliacao_bancaria (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    conta_bancaria_id BIGINT NOT NULL,
    data_transacao DATE NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    tipo_transacao VARCHAR(20) CHECK (tipo_transacao IN ('BOLETO', 'PIX', 'TED', 'DOC', 'TRANSFERENCIA', 'DEPOSITO', 'SAQUE', 'TARIFA')),
    documento_referencia VARCHAR(100),
    conciliado BOOLEAN DEFAULT FALSE,
    data_conciliacao TIMESTAMP,
    observacoes VARCHAR(500),
    saldo_anterior DECIMAL(15,2),
    saldo_atual DECIMAL(15,2),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para performance
CREATE INDEX idx_boletos_empresa_id ON boletos(empresa_id);
CREATE INDEX idx_boletos_conta_bancaria_id ON boletos(conta_bancaria_id);
CREATE INDEX idx_boletos_status ON boletos(status);
CREATE INDEX idx_boletos_data_vencimento ON boletos(data_vencimento);
CREATE INDEX idx_boletos_linha_digitavel ON boletos(linha_digitavel);

CREATE INDEX idx_transacoes_pix_empresa_id ON transacoes_pix(empresa_id);
CREATE INDEX idx_transacoes_pix_conta_bancaria_id ON transacoes_pix(conta_bancaria_id);
CREATE INDEX idx_transacoes_pix_status ON transacoes_pix(status);
CREATE INDEX idx_transacoes_pix_chave_pix ON transacoes_pix(chave_pix);
CREATE INDEX idx_transacoes_pix_txid ON transacoes_pix(txid);

CREATE INDEX idx_conciliacao_empresa_id ON conciliacao_bancaria(empresa_id);
CREATE INDEX idx_conciliacao_conta_bancaria_id ON conciliacao_bancaria(conta_bancaria_id);
CREATE INDEX idx_conciliacao_data_transacao ON conciliacao_bancaria(data_transacao);
CREATE INDEX idx_conciliacao_conciliado ON conciliacao_bancaria(conciliado);

-- Dados de exemplo
INSERT INTO boletos (empresa_id, conta_bancaria_id, numero_documento, linha_digitavel, codigo_barras, valor, data_vencimento, sacado_nome, sacado_documento, instrucoes, status, nosso_numero) VALUES
(1, 1, 'DOC001', '34191.23456 78901.234567 89012.345678 1 23456789012345', '34191234567890123456789012345678901234567890', 150.00, '2024-02-15', 'João Silva', '12345678901', 'Pagamento até o vencimento', 'PENDENTE', '1234567890'),
(1, 1, 'DOC002', '34191.23456 78901.234567 89012.345679 2 23456789012346', '34191234567890123456789012345678901234567891', 250.00, '2024-02-20', 'Maria Santos', '98765432100', 'Não receber após vencimento', 'PAGO', '1234567891'),
(2, 2, 'DOC003', '34191.23456 78901.234567 89012.345680 3 23456789012347', '34191234567890123456789012345678901234567892', 500.00, '2024-02-25', 'Pedro Oliveira', '11122233344', 'Multa de 2% após vencimento', 'PENDENTE', '1234567892');

INSERT INTO transacoes_pix (empresa_id, conta_bancaria_id, end_to_end_id, txid, valor, chave_pix, descricao, pagador_nome, pagador_documento, recebedor_nome, recebedor_documento, status, qr_code, payload_pix) VALUES
(1, 1, 'E12345678901234567890123456789012', 'TXN001234567890123456789', 100.00, '12345678901', 'Pagamento de serviços', 'Cliente A', '12345678901', 'Padaria do João', '12.345.678/0001-90', 'APROVADO', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==', '00020126580014br.gov.bcb.pix0136123456789015204000053039865802BR5925PADARIA DO JOAO6009SAO PAULO62070503***6304'),
(2, 2, 'E12345678901234567890123456789013', 'TXN001234567890123456790', 75.50, 'pedro@email.com', 'Pagamento de produto', 'Cliente B', '98765432100', 'Oficina do Pedro', '98.765.432/0001-10', 'PENDENTE', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==', '00020126580014br.gov.bcb.pix0136pedro@email.com5204000053039865802BR5925OFICINA DO PEDRO6009SAO PAULO62070503***6304'),
(3, 3, 'E12345678901234567890123456789014', 'TXN001234567890123456791', 300.00, '+5511999887766', 'Consultoria', 'Cliente C', '55566677788', 'Consultoria da Ana', '55.566.677/0001-88', 'APROVADO', 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==', '00020126580014br.gov.bcb.pix0136+55119998877665204000053039865802BR5925CONSULTORIA DA ANA6009SAO PAULO62070503***6304');

INSERT INTO conciliacao_bancaria (empresa_id, conta_bancaria_id, data_transacao, descricao, valor, tipo_transacao, documento_referencia, conciliado, saldo_anterior, saldo_atual) VALUES
(1, 1, '2024-01-15', 'Pagamento de boleto DOC002', 250.00, 'BOLETO', 'DOC002', true, 1000.00, 1250.00),
(1, 1, '2024-01-16', 'Recebimento PIX', 100.00, 'PIX', 'TXN001234567890123456789', true, 1250.00, 1350.00),
(2, 2, '2024-01-17', 'Tarifa bancária', -15.00, 'TARIFA', 'TAR001', true, 500.00, 485.00),
(3, 3, '2024-01-18', 'Recebimento PIX consultoria', 300.00, 'PIX', 'TXN001234567890123456791', true, 2000.00, 2300.00),
(1, 1, '2024-01-19', 'Transferência TED', -200.00, 'TED', 'TED001', false, 1350.00, 1150.00);