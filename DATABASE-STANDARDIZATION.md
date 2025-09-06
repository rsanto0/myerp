# 🗄️ PADRONIZAÇÃO DE BANCO DE DADOS - MyERP

## ✅ **PROBLEMA IDENTIFICADO E CORRIGIDO**

**Problema:** Módulos usando bancos diferentes (H2 vs PostgreSQL)
**Solução:** Padronização completa para PostgreSQL

## 📊 **CONFIGURAÇÃO ATUAL PADRONIZADA**

### **Todos os Módulos Agora Usam PostgreSQL**

| Módulo | Porta | Database | Status |
|--------|-------|----------|--------|
| **Auth Service** | 8081 | `myerp_auth` | ✅ PostgreSQL |
| **RH Module** | 8082 | `myerp_rh` | ✅ PostgreSQL |
| **Biometria Module** | 8083 | `myerp_biometria` | ✅ PostgreSQL |
| **Company Module** | 8085 | `myerp_company` | ✅ PostgreSQL |
| **Financial Module** | 8086 | `myerp_financial` | ✅ PostgreSQL |
| **Monitoring Module** | 8084 | `myerp_monitoring` | ✅ PostgreSQL |

### **Configuração Padrão PostgreSQL**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/myerp_[module]
    username: myerp_user
    password: myerp123
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## 🔧 **CONFIGURAÇÕES ATUALIZADAS**

### **Config Server - Configurações Centralizadas**
- ✅ **application.yml** - Configurações globais PostgreSQL
- ✅ **auth-service.yml** - H2 → PostgreSQL
- ✅ **rh-service.yml** - H2 → PostgreSQL  
- ✅ **biometria-service.yml** - H2 → PostgreSQL
- ✅ **company-service.yml** - H2 → PostgreSQL
- ✅ **financial-service.yml** - H2 → PostgreSQL

### **Script de Inicialização**
- ✅ **init-db.sql** - Criação de todos os bancos
- ✅ **Nomes padronizados** - myerp_[module]
- ✅ **Permissões configuradas** - myerp_user

## 🚀 **BENEFÍCIOS DA PADRONIZAÇÃO**

### **Consistência**
- ✅ **Mesmo SGBD** em todos os módulos
- ✅ **Configurações uniformes** via Config Server
- ✅ **Backup centralizado** de todos os dados
- ✅ **Monitoramento unificado** de performance

### **Produção**
- ✅ **Escalabilidade** do PostgreSQL
- ✅ **Transações ACID** confiáveis
- ✅ **Replicação** e alta disponibilidade
- ✅ **Ferramentas de administração** maduras

### **Desenvolvimento**
- ✅ **Ambiente consistente** entre dev/prod
- ✅ **Migrations** com Flyway
- ✅ **Debugging** simplificado
- ✅ **Testes de integração** realistas

## 🗄️ **ESTRUTURA DE BANCOS**

### **Bancos Criados Automaticamente**
```sql
-- Módulos Ativos
CREATE DATABASE myerp_auth;        -- Auth Service
CREATE DATABASE myerp_rh;          -- RH Module  
CREATE DATABASE myerp_biometria;   -- Biometria Module
CREATE DATABASE myerp_company;     -- Company Module
CREATE DATABASE myerp_financial;   -- Financial Module
CREATE DATABASE myerp_monitoring;  -- Monitoring Module

-- Módulos Futuros
CREATE DATABASE myerp_vendas;      -- Vendas Module
CREATE DATABASE myerp_estoque;     -- Estoque Module
CREATE DATABASE myerp_compras;     -- Compras Module
```

### **Usuário e Permissões**
```sql
-- Usuário principal com acesso a todos os bancos
User: myerp_user
Pass: myerp123
Permissions: ALL PRIVILEGES em todos os bancos myerp_*
```

## 🔄 **MIGRAÇÃO REALIZADA**

### **Antes (Inconsistente)**
```yaml
# Alguns módulos
datasource:
  url: jdbc:h2:mem:auth_db
  driver-class-name: org.h2.Driver

# Outros módulos  
datasource:
  url: jdbc:postgresql://localhost:5432/myerp_company
  driver-class-name: org.postgresql.Driver
```

### **Depois (Padronizado)**
```yaml
# TODOS os módulos
datasource:
  url: jdbc:postgresql://localhost:5432/myerp_[module]
  username: myerp_user
  password: myerp123
  driver-class-name: org.postgresql.Driver
```

## 🚀 **COMO USAR**

### **Inicialização Automática**
```bash
# O script start-myerp.bat já inicia PostgreSQL
dev-scripts/start-myerp.bat

# Ou manual
dev-scripts/start-postgres.bat
```

### **Verificação dos Bancos**
```bash
# Conectar via pgAdmin
http://localhost:5050

# Ou via psql
psql -h localhost -U myerp_user -d myerp_auth
psql -h localhost -U myerp_user -d myerp_company
```

### **Configurações via Config Server**
```bash
# Verificar configurações PostgreSQL
http://localhost:8888/auth-service/default
http://localhost:8888/company-service/default
```

## ✅ **CONFIRMAÇÃO DE PADRONIZAÇÃO**

- ✅ **Todos os 6 módulos** usam PostgreSQL
- ✅ **Configurações centralizadas** no Config Server
- ✅ **Bancos criados automaticamente** via init-db.sql
- ✅ **Usuário único** com permissões adequadas
- ✅ **Nomes padronizados** myerp_[module]
- ✅ **Dialect PostgreSQL** em todos os módulos

**O sistema MyERP agora tem configuração de banco 100% padronizada e consistente! 🗄️✅**