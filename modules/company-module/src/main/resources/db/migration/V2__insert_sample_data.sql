-- Dados de exemplo para testes (SAFE - não afeta produção)

-- Empresa de exemplo 1 - Padaria do João
INSERT INTO empresa (
    razao_social, nome_fantasia, cnpj, 
    endereco, cidade, estado, cep,
    telefone, email,
    tipo_plano, grupo_manutencao,
    data_fundacao
) VALUES (
    'Padaria do João Ltda', 'Padaria do João', '12.345.678/0001-90',
    'Rua das Flores, 123', 'São Paulo', 'SP', '01234-567',
    '(11) 1234-5678', 'contato@padariadojoao.com.br',
    'BASIC', 'C',
    '2020-01-15 08:00:00'
) ON CONFLICT (cnpj) DO NOTHING;

-- Empresa de exemplo 2 - Oficina do Pedro
INSERT INTO empresa (
    razao_social, nome_fantasia, cnpj,
    endereco, cidade, estado, cep,
    telefone, email,
    tipo_plano, grupo_manutencao,
    data_fundacao
) VALUES (
    'Oficina Pedro Auto Ltda', 'Oficina do Pedro', '98.765.432/0001-10',
    'Av. Principal, 456', 'Rio de Janeiro', 'RJ', '20123-456',
    '(21) 9876-5432', 'pedro@oficinapedro.com.br',
    'STANDARD', 'B',
    '2019-03-20 09:00:00'
) ON CONFLICT (cnpj) DO NOTHING;

-- Empresa de exemplo 3 - Consultoria da Ana
INSERT INTO empresa (
    razao_social, nome_fantasia, cnpj,
    endereco, cidade, estado, cep,
    telefone, email,
    tipo_plano, grupo_manutencao,
    data_fundacao
) VALUES (
    'Ana Consultoria Empresarial S/A', 'Consultoria Ana', '11.222.333/0001-44',
    'Torre Empresarial, Sala 1001', 'Belo Horizonte', 'MG', '30112-000',
    '(31) 3333-4444', 'ana@consultoriaana.com.br',
    'PREMIUM', 'A',
    '2018-07-10 10:00:00'
) ON CONFLICT (cnpj) DO NOTHING;

-- Departamentos para Padaria do João (empresa_id = 1)
INSERT INTO departamento (empresa_id, nome, descricao, codigo, centro_custo) 
SELECT 1, 'Produção', 'Produção de pães e doces', 'PROD', 'CC001'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 1)
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO departamento (empresa_id, nome, descricao, codigo, centro_custo)
SELECT 1, 'Vendas', 'Atendimento ao cliente', 'VEND', 'CC002'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 1)
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO departamento (empresa_id, nome, descricao, codigo, centro_custo)
SELECT 1, 'Administração', 'Gestão administrativa', 'ADM', 'CC003'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 1)
ON CONFLICT (empresa_id, nome) DO NOTHING;

-- Departamentos para Oficina do Pedro (empresa_id = 2)
INSERT INTO departamento (empresa_id, nome, descricao, codigo, centro_custo)
SELECT 2, 'Mecânica', 'Serviços mecânicos', 'MEC', 'CC101'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 2)
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO departamento (empresa_id, nome, descricao, codigo, centro_custo)
SELECT 2, 'Atendimento', 'Recepção de clientes', 'ATEND', 'CC102'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 2)
ON CONFLICT (empresa_id, nome) DO NOTHING;

-- Departamentos para Consultoria da Ana (empresa_id = 3)
INSERT INTO departamento (empresa_id, nome, descricao, codigo, centro_custo)
SELECT 3, 'Consultoria', 'Serviços de consultoria', 'CONS', 'CC201'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 3)
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO departamento (empresa_id, nome, descricao, codigo, centro_custo)
SELECT 3, 'Administrativo', 'Suporte administrativo', 'ADM', 'CC202'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 3)
ON CONFLICT (empresa_id, nome) DO NOTHING;

