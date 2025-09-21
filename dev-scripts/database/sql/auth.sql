-- ========================================
-- Auth Module - Dados Iniciais
-- ========================================

-- Usuários padrão do sistema
INSERT INTO users (login, senha, nome, cpf, role) VALUES 
('admin', 'admin123', 'Administrador do Sistema', '00000000000', 'ADMIN'),
('funcionario', '123456', 'Funcionário Exemplo', '11111111111', 'FUNCIONARIO'),
('joao.silva', '1234', 'João Silva', '12345678901', 'FUNCIONARIO'),
('maria.santos', '5678', 'Maria Santos', '98765432100', 'FUNCIONARIO'),
('pedro.admin', 'admin456', 'Pedro Administrador', '55555555555', 'ADMIN')
ON CONFLICT (login) DO NOTHING;

-- Configurações JWT (se necessário)
CREATE TABLE IF NOT EXISTS jwt_config (
    id SERIAL PRIMARY KEY,
    secret_key VARCHAR(255) NOT NULL,
    expiration_hours INTEGER DEFAULT 24,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO jwt_config (secret_key, expiration_hours) VALUES 
('myerp-secret-key-2024', 24)
ON CONFLICT DO NOTHING;

-- Log de inicialização
INSERT INTO system_logs (module, action, message, timestamp) VALUES 
('AUTH', 'INIT', 'Dados iniciais carregados com sucesso', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;