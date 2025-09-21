-- ========================================
-- Compras Module - Dados Iniciais
-- ========================================

-- Fornecedores principais
INSERT INTO fornecedores (empresa_id, nome, cnpj, inscricao_estadual, contato_principal, telefone, email, endereco, cep, cidade, estado, ativo, categoria) VALUES 
-- Fornecedores da Padaria
(1, 'Moinho Três Corações', '12.345.678/0001-90', '123456789', 'Carlos Silva', '1133333333', 'vendas@moinho3c.com', 'Rua Industrial, 500', '01234-567', 'São Paulo', 'SP', true, 'MATERIA_PRIMA'),
(1, 'Distribuidora Café Premium', '98.765.432/0001-10', '987654321', 'Ana Costa', '1144444444', 'comercial@cafepremium.com', 'Av. Café, 200', '02345-678', 'São Paulo', 'SP', true, 'BEBIDAS'),
(1, 'Embalagens & Cia', '11.222.333/0001-44', '111222333', 'Pedro Santos', '1155555555', 'pedro@embalagens.com', 'Rua Embalagem, 100', '03456-789', 'São Paulo', 'SP', true, 'EMBALAGENS'),
-- Fornecedores da Oficina
(2, 'Auto Peças Brasil', '44.555.666/0001-77', '444555666', 'Roberto Mecânico', '1166666666', 'vendas@autopecas.com', 'Av. Automotive, 1000', '04567-890', 'São Paulo', 'SP', true, 'PECAS_AUTOMOTIVAS'),
(2, 'Óleos Lubrificantes SA', '77.888.999/0001-33', '777888999', 'Luiz Técnico', '1177777777', 'comercial@oleoslubrif.com', 'Rua Lubrificante, 300', '05678-901', 'São Paulo', 'SP', true, 'LUBRIFICANTES'),
-- Fornecedores da Consultoria
(3, 'Materiais de Escritório Plus', '33.444.555/0001-22', '333444555', 'Carla Vendas', '1188888888', 'carla@escritorioplus.com', 'Rua Escritório, 150', '06789-012', 'São Paulo', 'SP', true, 'ESCRITORIO')
ON CONFLICT DO NOTHING;

-- Categorias de produtos para compra
INSERT INTO categorias_compra (empresa_id, nome, descricao, ativa) VALUES 
(1, 'Matéria Prima', 'Ingredientes para produção', true),
(1, 'Embalagens', 'Sacos, caixas e embalagens', true),
(1, 'Bebidas', 'Café, sucos e refrigerantes', true),
(2, 'Peças Automotivas', 'Peças para manutenção', true),
(2, 'Lubrificantes', 'Óleos e graxas', true),
(3, 'Material de Escritório', 'Papelaria e suprimentos', true)
ON CONFLICT DO NOTHING;

-- Produtos para compra
INSERT INTO produtos_compra (empresa_id, categoria_id, nome, codigo, unidade_medida, preco_medio, fornecedor_preferencial_id, ativo) VALUES 
-- Produtos Padaria
(1, 1, 'Farinha de Trigo Especial', 'FAR001', 'KG', 3.50, 1, true),
(1, 1, 'Açúcar Cristal', 'ACU001', 'KG', 2.80, 1, true),
(1, 1, 'Fermento Biológico', 'FER001', 'KG', 8.50, 1, true),
(1, 2, 'Saco Papel Pão', 'EMB001', 'UN', 0.15, 3, true),
(1, 3, 'Café Torrado Moído', 'CAF001', 'KG', 18.00, 2, true),
-- Produtos Oficina
(2, 4, 'Filtro de Óleo', 'FIL001', 'UN', 25.00, 4, true),
(2, 4, 'Pastilha de Freio', 'PAS001', 'JOGO', 120.00, 4, true),
(2, 5, 'Óleo Motor 5W30', 'OLE001', 'LITRO', 35.00, 5, true),
-- Produtos Consultoria
(3, 6, 'Papel A4', 'PAP001', 'RESMA', 25.00, 6, true),
(3, 6, 'Toner Impressora', 'TON001', 'UN', 180.00, 6, true)
ON CONFLICT DO NOTHING;

