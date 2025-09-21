-- ========================================
-- Biometria Module - Dados Iniciais
-- ========================================

-- Configurações de câmeras por empresa
INSERT INTO cameras_biometria (empresa_id, nome, localizacao, ip_address, porta, ativa, tipo_camera) VALUES 
(1, 'Câmera Principal', 'Entrada Principal', '192.168.1.100', 8080, true, 'IP'),
(1, 'Câmera Saída', 'Saída Funcionários', '192.168.1.101', 8080, true, 'IP'),
(2, 'Câmera Portaria', 'Portaria', '192.168.1.102', 8080, true, 'IP'),
(3, 'Câmera Recepção', 'Recepção', '192.168.1.103', 8080, true, 'IP'),
(4, 'Câmera Entrada', 'Hall de Entrada', '192.168.1.104', 8080, true, 'IP'),
(4, 'Câmera Desenvolvimento', 'Sala Desenvolvimento', '192.168.1.105', 8080, false, 'IP')
ON CONFLICT DO NOTHING;

-- Templates biométricos (simulados para testes)
INSERT INTO templates_biometricos (funcionario_id, template_hash, algoritmo, qualidade, ativo, data_cadastro) VALUES 
(1, 'hash_biometrico_joao_silva_001', 'SHA256', 95, true, CURRENT_DATE),
(2, 'hash_biometrico_maria_santos_002', 'SHA256', 92, true, CURRENT_DATE),
(3, 'hash_biometrico_carlos_padeiro_003', 'SHA256', 88, true, CURRENT_DATE),
(4, 'hash_biometrico_pedro_oliveira_004', 'SHA256', 96, true, CURRENT_DATE),
(5, 'hash_biometrico_ana_costa_005', 'SHA256', 90, true, CURRENT_DATE),
(7, 'hash_biometrico_ana_consultora_007', 'SHA256', 94, true, CURRENT_DATE)
ON CONFLICT DO NOTHING;

-- Eventos de detecção biométrica (últimos dias)
INSERT INTO eventos_biometria (funcionario_id, camera_id, tipo_movimento, data_hora, confianca, processado, ponto_registrado) VALUES 
-- Eventos de hoje
(1, 1, 'ENTRADA', CURRENT_DATE + TIME '08:00:00', 0.95, true, true),
(2, 1, 'ENTRADA', CURRENT_DATE + TIME '08:05:00', 0.92, true, true),
(4, 3, 'ENTRADA', CURRENT_DATE + TIME '08:00:00', 0.96, true, true),
-- Eventos de ontem
(1, 2, 'SAIDA', CURRENT_DATE - INTERVAL '1 day' + TIME '18:00:00', 0.94, true, true),
(2, 2, 'SAIDA', CURRENT_DATE - INTERVAL '1 day' + TIME '18:10:00', 0.91, true, true),
(4, 3, 'SAIDA', CURRENT_DATE - INTERVAL '1 day' + TIME '18:30:00', 0.95, true, true),
-- Eventos pendentes (baixa confiança)
(3, 1, 'ENTRADA', CURRENT_DATE + TIME '08:30:00', 0.65, false, false),
(5, 3, 'ENTRADA', CURRENT_DATE + TIME '08:45:00', 0.70, false, false)
ON CONFLICT DO NOTHING;

-- Configurações de detecção por empresa
INSERT INTO configuracoes_biometria (empresa_id, confianca_minima, deteccao_automatica, tolerancia_horario, notificar_inconsistencias) VALUES 
(1, 0.80, true, 15, true),
(2, 0.85, true, 10, true),
(3, 0.90, true, 5, true),
(4, 0.85, true, 15, false)
ON CONFLICT (empresa_id) DO NOTHING;

-- Sugestões para RH (eventos que precisam de validação manual)
INSERT INTO sugestoes_rh (funcionario_id, evento_biometria_id, tipo_sugestao, motivo, status, data_criacao) VALUES 
(3, 7, 'VALIDAR_PONTO', 'Confiança biométrica baixa (65%)', 'PENDENTE', CURRENT_TIMESTAMP),
(5, 8, 'VALIDAR_PONTO', 'Confiança biométrica baixa (70%)', 'PENDENTE', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;