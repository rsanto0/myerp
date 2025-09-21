-- Criação da tabela usuarios com constraint correta para MASTER
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'FUNCIONARIO',
    CONSTRAINT usuarios_role_check CHECK (role IN ('FUNCIONARIO', 'ADMIN', 'MASTER'))
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_usuarios_login ON usuarios(login);
CREATE INDEX IF NOT EXISTS idx_usuarios_cpf ON usuarios(cpf);