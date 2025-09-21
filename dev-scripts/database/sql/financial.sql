-- ========================================
-- Financial Module - Dados Iniciais
-- ========================================

-- Contas bancárias das empresas
INSERT INTO contas_bancarias (empresa_id, banco, agencia, conta, digito, tipo_conta, pix_habilitado, boleto_habilitado, ativa) VALUES 
(1, '001', '1234', '567890', '1', 'CORRENTE', true, true, true),
(1, '237', '5678', '123456', '7', 'POUPANCA', false, false, true),
(2, '104', '9876', '654321', '0', 'CORRENTE', true, true, true),
(3, '033', '1111', '999888', '5', 'CORRENTE', true, true, true),
(4, '341', '2222', '777666', '3', 'CORRENTE', true, true, true)
ON CONFLICT DO NOTHING;

-- Chaves PIX das empresas
INSERT INTO chaves_pix (empresa_id, conta_bancaria_id, tipo_chave, chave, ativa) VALUES 
(1, 1, 'CNPJ', '12.345.678/0001-90', true),
(1, 1, 'EMAIL', 'pix@padariaojoao.com', true),
(2, 3, 'CNPJ', '98.765.432/0001-10', true),
(3, 4, 'CNPJ', '11.222.333/0001-44', true),
(3, 4, 'TELEFONE', '+5511999999999', true),
(4, 5, 'CNPJ', '44.555.666/0001-77', true)
ON CONFLICT DO NOTHING;

-- Boletos de exemplo
INSERT INTO boletos (empresa_id, conta_bancaria_id, numero_documento, valor, vencimento, status, pagador_nome, pagador_cpf_cnpj, nosso_numero) VALUES 
(1, 1, 'DOC001', 150.00, CURRENT_DATE + INTERVAL '30 days', 'PENDENTE', 'Cliente A', '12345678901', '001234567890'),
(1, 1, 'DOC002', 250.00, CURRENT_DATE - INTERVAL '5 days', 'PAGO', 'Cliente B', '98765432100', '001234567891'),
(1, 1, 'DOC003', 500.00, CURRENT_DATE + INTERVAL '15 days', 'PENDENTE', 'Cliente C', '11122233344', '001234567892'),
(2, 3, 'DOC101', 800.00, CURRENT_DATE + INTERVAL '20 days', 'PENDENTE', 'Empresa XYZ', '12.345.678/0001-90', '104567890123'),
(3, 4, 'DOC201', 1200.00, CURRENT_DATE - INTERVAL '10 days', 'VENCIDO', 'Consultoria ABC', '98.765.432/0001-10', '033123456789'),
(4, 5, 'DOC301', 2500.00, CURRENT_DATE + INTERVAL '45 days', 'PENDENTE', 'Tech Corp', '44.555.666/0001-77', '341987654321')
ON CONFLICT DO NOTHING;

-- Transações PIX
INSERT INTO transacoes_pix (empresa_id, chave_pix_id, valor, descricao, status, txid, end_to_end_id, qr_code_payload) VALUES 
(1, 1, 100.00, 'Pagamento Cliente A', 'APROVADO', 'TXD001ABC123', 'E12345678202412151000001', 'pix_payload_001'),
(1, 2, 75.50, 'Pagamento Cliente B', 'PENDENTE', 'TXD002DEF456', 'E12345678202412151000002', 'pix_payload_002'),
(2, 3, 300.00, 'Serviço Mecânico', 'APROVADO', 'TXD003GHI789', 'E98765432202412151000003', 'pix_payload_003'),
(3, 4, 1500.00, 'Consultoria Mensal', 'APROVADO', 'TXD004JKL012', 'E11222333202412151000004', 'pix_payload_004'),
(3, 5, 850.00, 'Projeto Especial', 'PENDENTE', 'TXD005MNO345', 'E11222333202412151000005', 'pix_payload_005'),
(4, 6, 5000.00, 'Desenvolvimento Sistema', 'APROVADO', 'TXD006PQR678', 'E44555666202412151000006', 'pix_payload_006')
ON CONFLICT DO NOTHING;

-- Conciliação bancária
INSERT INTO conciliacao_bancaria (empresa_id, conta_bancaria_id, data_movimento, tipo_movimento, valor, descricao, conciliado, referencia_externa) VALUES 
(1, 1, CURRENT_DATE, 'CREDITO', 250.00, 'Pagamento Boleto DOC002', true, 'BOL_DOC002'),
(1, 1, CURRENT_DATE, 'CREDITO', 100.00, 'PIX Recebido', true, 'PIX_TXD001ABC123'),
(2, 3, CURRENT_DATE, 'CREDITO', 300.00, 'PIX Recebido', true, 'PIX_TXD003GHI789'),
(3, 4, CURRENT_DATE, 'CREDITO', 1500.00, 'PIX Recebido', true, 'PIX_TXD004JKL012'),
(4, 5, CURRENT_DATE, 'CREDITO', 5000.00, 'PIX Recebido', true, 'PIX_TXD006PQR678'),
(1, 1, CURRENT_DATE, 'DEBITO', 50.00, 'Taxa Bancária', false, 'TAXA_001')
ON CONFLICT DO NOTHING;

-- Configurações financeiras por empresa
INSERT INTO configuracoes_financeiras (empresa_id, dias_vencimento_padrao, juros_mora, multa_atraso, desconto_antecipacao, notificar_vencimento) VALUES 
(1, 30, 2.00, 10.00, 5.00, true),
(2, 15, 1.50, 8.00, 3.00, true),
(3, 45, 2.50, 12.00, 7.00, true),
(4, 30, 2.00, 10.00, 5.00, false)
ON CONFLICT (empresa_id) DO NOTHING;