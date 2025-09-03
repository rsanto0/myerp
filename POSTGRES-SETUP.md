# Configuração PostgreSQL - MyERP

## 🚀 Instalação Rápida

### 1. Iniciar PostgreSQL
```bash
# Windows
start-postgres.bat

# Linux/Mac
docker-compose up -d postgres pgadmin
```

### 2. Verificar Instalação
- **PostgreSQL**: localhost:5432
- **pgAdmin**: http://localhost:5050

## 📊 Databases Criados

| Módulo | Database | Porta |
|--------|----------|-------|
| Auth Service | auth_db | 5432 |
| RH Module | rh_db | 5432 |
| Biometria | biometria_db | 5432 |
| Financeiro | financeiro_db | 5432 |
| Vendas | vendas_db | 5432 |
| Estoque | estoque_db | 5432 |
| Compras | compras_db | 5432 |

## 🔧 Configuração dos Módulos

### Auth Service
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_db
    username: myerp_user
    password: myerp_pass
```

### RH Module (Sistema Ponto)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/rh_db
    username: myerp_user
    password: myerp_pass
```

### Biometria Module
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/biometria_db
    username: myerp_user
    password: myerp_pass
```

## 🔐 Credenciais

### PostgreSQL
- **Host**: localhost:5432
- **Usuário**: myerp_user
- **Senha**: myerp_pass

### pgAdmin
- **URL**: http://localhost:5050
- **Email**: admin@myerp.com
- **Senha**: admin123

## 📝 Próximos Passos

1. Execute `start-postgres.bat`
2. Configure cada módulo com sua database específica
3. Remova dependências H2 dos módulos
4. Adicione dependência PostgreSQL nos pom.xml dos módulos
5. Teste a conexão

## 🛠️ Comandos Úteis

```bash
# Parar PostgreSQL
docker-compose down

# Ver logs
docker-compose logs postgres

# Backup database
docker exec myerp-postgres pg_dump -U myerp_user myerp_db > backup.sql

# Restaurar backup
docker exec -i myerp-postgres psql -U myerp_user myerp_db < backup.sql
```