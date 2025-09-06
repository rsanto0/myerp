# Collections Postman - MyERP

## 📋 Collections Disponíveis

### **MyERP-System.postman_collection.json**
Collection principal com **todos os endpoints** do sistema MyERP:

#### **🔐 1. Auth Service**
- Login Admin/Funcionário (salva JWT automaticamente)
- Validar Token
- Criar Usuário

#### **👥 2. RH Module** 
- Registrar Ponto (Funcionário)
- Consultar Pontos (Funcionário)
- Listar Funcionários (Admin)
- Criar Funcionário (Admin)
- Listar Todos os Pontos (Admin)

#### **📷 3. Biometria Module**
- Simular Detecção Facial
- Listar Eventos de Detecção
- Dashboard Biometria

#### **📊 4. Monitoring Module**
- Dashboard Geral
- Health Check
- Status dos Serviços

### **MyERP-Tests.postman_collection.json**
Collection de **testes automatizados** com validações.

### **MyERP-Environment.postman_environment.json**
Variáveis de ambiente com:
- URLs base
- Tokens JWT (salvos automaticamente)
- IDs de usuários

## 🚀 Como Usar

### **1. Importar no Postman**
```
File → Import → Upload Files
- MyERP-System.postman_collection.json
- MyERP-Environment.postman_environment.json
```

### **2. Configurar Environment**
- Selecione "MyERP Environment" no canto superior direito
- URLs já configuradas para desenvolvimento local

### **3. Fluxo de Teste**
1. **Login Admin** → JWT salvo automaticamente
2. **Criar Funcionário** → Sincroniza com Auth Service
3. **Login Funcionário** → JWT funcionário salvo
4. **Registrar Ponto** → Usa JWT do funcionário
5. **Simular Biometria** → Registra ponto automaticamente

## 🔧 Configurações

### **URLs Base**
- **API Gateway**: http://localhost:8080
- **Biometria**: http://localhost:8083  
- **Monitoring**: http://localhost:8084

### **Autenticação**
- Tokens JWT salvos automaticamente após login
- Headers Authorization configurados automaticamente
- Variáveis de ambiente atualizadas dinamicamente

## ✅ Funcionalidades

- **Scripts automáticos** para captura de JWT
- **Variáveis dinâmicas** (user_id, jwt_token)
- **Endpoints atualizados** para nova arquitetura
- **Validações de resposta** nos testes
- **Fluxos completos** de negócio

## 📝 Notas

- **Collection única**: Todos os módulos em uma collection
- **Arquitetura atual**: Via API Gateway (porta 8080)
- **Sincronização**: Funcionários criados no RH são sincronizados com Auth Service
- **Logs estruturados**: Prefixos [MODULO] nos endpoints

---

**Última atualização:** 2025-09-06  
**Versão:** Arquitetura unificada com Usuario centralizado