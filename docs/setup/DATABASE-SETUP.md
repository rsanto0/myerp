# 🗄️ CONFIGURAÇÃO DE BANCO DE DADOS - MyERP

## 📋 **VISÃO GERAL**

O MyERP utiliza **PostgreSQL** como banco de dados padrão, com suporte a múltiplos SGBDs através de configuração. Este guia consolida todas as informações sobre configuração, localização e alteração de credenciais de banco.

---

## 🚀 **SETUP INICIAL POSTGRESQL**

### **1. Usando Docker (Recomendado)**
```bash
# Iniciar PostgreSQL via Docker Compose
cd dev-scripts
docker-compose up -d postgres

# Criar bancos automaticamente
cd database
powershell -ExecutionPolicy Bypass -File init-myerp.ps1
```

### **2. Instalação Manual**
```bash
# Windows
# 1. Baixar PostgreSQL 15: https://www.postgresql.org/download/windows/
# 2. Instalar com configurações padrão
# 3. Configurar usuário: myerp_user / senha: myerp_pass
# 4. Executar script de criação de bancos
```

### **3. Verificação**
```bash
# Testar conexão
cd dev-scripts/database
connect-db.bat

# Verificar configurações
verify-database-config.bat
```

---

## 📍 **LOCALIZAÇÃO DAS CONFIGURAÇÕES**

### **Config Server (Centralizadas)**
```
infrastructure/config-server/src/main/resources/config-repo/
├── application.yml          # Configurações globais
├── auth-service.yml         # Auth Service - myerp_auth
├── rh-service.yml          # RH Module - myerp_rh
├── biometria-service.yml   # Biometria Module - myerp_biometria
├── company-service.yml     # Company Module - myerp_company
├── financial-service.yml   # Financial Module - myerp_financial
└── monitoring-service.yml  # Monitoring Module (sem banco)
```

### **Módulos Individuais (Locais)**
```
infrastructure/auth-service/src/main/resources/
├── application.yml
└── application-local.yml

modules/*/src/main/resources/
└── application.yml
```

### **Infraestrutura**
```
docker-compose.yml           # PostgreSQL container
scripts/init-db.sql         # Criação de bancos
dev-scripts/database/       # Scripts de banco
```

---

## 🔧 **CONFIGURAÇÃO ATUAL PADRONIZADA**

### **Credenciais Padrão**
- **Host:** localhost:5432
- **Usuário:** myerp_user
- **Senha:** myerp_pass
- **Driver:** PostgreSQL

### **Bancos por Módulo**
| Módulo | Porta | Database | Status |
|--------|-------|----------|--------|
| **Auth Service** | 8081 | myerp_auth | ✅ |
| **RH Module** | 8082 | myerp_rh | ✅ |
| **Biometria Module** | 8083 | myerp_biometria | ✅ |
| **Company Module** | 8085 | myerp_company | ✅ |
| **Financial Module** | 8086 | myerp_financial | ✅ |
| **Monitoring Module** | 8084 | N/A (sem banco) | ✅ |

### **Configuração Padrão**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_[module]
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  flyway:
    enabled: false  # Desabilitado para desenvolvimento
```

---

## 🔐 **ALTERAÇÃO DE CREDENCIAIS**

### **Método Automatizado (Recomendado)**
```bash
# Configurador completo de datasources
cd dev-scripts/database
configure-datasources.bat

# Configura usuário, senha e tipo de banco
# Atualiza todos os arquivos automaticamente
```

### **Método Completo**
```bash
# Configurador completo (múltiplos SGBDs)
cd dev-scripts/config
configure-datasources.bat

# Suporta PostgreSQL, MySQL, SQL Server, Oracle
# Configurações avançadas de ambiente
```

### **Método Manual**
**13 arquivos que devem ser alterados:**

**1. Docker Compose:**
```yaml
# docker-compose.yml
services:
  postgres:
    environment:
      POSTGRES_USER: novo_usuario
      POSTGRES_PASSWORD: nova_senha
```

**2. Config Server (6 arquivos):**
```yaml
# config-repo/application.yml + 5 serviços específicos
spring:
  datasource:
    username: novo_usuario
    password: nova_senha
```

**3. Configurações Locais (6 arquivos):**
```yaml
# Cada módulo: application.yml
spring:
  datasource:
    username: novo_usuario
    password: nova_senha
```

### **Ordem de Alteração**
1. **Parar sistema:** `stop-myerp.bat`
2. **Alterar credenciais** nos 13 arquivos
3. **Recriar banco:** `docker-compose down -v && docker-compose up -d postgres`
4. **Criar bancos:** `create-databases.bat`
5. **Iniciar sistema:** `start-myerp.bat`

---

## 🗄️ **BANCOS E ESTRUTURA**

### **Bancos Criados Automaticamente**
```sql
-- Módulos implementados
CREATE DATABASE myerp_auth;
CREATE DATABASE myerp_rh;
CREATE DATABASE myerp_biometria;
CREATE DATABASE myerp_company;
CREATE DATABASE myerp_financial;
CREATE DATABASE myerp_monitoring;

