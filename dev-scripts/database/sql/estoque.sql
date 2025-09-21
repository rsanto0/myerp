-- ========================================
-- Estoque Module - Dados Iniciais
-- ========================================

-- Estoque inicial dos produtos
INSERT INTO estoque (empresa_id, produto_id, quantidade_atual, estoque_minimo, estoque_maximo, localizacao, lote) VALUES 
-- Estoque Padaria do João
(1, 1, 200, 50, 500, 'Depósito Principal', 'LOTE001'),
(1, 2, 80, 30, 200, 'Depósito Principal', 'LOTE002'),
(1, 3, 50, 20, 100, 'Balcão', 'LOTE003'),
(1, 4, 10, 5, 20, 'Vitrine Refrigerada', 'LOTE004'),
-- Estoque Oficina (peças e materiais)
(2, 5, 0, 0, 0, 'N/A', 'N/A'), -- Serviços não têm estoque
(2, 6, 0, 0, 0, 'N/A', 'N/A'),
(2, 7, 0, 0, 0, 'N/A', 'N/A'),
-- Consultoria (não possui estoque físico)
(3, 8, 0, 0, 0, 'N/A', 'N/A'),
(3, 9, 0, 0, 0, 'N/A', 'N/A'),
(3, 10, 0, 0, 0, 'N/A', 'N/A')
ON CONFLICT DO NOTHING;

-- Movimentações de estoque (histórico)
INSERT INTO movimentacoes_estoque (empresa_id, produto_id, tipo_movimentacao, quantidade, motivo, data_movimentacao, usuario_responsavel, documento_referencia) VALUES 
-- Movimentações Padaria
(1, 1, 'ENTRADA', 200, 'Estoque inicial', CURRENT_DATE - INTERVAL '7 days', 'joao.silva', 'EST_INICIAL_001'),
(1, 2, 'ENTRADA', 100, 'Estoque inicial', CURRENT_DATE - INTERVAL '7 days', 'joao.silva', 'EST_INICIAL_002'),
(1, 3, 'ENTRADA', 60, 'Estoque inicial', CURRENT_DATE - INTERVAL '7 days', 'joao.silva', 'EST_INICIAL_003'),
(1, 4, 'ENTRADA', 15, 'Estoque inicial', CURRENT_DATE - INTERVAL '7 days', 'joao.silva', 'EST_INICIAL_004'),
-- Saídas por vendas
(1, 1, 'SAIDA', 10, 'Venda VND001', CURRENT_DATE, 'maria.santos', 'VND001'),
(1, 2, 'SAIDA', 5, 'Venda VND001', CURRENT_DATE, 'maria.santos', 'VND001'),
(1, 1, 'SAIDA', 20, 'Venda VND002', CURRENT_DATE, 'maria.santos', 'VND002'),
(1, 4, 'SAIDA', 1, 'Venda VND002', CURRENT_DATE, 'maria.santos', 'VND002'),
(1, 1, 'SAIDA', 100, 'Venda VND003', CURRENT_DATE - INTERVAL '1 day', 'maria.santos', 'VND003'),
(1, 2, 'SAIDA', 50, 'Venda VND003', CURRENT_DATE - INTERVAL '1 day', 'maria.santos', 'VND003'),
(1, 4, 'SAIDA', 1, 'Venda VND003', CURRENT_DATE - INTERVAL '1 day', 'maria.santos', 'VND003'),
-- Ajustes de inventário
(1, 1, 'AJUSTE', -5, 'Perda por validade', CURRENT_DATE - INTERVAL '2 days', 'joao.silva', 'AJT001'),
(1, 2, 'AJUSTE', -3, 'Quebra no transporte', CURRENT_DATE - INTERVAL '3 days', 'joao.silva', 'AJT002')
ON CONFLICT DO NOTHING;

-- Fornecedores de produtos
INSERT INTO fornecedores_produtos (empresa_id, nome_fornecedor, cnpj, contato, telefone, email, endereco, ativo) VALUES 
(1, 'Moinho São Paulo', '12.345.678/0001-90', 'Carlos Vendas', '1133333333', 'vendas@moinho.com', 'Rua Industrial, 500', true),
(1, 'Distribuidora Café Brasil', '98.765.432/0001-10', 'Ana Comercial', '1144444444', 'comercial@cafebrasil.com', 'Av. Café, 200', true),
(1, 'Açúcar & Cia', '11.222.333/0001-44', 'Pedro Suprimentos', '1155555555', 'pedro@acucarecia.com', 'Rua Doce, 100', true)
ON CONFLICT DO NOTHING;

-- Pedidos de reposição de estoque
INSERT INTO pedidos_reposicao (empresa_id, produto_id, fornecedor_id, quantidade_solicitada, data_pedido, status, previsao_entrega, valor_unitario, valor_total) VALUES 
(1, 1, 1, 300, CURRENT_DATE + INTERVAL '1 day', 'PENDENTE', CURRENT_DATE + INTERVAL '3 days', 0.35, 105.00),
(1, 2, 1, 150, CURRENT_DATE + INTERVAL '1 day', 'PENDENTE', CURRENT_DATE + INTERVAL '3 days', 0.80, 120.00),
(1, 3, 2, 100, CURRENT_DATE, 'APROVADO', CURRENT_DATE + INTERVAL '2 days', 2.50, 250.00)
ON CONFLICT DO NOTHING;

-- Alertas de estoque baixo
INSERT INTO alertas_estoque (empresa_id, produto_id, tipo_alerta, mensagem, data_alerta, resolvido) VALUES 
(1, 1, 'ESTOQUE_BAIXO', 'Pão Francês com estoque abaixo do mínimo (50 unidades)', CURRENT_TIMESTAMP, false),
(1, 4, 'ESTOQUE_CRITICO', 'Bolo de Chocolate com estoque crítico (5 unidades)', CURRENT_TIMESTAMP, false)
ON CONFLICT DO NOTHING;

-- Configurações de estoque por empresa
INSERT INTO configuracoes_estoque (empresa_id, alerta_estoque_baixo, alerta_estoque_critico, reposicao_automatica, margem_seguranca_dias) VALUES 
(1, true, true, false, 7),
(2, false, false, false, 0), -- Oficina não usa estoque
(3, false, false, false, 0)  -- Consultoria não usa estoque
ON CONFLICT (empresa_id) DO NOTHING;

-- Inventários realizados
INSERT INTO inventarios (empresa_id, data_inventario, status, responsavel, observacoes) VALUES 
(1, CURRENT_DATE - INTERVAL '1 month', 'CONCLUIDO', 'joao.silva', 'Inventário mensal - tudo conforme'),
(1, CURRENT_DATE - INTERVAL '2 months', 'CONCLUIDO', 'joao.silva', 'Inventário mensal - pequenas divergências ajustadas')
ON CONFLICT DO NOTHING;