-- Cargos para Padaria do João
INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 1, 'Proprietário', 'PRESIDENTE', 1, 5000.00, d.id
FROM departamento d WHERE d.empresa_id = 1 AND d.nome = 'Administração'
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 1, 'Padeiro', 'TECNICO', 23, 2500.00, d.id
FROM departamento d WHERE d.empresa_id = 1 AND d.nome = 'Produção'
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 1, 'Atendente', 'FUNCIONARIO', 26, 1800.00, d.id
FROM departamento d WHERE d.empresa_id = 1 AND d.nome = 'Vendas'
ON CONFLICT (empresa_id, nome) DO NOTHING;

-- Cargos para Oficina do Pedro
INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 2, 'Proprietário', 'PRESIDENTE', 1, 8000.00, d.id
FROM departamento d WHERE d.empresa_id = 2 AND d.nome = 'Atendimento'
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 2, 'Mecânico Senior', 'TECNICO', 23, 4500.00, d.id
FROM departamento d WHERE d.empresa_id = 2 AND d.nome = 'Mecânica'
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 2, 'Auxiliar Mecânico', 'ASSISTENTE', 22, 2200.00, d.id
FROM departamento d WHERE d.empresa_id = 2 AND d.nome = 'Mecânica'
ON CONFLICT (empresa_id, nome) DO NOTHING;

-- Cargos para Consultoria da Ana
INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 3, 'Sócia Diretora', 'PRESIDENTE', 1, 15000.00, d.id
FROM departamento d WHERE d.empresa_id = 3 AND d.nome = 'Administrativo'
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 3, 'Consultor Senior', 'ANALISTA_SENIOR', 19, 8000.00, d.id
FROM departamento d WHERE d.empresa_id = 3 AND d.nome = 'Consultoria'
ON CONFLICT (empresa_id, nome) DO NOTHING;

INSERT INTO cargo (empresa_id, nome, tipo_cargo, nivel_hierarquico, salario_base, departamento_id)
SELECT 3, 'Analista', 'ANALISTA_PLENO', 20, 5000.00, d.id
FROM departamento d WHERE d.empresa_id = 3 AND d.nome = 'Consultoria'
ON CONFLICT (empresa_id, nome) DO NOTHING;

-- Feature flags de exemplo
INSERT INTO empresa_feature_flag (empresa_id, feature, ativa)
SELECT 1, 'BIOMETRIA_FACIAL', false
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 1)
ON CONFLICT (empresa_id, feature) DO NOTHING;

INSERT INTO empresa_feature_flag (empresa_id, feature, ativa)
SELECT 2, 'BIOMETRIA_FACIAL', true
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 2)
ON CONFLICT (empresa_id, feature) DO NOTHING;

INSERT INTO empresa_feature_flag (empresa_id, feature, ativa)
SELECT 3, 'BIOMETRIA_FACIAL', true
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 3)
ON CONFLICT (empresa_id, feature) DO NOTHING;

INSERT INTO empresa_feature_flag (empresa_id, feature, ativa)
SELECT 3, 'DASHBOARD_AVANCADO', true
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 3)
ON CONFLICT (empresa_id, feature) DO NOTHING;

-- Configurações de exemplo
INSERT INTO empresa_configuracao (empresa_id, chave, valor)
SELECT 1, 'HORARIO_FUNCIONAMENTO', '06:00-18:00'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 1)
ON CONFLICT (empresa_id, chave) DO NOTHING;

INSERT INTO empresa_configuracao (empresa_id, chave, valor)
SELECT 2, 'HORARIO_FUNCIONAMENTO', '08:00-18:00'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 2)
ON CONFLICT (empresa_id, chave) DO NOTHING;

INSERT INTO empresa_configuracao (empresa_id, chave, valor)
SELECT 3, 'HORARIO_FUNCIONAMENTO', '09:00-17:00'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 3)
ON CONFLICT (empresa_id, chave) DO NOTHING;