-- Pedidos de compra
INSERT INTO pedidos_compra (empresa_id, fornecedor_id, numero_pedido, data_pedido, status, valor_total, prazo_entrega, condicoes_pagamento, responsavel_compra) VALUES 
-- Pedidos Padaria
(1, 1, 'PC001', CURRENT_DATE - INTERVAL '5 days', 'ENTREGUE', 875.00, CURRENT_DATE - INTERVAL '2 days', '30 dias', 'joao.silva'),
(1, 2, 'PC002', CURRENT_DATE - INTERVAL '3 days', 'PENDENTE', 540.00, CURRENT_DATE + INTERVAL '2 days', '15 dias', 'joao.silva'),
(1, 3, 'PC003', CURRENT_DATE, 'APROVADO', 150.00, CURRENT_DATE + INTERVAL '5 days', 'À vista', 'maria.santos'),
-- Pedidos Oficina
(2, 4, 'PC101', CURRENT_DATE - INTERVAL '7 days', 'ENTREGUE', 1450.00, CURRENT_DATE - INTERVAL '3 days', '45 dias', 'pedro.oliveira'),
(2, 5, 'PC102', CURRENT_DATE - INTERVAL '1 day', 'APROVADO', 700.00, CURRENT_DATE + INTERVAL '3 days', '30 dias', 'pedro.oliveira'),
-- Pedidos Consultoria
(3, 6, 'PC201', CURRENT_DATE - INTERVAL '10 days', 'ENTREGUE', 385.00, CURRENT_DATE - INTERVAL '7 days', '30 dias', 'ana.consultora')
ON CONFLICT DO NOTHING;

-- Itens dos pedidos de compra
INSERT INTO itens_pedido_compra (pedido_id, produto_id, quantidade, preco_unitario, subtotal) VALUES 
-- Itens PC001 (Padaria - Entregue)
(1, 1, 100, 3.50, 350.00), -- Farinha
(1, 2, 50, 2.80, 140.00),  -- Açúcar
(1, 3, 5, 8.50, 42.50),    -- Fermento
(1, 4, 1000, 0.15, 150.00), -- Sacos
(1, 5, 10, 18.00, 180.00), -- Café
-- Itens PC002 (Padaria - Pendente)
(2, 5, 30, 18.00, 540.00), -- Café
-- Itens PC003 (Padaria - Aprovado)
(3, 4, 1000, 0.15, 150.00), -- Sacos
-- Itens PC101 (Oficina - Entregue)
(4, 6, 20, 25.00, 500.00),   -- Filtros
(4, 7, 5, 120.00, 600.00),   -- Pastilhas
(4, 8, 10, 35.00, 350.00),   -- Óleo
-- Itens PC102 (Oficina - Aprovado)
(5, 8, 20, 35.00, 700.00),   -- Óleo
-- Itens PC201 (Consultoria - Entregue)
(6, 9, 10, 25.00, 250.00),   -- Papel
(6, 10, 1, 180.00, 180.00)   -- Toner
ON CONFLICT DO NOTHING;

-- Recebimentos de mercadorias
INSERT INTO recebimentos (pedido_compra_id, data_recebimento, quantidade_recebida, responsavel_recebimento, observacoes, status_qualidade) VALUES 
(1, CURRENT_DATE - INTERVAL '2 days', 100, 'maria.santos', 'Mercadoria conforme pedido', 'APROVADO'),
(4, CURRENT_DATE - INTERVAL '3 days', 100, 'ana.costa', 'Todas as peças em perfeito estado', 'APROVADO'),
(6, CURRENT_DATE - INTERVAL '7 days', 100, 'carlos.admin', 'Material de escritório OK', 'APROVADO')
ON CONFLICT DO NOTHING;

-- Configurações de compras por empresa
INSERT INTO configuracoes_compras (empresa_id, limite_aprovacao_gerente, limite_aprovacao_diretor, prazo_pagamento_padrao, desconto_pagamento_vista, alerta_prazo_entrega) VALUES 
(1, 1000.00, 5000.00, 30, 5.00, true),
(2, 2000.00, 10000.00, 45, 3.00, true),
(3, 1500.00, 8000.00, 30, 7.00, true)
ON CONFLICT (empresa_id) DO NOTHING;