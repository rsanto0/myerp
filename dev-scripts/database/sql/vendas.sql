-- ========================================
-- Vendas Module - Dados Iniciais
-- ========================================

-- Produtos por empresa
INSERT INTO produtos (empresa_id, nome, codigo, preco, categoria, unidade_medida, ativo, estoque_minimo) VALUES 
-- Padaria do João
(1, 'Pão Francês', 'PAO001', 0.50, 'PADARIA', 'UN', true, 50),
(1, 'Pão Doce', 'PAO002', 1.20, 'PADARIA', 'UN', true, 30),
(1, 'Café Expresso', 'CAF001', 3.50, 'BEBIDAS', 'UN', true, 20),
(1, 'Bolo de Chocolate', 'BOL001', 25.00, 'CONFEITARIA', 'UN', true, 5),
-- Oficina do Pedro
(2, 'Troca de Óleo', 'SRV001', 80.00, 'SERVICOS', 'UN', true, 0),
(2, 'Alinhamento', 'SRV002', 120.00, 'SERVICOS', 'UN', true, 0),
(2, 'Balanceamento', 'SRV003', 60.00, 'SERVICOS', 'UN', true, 0),
-- Consultoria da Ana
(3, 'Consultoria Empresarial', 'CON001', 200.00, 'CONSULTORIA', 'HORA', true, 0),
(3, 'Auditoria Fiscal', 'AUD001', 1500.00, 'AUDITORIA', 'UN', true, 0),
(3, 'Treinamento Corporativo', 'TRE001', 800.00, 'TREINAMENTO', 'UN', true, 0)
ON CONFLICT DO NOTHING;

-- Clientes por empresa
INSERT INTO clientes (empresa_id, nome, cpf_cnpj, email, telefone, endereco, ativo, tipo_cliente) VALUES 
-- Clientes da Padaria
(1, 'Maria Silva', '12345678901', 'maria@email.com', '11999999999', 'Rua A, 123', true, 'PESSOA_FISICA'),
(1, 'João Santos', '98765432100', 'joao@email.com', '11888888888', 'Rua B, 456', true, 'PESSOA_FISICA'),
(1, 'Empresa ABC Ltda', '12.345.678/0001-90', 'contato@abc.com', '1133333333', 'Av. Principal, 789', true, 'PESSOA_JURIDICA'),
-- Clientes da Oficina
(2, 'Carlos Motorista', '11122233344', 'carlos@email.com', '11777777777', 'Rua C, 321', true, 'PESSOA_FISICA'),
(2, 'Transportadora XYZ', '98.765.432/0001-10', 'frota@xyz.com', '1144444444', 'Rua Industrial, 100', true, 'PESSOA_JURIDICA'),
-- Clientes da Consultoria
(3, 'Empresa Tech Solutions', '44.555.666/0001-77', 'admin@techsol.com', '1155555555', 'Torre Empresarial, 2000', true, 'PESSOA_JURIDICA'),
(3, 'Startup Inovadora ME', '77.888.999/0001-33', 'contato@startup.com', '1166666666', 'Coworking Center, 50', true, 'PESSOA_JURIDICA')
ON CONFLICT DO NOTHING;

-- Vendas realizadas
INSERT INTO vendas (empresa_id, cliente_id, numero_venda, data_venda, valor_total, status, forma_pagamento, observacoes) VALUES 
-- Vendas da Padaria
(1, 1, 'VND001', CURRENT_DATE, 15.70, 'FINALIZADA', 'DINHEIRO', 'Venda balcão'),
(1, 2, 'VND002', CURRENT_DATE, 28.50, 'FINALIZADA', 'PIX', 'Cliente frequente'),
(1, 3, 'VND003', CURRENT_DATE - INTERVAL '1 day', 125.00, 'FINALIZADA', 'BOLETO', 'Pedido empresarial'),
-- Vendas da Oficina
(2, 4, 'VND101', CURRENT_DATE, 260.00, 'FINALIZADA', 'CARTAO_CREDITO', 'Serviço completo'),
(2, 5, 'VND102', CURRENT_DATE - INTERVAL '2 days', 480.00, 'FINALIZADA', 'TRANSFERENCIA', 'Manutenção frota'),
-- Vendas da Consultoria
(3, 6, 'VND201', CURRENT_DATE - INTERVAL '3 days', 4000.00, 'FINALIZADA', 'BOLETO', 'Projeto mensal'),
(3, 7, 'VND202', CURRENT_DATE - INTERVAL '1 week', 2400.00, 'FINALIZADA', 'PIX', 'Consultoria estratégica')
ON CONFLICT DO NOTHING;

-- Itens das vendas
INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal) VALUES 
-- Itens VND001 (Padaria)
(1, 1, 10, 0.50, 5.00),
(1, 2, 5, 1.20, 6.00),
(1, 3, 2, 3.50, 7.00),
-- Itens VND002 (Padaria)
(2, 1, 20, 0.50, 10.00),
(2, 4, 1, 25.00, 25.00),
-- Itens VND003 (Padaria - Empresarial)
(3, 1, 100, 0.45, 45.00), -- Desconto por quantidade
(3, 2, 50, 1.10, 55.00),
(3, 4, 1, 25.00, 25.00),
-- Itens VND101 (Oficina)
(4, 5, 1, 80.00, 80.00),
(4, 6, 1, 120.00, 120.00),
(4, 7, 1, 60.00, 60.00),
-- Itens VND102 (Oficina - Frota)
(5, 5, 4, 80.00, 320.00), -- 4 carros
(5, 6, 2, 120.00, 240.00), -- 2 alinhamentos
-- Itens VND201 (Consultoria)
(6, 8, 20, 200.00, 4000.00), -- 20 horas consultoria
-- Itens VND202 (Consultoria)
(7, 9, 1, 1500.00, 1500.00), -- Auditoria
(7, 10, 1, 800.00, 800.00) -- Treinamento
ON CONFLICT DO NOTHING;

-- Configurações de vendas por empresa
INSERT INTO configuracoes_vendas (empresa_id, desconto_maximo, comissao_vendedor, prazo_pagamento_padrao, permite_venda_fiado) VALUES 
(1, 10.00, 2.00, 0, true),
(2, 15.00, 5.00, 30, false),
(3, 20.00, 10.00, 45, false)
ON CONFLICT (empresa_id) DO NOTHING;