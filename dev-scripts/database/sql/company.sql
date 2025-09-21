-- ========================================
-- Company Module - Dados Iniciais
-- ========================================

-- Empresas de exemplo para multi-tenant
INSERT INTO empresas (cnpj, razao_social, nome_fantasia, plano, grupo_manutencao, ativa) VALUES 
('12.345.678/0001-90', 'Padaria do João Ltda', 'Padaria do João', 'BASIC', 'C', true),
('98.765.432/0001-10', 'Oficina Mecânica Pedro ME', 'Oficina do Pedro', 'STANDARD', 'B', true),
('11.222.333/0001-44', 'Consultoria Ana & Associados', 'Consultoria da Ana', 'PREMIUM', 'A', true),
('44.555.666/0001-77', 'Tech Solutions Corp', 'TechSol', 'ENTERPRISE', 'A', true)
ON CONFLICT (cnpj) DO NOTHING;

-- Departamentos por empresa
INSERT INTO departamentos (empresa_id, nome, centro_custo, responsavel) VALUES 
(1, 'Produção', 'CC001', 'João Silva'),
(1, 'Vendas', 'CC002', 'Maria Santos'),
(1, 'Administração', 'CC003', 'João Silva'),
(2, 'Mecânica', 'CC101', 'Pedro Oliveira'),
(2, 'Atendimento', 'CC102', 'Ana Costa'),
(3, 'Consultoria', 'CC201', 'Ana Consultora'),
(3, 'Administrativo', 'CC202', 'Carlos Admin'),
(4, 'Desenvolvimento', 'CC301', 'Tech Lead'),
(4, 'Suporte', 'CC302', 'Support Manager')
ON CONFLICT DO NOTHING;

-- Cargos hierárquicos
INSERT INTO cargos (departamento_id, nome, nivel_hierarquico, salario_base, carga_horaria) VALUES 
-- Padaria do João
(1, 'Proprietário', 1, 5000.00, 40),
(1, 'Padeiro Senior', 19, 2500.00, 44),
(2, 'Atendente', 25, 1500.00, 40),
-- Oficina do Pedro
(4, 'Proprietário', 1, 6000.00, 40),
(4, 'Mecânico Senior', 20, 3000.00, 44),
(5, 'Auxiliar Mecânico', 26, 1800.00, 44),
-- Consultoria da Ana
(6, 'Sócia Diretora', 1, 8000.00, 40),
(6, 'Consultor Senior', 16, 5000.00, 40),
(7, 'Analista', 22, 3500.00, 40),
-- TechSol
(8, 'CTO', 3, 12000.00, 40),
(9, 'Analista Suporte', 22, 4000.00, 40)
ON CONFLICT DO NOTHING;

-- Feature flags por empresa
INSERT INTO feature_flags (empresa_id, feature_name, enabled) VALUES 
(3, 'BIOMETRIA_FACIAL', true),
(3, 'DASHBOARD_AVANCADO', true),
(3, 'RELATORIOS_CUSTOMIZADOS', true),
(4, 'BIOMETRIA_FACIAL', true),
(4, 'DASHBOARD_AVANCADO', true),
(4, 'RELATORIOS_CUSTOMIZADOS', true),
(4, 'API_INTEGRACAO', true)
ON CONFLICT (empresa_id, feature_name) DO NOTHING;

-- Configurações por empresa
INSERT INTO configuracoes_empresa (empresa_id, chave, valor) VALUES 
(1, 'HORARIO_FUNCIONAMENTO', '06:00-18:00'),
(1, 'TOLERANCIA_PONTO', '15'),
(1, 'EMAIL_NOTIFICACOES', 'admin@padariaojoao.com'),
(2, 'HORARIO_FUNCIONAMENTO', '08:00-18:00'),
(2, 'TOLERANCIA_PONTO', '10'),
(2, 'EMAIL_NOTIFICACOES', 'pedro@oficinapedro.com'),
(3, 'HORARIO_FUNCIONAMENTO', '09:00-18:00'),
(3, 'TOLERANCIA_PONTO', '5'),
(3, 'EMAIL_NOTIFICACOES', 'ana@consultoriaana.com'),
(4, 'HORARIO_FUNCIONAMENTO', '09:00-18:00'),
(4, 'TOLERANCIA_PONTO', '15'),
(4, 'EMAIL_NOTIFICACOES', 'admin@techsol.com')
ON CONFLICT (empresa_id, chave) DO NOTHING;