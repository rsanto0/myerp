# 🗄️ LOCALIZAÇÃO DAS CONFIGURAÇÕES DE BANCO DE DADOS - MyERP

## 📍 **ONDE ESTÃO AS CONFIGURAÇÕES**

### **1. CONFIG SERVER (Centralizadas)**
```
infrastructure/config-server/src/main/resources/config-repo/
├── application.yml          # Configurações globais PostgreSQL
├── auth-service.yml         # Auth Service - myerp_auth
├── rh-service.yml          # RH Module - myerp_rh
├── biometria-service.yml   # Biometria Module - myerp_biometria
├── company-service.yml     # Company Module - myerp_company
├── financial-service.yml   # Financial Module - myerp_financial
└── monitoring-service.yml  # Monitoring Module (sem banco)
```

### **2. MÓDULOS INDIVIDUAIS (Locais)**
```
# Auth Service
infrastructure/auth-service/src/main/resources/
├── application.yml
└── application-local.yml

# RH Module
modules/rh-module/src/main/resources/
└── application.yml

# Biometria Module
modules/biometria-module/src/main/resources/
└── application.yml

# Company Module
modules/company-module/src/main/resources/
└── application.yml

# Financial Module
modules/financial-module/src/main/resources/
└── application.yml
```

### **3. DOCKER E SCRIPTS**
```
# Docker Compose
docker-compose.yml           # PostgreSQL container

# Scripts de inicialização
scripts/init-db.sql         # Criação de bancos
dev-scripts/create-databases.sql
dev-scripts/create-databases.bat
```

---

## 🔧 **CONFIGURAÇÕES ATUAIS**

### **Config Server - Configurações Globais**
**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/application.yml`
```yaml
spring:
  datasource:
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  flyway:
    enabled: false
```

### **Auth Service**
**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/auth-service.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_auth
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
```

**Arquivo Local:** `infrastructure/auth-service/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_auth
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
```

### **RH Module**
**Arquivo:** `modules/rh-module/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_rh
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
```

### **Biometria Module**
**Arquivo:** `modules/biometria-module/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_biometria
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
```

### **Company Module**
**Arquivo:** `modules/company-module/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_company
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
```

### **Financial Module**
**Arquivo:** `modules/financial-module/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_financial
    username: myerp_user
    password: myerp_pass
    driver-class-name: org.postgresql.Driver
```

---

## 🐳 **DOCKER COMPOSE**

**Arquivo:** `docker-compose.yml`
```yaml
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

---

## 📊 **BANCOS CRIADOS**

### **Script de Inicialização**
**Arquivo:** `scripts/init-db.sql`
```sql
-- Databases para todos os módulos MyERP
CREATE DATABASE myerp_auth;
CREATE DATABASE myerp_rh;
CREATE DATABASE myerp_biometria;
CREATE DATABASE myerp_company;
CREATE DATABASE myerp_financial;
CREATE DATABASE myerp_monitoring;

-- Databases para módulos futuros
CREATE DATABASE myerp_vendas;
CREATE DATABASE myerp_estoque;
CREATE DATABASE myerp_compras;
```

### **Mapeamento Banco x Módulo**
| Módulo | Porta | Database | Status |
|--------|-------|----------|--------|
| **Auth Service** | 8081 | `myerp_auth` | ✅ |
| **RH Module** | 8082 | `myerp_rh` | ✅ |
| **Biometria Module** | 8083 | `myerp_biometria` | ✅ |
| **Company Module** | 8085 | `myerp_company` | ✅ |
| **Financial Module** | 8086 | `myerp_financial` | ✅ |
| **Monitoring Module** | 8084 | N/A (sem banco) | ✅ |

---

## 🔐 **CREDENCIAIS PADRÃO**

### **PostgreSQL Container**
- **Host:** localhost
- **Porta:** 5432
- **Usuário:** myerp_user
- **Senha:** myerp_pass

### **pgAdmin (Interface Web)**
- **URL:** http://localhost:5050
- **Email:** admin@myerp.com
- **Senha:** admin123

---

## 🛠️ **COMO ALTERAR CONFIGURAÇÕES**

### **1. Alterar Senha do Banco**
```bash
# 1. Alterar docker-compose.yml
POSTGRES_PASSWORD: nova_senha

# 2. Alterar em TODOS os application.yml
password: nova_senha

# 3. Recriar containers
docker-compose down
docker-compose up -d
```

### **2. Alterar Host do Banco**
```bash
# Alterar em todos os application.yml
url: jdbc:postgresql://novo_host:5432/myerp_*
```

### **3. Usar Banco Externo**
```bash
# Alterar docker-compose.yml (comentar postgres)
# Alterar application.yml com dados do banco externo
url: jdbc:postgresql://servidor-externo:5432/myerp_*
username: usuario_externo
password: senha_externa
```

---

## 📋 **VERIFICAÇÃO DE CONFIGURAÇÕES**

### **Verificar Bancos Criados**
```bash
# Via Docker
docker exec myerp-postgres psql -U myerp_user -d postgres -c "\l"

# Via pgAdmin
http://localhost:5050
```

### **Testar Conexões**
```bash
# Testar cada módulo
curl http://localhost:8081/actuator/health  # Auth
curl http://localhost:8082/actuator/health  # RH
curl http://localhost:8083/actuator/health  # Biometria
curl http://localhost:8085/actuator/health  # Company
curl http://localhost:8086/actuator/health  # Financial
```

### **Verificar Logs de Conexão**
```bash
# Logs do PostgreSQL
docker logs myerp-postgres

# Logs dos módulos (verificar conexão DB)
# Procurar por "HikariPool" ou "database connection"
```

---

## 🚨 **PROBLEMAS COMUNS**

### **Erro: database does not exist**
```bash
# Executar script de criação
cd dev-scripts
create-databases.bat
```

### **Erro: password authentication failed**
```bash
# Verificar se senha está correta em todos os arquivos
# Padrão atual: myerp_pass
```

### **Erro: connection refused**
```bash
# Verificar se PostgreSQL está rodando
docker ps | grep postgres

# Iniciar se necessário
docker-compose up -d postgres
```

---

## 📁 **RESUMO DOS LOCAIS**

### **Configurações Centralizadas (Config Server)**
```
infrastructure/config-server/src/main/resources/config-repo/
├── application.yml          # ✅ Configurações globais
├── auth-service.yml         # ✅ myerp_auth
├── rh-service.yml          # ✅ myerp_rh
├── biometria-service.yml   # ✅ myerp_biometria
├── company-service.yml     # ✅ myerp_company
├── financial-service.yml   # ✅ myerp_financial
└── monitoring-service.yml  # ✅ Sem banco
```

### **Configurações Locais (Cada Módulo)**
```
infrastructure/auth-service/src/main/resources/application.yml     # ✅
modules/rh-module/src/main/resources/application.yml              # ✅
modules/biometria-module/src/main/resources/application.yml       # ✅
modules/company-module/src/main/resources/application.yml         # ✅
modules/financial-module/src/main/resources/application.yml       # ✅
```

### **Infraestrutura**
```
docker-compose.yml           # ✅ PostgreSQL container
scripts/init-db.sql         # ✅ Criação de bancos
dev-scripts/create-databases.sql  # ✅ Script manual
```

**🎯 TODAS AS CONFIGURAÇÕES DE BANCO ESTÃO PADRONIZADAS E FUNCIONAIS! 🗄️✅**