-- ========================================
-- RH Module - Dados Iniciais
-- ========================================

-- Funcionários vinculados às empresas
INSERT INTO funcionarios (nome, cpf, login, senha, role, empresa_id, cargo_id, ativo) VALUES 
-- Padaria do João (empresa_id = 1)
('João Silva', '12345678901', 'joao.silva', '1234', 'ADMIN', 1, 1, true),
('Maria Santos', '98765432100', 'maria.santos', '5678', 'FUNCIONARIO', 1, 3, true),
('Carlos Padeiro', '11122233344', 'carlos.padeiro', '9999', 'FUNCIONARIO', 1, 2, true),
-- Oficina do Pedro (empresa_id = 2)
('Pedro Oliveira', '55566677788', 'pedro.oliveira', 'admin123', 'ADMIN', 2, 4, true),
('Ana Costa', '99988877766', 'ana.costa', '4567', 'FUNCIONARIO', 2, 6, true),
('Roberto Mecânico', '33344455566', 'roberto.mecanico', '7890', 'FUNCIONARIO', 2, 5, true),
-- Consultoria da Ana (empresa_id = 3)
('Ana Consultora', '77788899900', 'ana.consultora', 'consul123', 'ADMIN', 3, 7, true),
('Carlos Admin', '12312312312', 'carlos.admin', 'admin789', 'FUNCIONARIO', 3, 9, true),
('Fernanda Analista', '45645645645', 'fernanda.analista', 'analista456', 'FUNCIONARIO', 3, 9, true)
ON CONFLICT (cpf) DO NOTHING;

-- Horários de trabalho por empresa
INSERT INTO horarios_trabalho (empresa_id, entrada, saida_almoco, retorno_almoco, saida, dias_semana) VALUES 
(1, '06:00:00', '12:00:00', '13:00:00', '18:00:00', 'SEG,TER,QUA,QUI,SEX,SAB'),
(2, '08:00:00', '12:00:00', '13:00:00', '18:00:00', 'SEG,TER,QUA,QUI,SEX'),
(3, '09:00:00', '12:00:00', '13:00:00', '18:00:00', 'SEG,TER,QUA,QUI,SEX'),
(4, '09:00:00', '12:00:00', '13:00:00', '18:00:00', 'SEG,TER,QUA,QUI,SEX')
ON CONFLICT (empresa_id) DO NOTHING;

-- Registros de ponto de exemplo (últimos 7 dias)
INSERT INTO registros_ponto (funcionario_id, tipo, data_hora, observacao) VALUES 
-- João Silva (últimos dias)
(1, 'ENTRADA', CURRENT_DATE - INTERVAL '1 day' + TIME '08:00:00', 'Ponto normal'),
(1, 'SAIDA', CURRENT_DATE - INTERVAL '1 day' + TIME '18:00:00', 'Ponto normal'),
(1, 'ENTRADA', CURRENT_DATE + TIME '08:15:00', 'Chegou 15min atrasado'),
-- Maria Santos
(2, 'ENTRADA', CURRENT_DATE - INTERVAL '1 day' + TIME '08:00:00', 'Ponto normal'),
(2, 'SAIDA', CURRENT_DATE - INTERVAL '1 day' + TIME '18:00:00', 'Ponto normal'),
(2, 'ENTRADA', CURRENT_DATE + TIME '07:55:00', 'Chegou 5min adiantado'),
-- Pedro Oliveira
(4, 'ENTRADA', CURRENT_DATE - INTERVAL '1 day' + TIME '08:00:00', 'Ponto normal'),
(4, 'SAIDA', CURRENT_DATE - INTERVAL '1 day' + TIME '18:30:00', 'Hora extra'),
(4, 'ENTRADA', CURRENT_DATE + TIME '08:00:00', 'Ponto normal')
ON CONFLICT DO NOTHING;

-- Configurações de tolerância por empresa
INSERT INTO configuracoes_ponto (empresa_id, tolerancia_entrada, tolerancia_saida, permite_hora_extra, notificar_atraso) VALUES 
(1, 15, 15, true, true),
(2, 10, 10, true, true),
(3, 5, 5, false, true),
(4, 15, 15, true, false)
ON CONFLICT (empresa_id) DO NOTHING;