-- Módulos futuros
CREATE DATABASE myerp_vendas;
CREATE DATABASE myerp_estoque;
CREATE DATABASE myerp_compras;

-- Dados iniciais carregados via scripts SQL
-- Localização: dev-scripts/database/sql/*.sql
```

### **Estratégia de Schema**
- **Desenvolvimento:** JPA `ddl-auto: update`
- **Produção:** Flyway migrations (planejado)
- **Testes:** H2 em memória (opcional)

---

## 🐳 **DOCKER E CONTAINERS**

### **PostgreSQL Container**
```yaml
# docker-compose.yml
services:
  postgres:
    image: postgres:15-alpine
    container_name: myerp-postgres
    environment:
      POSTGRES_DB: myerp_db
      POSTGRES_USER: myerp_user
      POSTGRES_PASSWORD: myerp_pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./scripts/init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
```

### **pgAdmin (Interface Web)**
- **URL:** http://localhost:5050
- **Email:** admin@myerp.com
- **Senha:** admin123

---

## 🔍 **VERIFICAÇÃO E TROUBLESHOOTING**

### **Verificar Bancos Criados**
```bash
# Via Docker
docker exec myerp-postgres psql -U myerp_user -d postgres -c "\l"

# Via pgAdmin
http://localhost:5050
```

### **Testar Conexões**
```bash
# Health check de cada módulo
curl http://localhost:8081/actuator/health  # Auth
curl http://localhost:8082/actuator/health  # RH
curl http://localhost:8083/actuator/health  # Biometria
curl http://localhost:8085/actuator/health  # Company
curl http://localhost:8086/actuator/health  # Financial
```

### **Problemas Comuns**

#### **Erro: database does not exist**
```bash
cd dev-scripts/database
create-databases.bat
```

#### **Erro: password authentication failed**
```bash
# Verificar se senha está correta em todos os arquivos
# Padrão atual: myerp_pass
```

#### **Erro: connection refused**
```bash
# Verificar se PostgreSQL está rodando
docker ps | grep postgres

# Iniciar se necessário
docker-compose up -d postgres
```

---

## 🚀 **PRODUÇÃO E FLYWAY**

### **⚠️ IMPORTANTE PARA PRODUÇÃO**

**Status Atual:** Flyway desabilitado para desenvolvimento
**Status Produção:** Flyway DEVE ser reativado

### **Passos para Produção**
1. **Criar scripts de migração** para cada módulo
2. **Alterar ddl-auto** de `update` para `validate`
3. **Reativar Flyway** com `enabled: true`
4. **Testar migrações** em ambiente de staging

### **Exemplo de Migração**
```sql
-- V1__Create_empresa_table.sql
CREATE TABLE empresa (
    id BIGSERIAL PRIMARY KEY,
    razao_social VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) UNIQUE,
    tipo_plano VARCHAR(20) NOT NULL
);
```

---

## 📋 **CHECKLIST DE CONFIGURAÇÃO**

### **Setup Inicial**
- [ ] PostgreSQL instalado/Docker rodando
- [ ] Bancos criados automaticamente
- [ ] Credenciais configuradas
- [ ] Conexões testadas
- [ ] Health checks passando

### **Alteração de Credenciais**
- [ ] Sistema parado
- [ ] 13 arquivos atualizados
- [ ] Docker recreado
- [ ] Bancos recriados
- [ ] Sistema reiniciado
- [ ] Testes de conexão

### **Preparação para Produção**
- [ ] Scripts Flyway criados
- [ ] ddl-auto alterado para validate
- [ ] Flyway reativado
- [ ] Migrações testadas
- [ ] Backup strategy definida

---

## 🎯 **RESUMO EXECUTIVO**

**Configuração Atual:**
- ✅ PostgreSQL padronizado
- ✅ 6 bancos criados automaticamente
- ✅ Credenciais unificadas (myerp_user/myerp_pass)
- ✅ Scripts de configuração automatizados

**Para Desenvolvimento:**
- ✅ JPA ddl-auto: update
- ✅ Flyway desabilitado
- ✅ H2 console disponível

**Para Produção:**
- ⚠️ Flyway deve ser reativado
- ⚠️ Scripts de migração necessários
- ⚠️ ddl-auto deve ser validate

**🎉 BANCO DE DADOS COMPLETAMENTE CONFIGURADO E FUNCIONAL! 🗄️✅**