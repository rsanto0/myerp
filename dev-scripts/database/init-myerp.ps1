# ========================================
# Script final de inicialização MyERP DB
# ========================================

# Configurações principais
$ContainerName = "myerp-postgres"
$SuperUser = "postgres"
$SuperPass = "postgres"  # senha do superusuário no docker-compose.yml

# Usuário principal da aplicação
$AppUser = "myerp_user"
$AppPass = "myerp_pass"

# Diretório opcional para scripts extras
$SqlDir = "sql"

# Lista de bancos, schemas e usuários
$Databases = @(
    @{Name="myerp_auth";       Schema="auth_schema";       User="auth_user";       Pass="auth_pass";       Script="auth.sql"},
    @{Name="myerp_rh";         Schema="rh_schema";         User="rh_user";         Pass="rh_pass";         Script="rh.sql"},
    @{Name="myerp_biometria";  Schema="biometria_schema";  User="biometria_user";  Pass="biometria_pass";  Script="biometria.sql"},
    @{Name="myerp_company";    Schema="company_schema";    User="company_user";    Pass="company_pass";    Script="company.sql"},
    @{Name="myerp_financial";  Schema="financial_schema";  User="financial_user";  Pass="financial_pass";  Script="financial.sql"},
    @{Name="myerp_monitoring"; Schema="monitoring_schema"; User="monitoring_user"; Pass="monitoring_pass"; Script="monitoring.sql"},
    @{Name="myerp_vendas";     Schema="vendas_schema";     User="vendas_user";     Pass="vendas_pass";     Script="vendas.sql"},
    @{Name="myerp_estoque";    Schema="estoque_schema";    User="estoque_user";    Pass="estoque_pass";    Script="estoque.sql"},
    @{Name="myerp_compras";    Schema="compras_schema";    User="compras_user";    Pass="compras_pass";    Script="compras.sql"}
)

# Função auxiliar para rodar psql no container
function Run-PSQL($Database, $SQL) {
    docker exec -i $ContainerName psql -U $SuperUser -d $Database -c "$SQL" | Out-Null
}

# 1️⃣ Verificar se o container está rodando
$running = docker ps --filter "name=$ContainerName" --format "{{.Names}}"
if (-not $running) {
    Write-Host "⚠️  Container $ContainerName não está rodando. Tentando subir..."
    docker compose up -d postgres
}

# 2️⃣ Esperar Postgres ficar pronto
Write-Host "⏳ Aguardando Postgres ficar pronto..."
do {
    Start-Sleep -Seconds 2
    $status = docker exec $ContainerName pg_isready -U $SuperUser 2>&1
} until ($status -like "*accepting connections*")
Write-Host "✅ Postgres está pronto!"

# 3️⃣ Criar usuário principal (ignora se já existir)
Write-Host "➡️ Criando usuário principal: $AppUser"
docker exec -i $ContainerName psql -U $SuperUser -d postgres -c "CREATE USER $AppUser WITH PASSWORD '$AppPass';" 2>$null

# 4️⃣ Criar bancos, usuários de módulo, schemas e permissões
foreach ($db in $Databases) {

    $dbName = $db.Name
    $schema = $db.Schema
    $user = $db.User
    $pass = $db.Pass
    $extraScript = Join-Path $SqlDir $db.Script

    Write-Host "➡️  Configurando banco: $dbName (schema: $schema, user: $user)"

    # Criar banco se não existir
    docker exec -i $ContainerName psql -U $SuperUser -d postgres -c "CREATE DATABASE $dbName;" 2>$null

    # Criar usuário do módulo (ignora se já existir)
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "CREATE USER $user WITH PASSWORD '$pass';" 2>$null

    # Criar schema para aplicação
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "CREATE SCHEMA IF NOT EXISTS $schema AUTHORIZATION $AppUser;"

    # Ajustar search_path
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "ALTER ROLE $AppUser SET search_path TO $schema;"

    # Conceder permissões ao usuário principal
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "GRANT ALL PRIVILEGES ON SCHEMA $schema TO $AppUser;"
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "GRANT USAGE, CREATE ON SCHEMA $schema TO $AppUser;"
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "ALTER DEFAULT PRIVILEGES IN SCHEMA $schema GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO $AppUser;"
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "ALTER DEFAULT PRIVILEGES IN SCHEMA $schema GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO $AppUser;"

    # Conceder acesso total do banco ao usuário do módulo
    docker exec -i $ContainerName psql -U $SuperUser -d $dbName -c "GRANT ALL PRIVILEGES ON DATABASE $dbName TO $user;"

    # Executar script extra, se existir
    if (Test-Path $extraScript) {
        Write-Host "   Executando script extra: $extraScript"
        $targetPath = "/tmp/" + $db.Script
        $containerTarget = $ContainerName + ":" + $targetPath
        docker cp $extraScript $containerTarget
        docker exec -i $ContainerName psql -U $SuperUser -d $dbName -f $targetPath
    }
}

Write-Host "✅ Inicialização completa! Todos os bancos, schemas e usuários foram configurados."
