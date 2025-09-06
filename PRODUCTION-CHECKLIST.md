# 🚀 CHECKLIST PARA PRODUÇÃO - MyERP

## 🗄️ **FLYWAY - MIGRAÇÕES DE BANCO**

### ⚠️ **AÇÃO OBRIGATÓRIA ANTES DA PRODUÇÃO**

**Status Atual:** Flyway desabilitado para desenvolvimento
**Status Produção:** Flyway DEVE ser reativado

### 🔧 **PASSOS PARA ATIVAR FLYWAY**

#### **1. Criar Scripts de Migração**
```
src/main/resources/db/migration/
├── V1__Create_initial_schema.sql
├── V2__Insert_default_data.sql
└── V3__Create_indexes.sql
```

#### **2. Alterar Configuração JPA**
```yaml
# DESENVOLVIMENTO (atual)
spring:
  jpa:
    hibernate:
      ddl-auto: update  # ❌ Remover em produção

# PRODUÇÃO (obrigatório)
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # ✅ Apenas validar schema
```

#### **3. Reativar Flyway**
```yaml
# DESENVOLVIMENTO (atual)
flyway:
  enabled: false  # ❌ Temporário

# PRODUÇÃO (obrigatório)
flyway:
  enabled: true   # ✅ Controle total
  locations: classpath:db/migration
  baseline-on-migrate: true
```

### 📋 **SCRIPTS NECESSÁRIOS POR MÓDULO**

#### **Auth Service**
- V1__Create_users_table.sql
- V2__Insert_default_admin.sql

#### **RH Module**
- V1__Create_funcionarios_table.sql
- V2__Create_registros_ponto_table.sql

#### **Biometria Module**
- V1__Create_biometria_usuarios_table.sql
- V2__Create_configuracao_camera_table.sql

#### **Company Module**
- V1__Create_empresas_table.sql
- V2__Create_contas_bancarias_table.sql

#### **Financial Module**
- V1__Create_boletos_table.sql
- V2__Create_transacoes_pix_table.sql
- V3__Create_conciliacao_bancaria_table.sql

## 🔐 **SEGURANÇA - CONFIGURAÇÕES PRODUÇÃO**

### **Senhas e Secrets**
- ❌ **Remover senhas hardcoded** dos application.yml
- ✅ **Usar AWS Secrets Manager** ou variáveis de ambiente
- ✅ **Rotacionar senhas** regularmente

### **JWT Configuration**
```yaml
# DESENVOLVIMENTO (atual)
jwt:
  secret: myerp-secret-key-2024  # ❌ Hardcoded

# PRODUÇÃO (obrigatório)
jwt:
  secret: ${JWT_SECRET}  # ✅ Variável de ambiente
  expiration: ${JWT_EXPIRATION:3600000}
```

## 🌐 **CONFIGURAÇÕES DE AMBIENTE**

### **Profiles Obrigatórios**
- ✅ **local** - Desenvolvimento (atual)
- ✅ **docker** - Containers (atual)
- ⚠️ **prod** - Produção (criar)

### **Configuração Produção**
```yaml
# application-prod.yml
spring:
  profiles: prod
  
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
  jpa:
    hibernate:
      ddl-auto: validate  # ✅ OBRIGATÓRIO
    show-sql: false       # ✅ Performance
  
  flyway:
    enabled: true         # ✅ OBRIGATÓRIO
```

## 📊 **MONITORAMENTO PRODUÇÃO**

### **Logs**
- ✅ **Centralizar logs** (ELK Stack)
- ✅ **Log level INFO** (não DEBUG)
- ✅ **Structured logging** (JSON)

### **Métricas**
- ✅ **Prometheus** + Grafana
- ✅ **Health checks** robustos
- ✅ **Alertas** automáticos

## 🔄 **CI/CD PIPELINE**

### **Testes Obrigatórios**
- ✅ **Unit tests** (>80% coverage)
- ✅ **Integration tests** com TestContainers
- ✅ **Flyway migration tests**
- ✅ **Security scans**

### **Deploy Strategy**
- ✅ **Blue-Green deployment**
- ✅ **Database migration** antes do deploy
- ✅ **Rollback plan** automático

## 🛡️ **SECURITY HARDENING**

### **Network Security**
- ✅ **HTTPS only** (SSL/TLS)
- ✅ **VPC** com subnets privadas
- ✅ **Security Groups** restritivos
- ✅ **WAF** no API Gateway

### **Application Security**
- ✅ **Rate limiting**
- ✅ **CORS** configurado
- ✅ **Input validation**
- ✅ **SQL injection** protection

## 📋 **CHECKLIST FINAL**

### **Antes do Deploy Produção**
- [ ] Scripts Flyway criados e testados
- [ ] ddl-auto alterado para 'validate'
- [ ] Flyway reativado (enabled: true)
- [ ] Senhas movidas para Secrets Manager
- [ ] Profile 'prod' configurado
- [ ] Logs centralizados
- [ ] Monitoramento ativo
- [ ] Testes de migração executados
- [ ] Backup strategy definida
- [ ] Rollback plan testado

### **Pós Deploy**
- [ ] Health checks passando
- [ ] Métricas coletadas
- [ ] Logs funcionando
- [ ] Performance baseline estabelecida
- [ ] Alertas configurados

---

**⚠️ IMPORTANTE: Este checklist é OBRIGATÓRIO antes de qualquer deploy em produção!**

**📅 LEMBRETE: Flyway foi desabilitado temporariamente apenas para desenvolvimento. DEVE ser reativado para produção com controle total de migrações.**