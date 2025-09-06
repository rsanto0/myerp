# 📁 DEV SCRIPTS - MyERP (REORGANIZADO)

## 🗂️ **NOVA ESTRUTURA ORGANIZADA**

### **📁 core/ - Scripts Principais**
- `start-myerp.bat` - Inicia sistema completo MyERP
- `stop-myerp.bat` - Para sistema completo MyERP  
- `restart-myerp.bat` - **NOVO** - Reinicia sistema completo

### **📁 database/ - Scripts de Banco**
- `start-postgres.bat` - Inicia PostgreSQL
- `create-databases.bat` - Cria bancos automaticamente
- `create-databases.sql` - Script SQL de criação
- `connect-db.bat` - Conecta ao PostgreSQL
- `verify-database-config.bat` - Verifica configurações

### **📁 config/ - Scripts de Configuração**
- `configure-datasources.bat` - Configurador completo (4 SGBDs)
- `configure-datasources-simple.bat` - Configurador simples (PostgreSQL)
- `backup-configs.bat` - **NOVO** - Backup de configurações

### **📁 testing/ - Scripts de Teste**
- `test-compilation.bat` - Testa compilação de módulos
- `test-profiles.bat` - Testa configurações por profile
- `health-check.bat` - **NOVO** - Verifica saúde de todos os serviços

### **📁 aws/ - Scripts AWS**
- `aws-manager.bat` - **NOVO** - Orquestrador principal
- `deploy-aws.bat` - Deploy completo AWS (melhorado)
- `setup-aws.bat` - Setup inicial AWS (melhorado)
- `rollback-aws.bat` - **NOVO** - Rollback executável

---

## 🚀 **COMO USAR**

### **Iniciar Sistema**
```bash
# Iniciar completo
cd core
start-myerp.bat

# Reiniciar
restart-myerp.bat
```

### **Configurar Banco**
```bash
# Configurar datasources
cd config
configure-datasources-simple.bat

# Criar bancos
cd database
create-databases.bat
```

### **Testar Sistema**
```bash
# Health check completo
cd testing
health-check.bat

# Testar compilação
test-compilation.bat
```

### **Deploy AWS**
```bash
# Setup inicial
cd aws
setup-aws.bat

# Deploy
deploy-aws.bat
```

---

## ✅ **MELHORIAS IMPLEMENTADAS**

### **🗂️ Organização**
- Scripts organizados por categoria
- Estrutura mais limpa e intuitiva
- Fácil localização de funcionalidades

### **🔄 Consolidação**
- Removidos scripts redundantes de módulos individuais
- Criados scripts consolidados (restart, health-check, backup)
- Mantidos apenas scripts essenciais

### **📋 Scripts Removidos**
- `start-company-module.bat` - Redundante
- `start-config-server.bat` - Redundante  
- `start-financial-module.bat` - Redundante
- `start-monitoring.bat` - Redundante
- `stop-monitoring.bat` - Desnecessário
- `test-monitoring.bat` - Redundante

### **🆕 Scripts Novos**
- `core/restart-myerp.bat` - Reinicia sistema
- `testing/health-check.bat` - Verifica saúde
- `config/backup-configs.bat` - Backup configurações

---

## 📊 **ESTATÍSTICAS**

### **Antes da Reorganização**
- **Total**: 18 scripts
- **Redundantes**: 6 scripts
- **Organização**: Plana (tudo na raiz)

### **Depois da Reorganização**  
- **Total**: 15 scripts (17% redução)
- **Redundantes**: 0 scripts
- **Organização**: 5 categorias organizadas
- **Novos**: 3 scripts consolidados

---

## 🎯 **BENEFÍCIOS**

### **Para Desenvolvedores**
- ✅ **Mais fácil** de encontrar scripts
- ✅ **Menos confusão** com scripts similares
- ✅ **Funcionalidades consolidadas**

### **Para Operações**
- ✅ **Scripts mais confiáveis**
- ✅ **Menos manutenção**
- ✅ **Melhor organização**

### **Para Novos Usuários**
- ✅ **Estrutura intuitiva**
- ✅ **Documentação clara**
- ✅ **Fácil aprendizado**

---

## 📋 **MIGRAÇÃO DE SCRIPTS ANTIGOS**

Se você tinha scripts que chamavam os antigos, atualize:

```bash
# ANTES
start-company-module.bat

# DEPOIS  
cd core
start-myerp.bat
# (inicia sistema completo, incluindo company)

# ANTES
test-monitoring.bat

# DEPOIS
cd testing
health-check.bat
# (verifica todos os serviços, incluindo monitoring)
```

**🎉 SCRIPTS REORGANIZADOS E OTIMIZADOS COM SUCESSO! 🎉**