-- Adicionar coluna alias na tabela configuracao_camera existente

-- Adicionar coluna alias se não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'configuracao_camera' 
        AND column_name = 'alias'
    ) THEN
        ALTER TABLE configuracao_camera ADD COLUMN alias VARCHAR(100);
    END IF;
END $$;

-- Atualizar registros existentes com alias baseado no nome
UPDATE configuracao_camera 
SET alias = LOWER(REPLACE(REPLACE(nome_configuracao, ' ', '_'), '-', '_'))
WHERE alias IS NULL;

-- Tornar a coluna NOT NULL e UNIQUE após popular os dados
ALTER TABLE configuracao_camera ALTER COLUMN alias SET NOT NULL;

-- Adicionar constraint unique se não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE table_name = 'configuracao_camera' 
        AND constraint_name = 'uk_configuracao_camera_alias'
    ) THEN
        ALTER TABLE configuracao_camera ADD CONSTRAINT uk_configuracao_camera_alias UNIQUE (alias);
    END IF;
END $$;

-- Adicionar índice se não existir
CREATE INDEX IF NOT EXISTS idx_configuracao_camera_alias ON configuracao_camera(alias);

-- Comentário na coluna
COMMENT ON COLUMN configuracao_camera.alias IS 'Identificador único amigável (ex: camera_entrada, camera_saida)';