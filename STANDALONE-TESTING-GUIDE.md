# 🧪 GUIA DE TESTES ISOLADOS - MyERP

## ✅ **CONFIRMAÇÃO: TODOS OS MÓDULOS TÊM COLLECTIONS ISOLADAS**

Cada módulo do MyERP possui sua própria collection Postman para testes isolados:

| Módulo | Porta | Collection | Status |
|--------|-------|------------|--------|
| **Eureka Server** | 8761 | `eureka-server.postman_collection.json` | ✅ |
| **Config Server** | 8888 | `config-server.postman_collection.json` | ✅ **NOVO!** |
| **API Gateway** | 8080 | `api-gateway.postman_collection.json` | ✅ |
| **Auth Service** | 8081 | `auth-service.postman_collection.json` | ✅ |
| **RH Module** | 8082 | `rh-module.postman_collection.json` | ✅ |
| **Biometria Module** | 8083 | `biometria-module.postman_collection.json` | ✅ |
| **Monitoring Module** | 8084 | `monitoring-module.postman_collection.json` | ✅ |
| **Company Module** | 8085 | `company-module.postman_collection.json` | ✅ |
| **Financial Module** | 8086 | `financial-module.postman_collection.json` | ✅ |

## 🚀 **COMO TESTAR CADA MÓDULO ISOLADAMENTE**

### **1. Eureka Server (8761)**
```bash
# Iniciar apenas Eureka
cd infrastructure/service-discovery
mvn spring-boot:run

# Testar
- Collection: eureka-server.postman_collection.json
- Endpoints: /eureka/apps, service instances
```

### **2. Config Server (8888)**
```bash
# Iniciar Config Server
cd infrastructure/config-server
mvn spring-boot:run

# Testar
- Collection: config-server.postman_collection.json
- Configurações centralizadas, profiles
```

### **2. Auth Service (8081)**
```bash
# Iniciar Auth Service
cd infrastructure/auth-service
mvn spring-boot:run

# Testar
- Collection: auth-service.postman_collection.json
- Login, validação JWT, criação de usuários
```

### **3. API Gateway (8080)**
```bash
# Iniciar Gateway (requer Auth Service)
cd infrastructure/api-gateway
mvn spring-boot:run

# Testar
- Collection: api-gateway.postman_collection.json
- Roteamento, autenticação, filtros JWT
```

### **4. RH Module (8082)**
```bash
# Iniciar RH Module
cd modules/rh-module
mvn spring-boot:run

# Testar
- Collection: rh-module.postman_collection.json
- Headers X-User-* simulam autenticação
```

### **5. Biometria Module (8083)**
```bash
# Iniciar Biometria Module
cd modules/biometria-module
mvn spring-boot:run

# Testar
- Collection: biometria-module.postman_collection.json
- Câmeras, biometria, simulação
```

### **6. Monitoring Module (8084)**
```bash
# Iniciar Monitoring
cd modules/monitoring-module
mvn spring-boot:run

# Testar
- Collection: monitoring-module.postman_collection.json
- Dashboard, métricas, health checks
```

### **7. Company Module (8085)**
```bash
# Iniciar Company Module
cd modules/company-module
mvn spring-boot:run

# Testar
- Collection: company-module.postman_collection.json
- Multi-tenant, features, banking
```

### **8. Financial Module (8086)**
```bash
# Iniciar Financial Module
cd modules/financial-module
mvn spring-boot:run

# Testar
- Collection: financial-module.postman_collection.json
- Boletos, PIX, conciliação bancária
```

## 📋 **CARACTERÍSTICAS DAS COLLECTIONS**

### **Variáveis Próprias**
Cada collection tem suas próprias variáveis:
- `base_url` específica (localhost:porta)
- `jwt_token` (quando aplicável)
- IDs específicos do módulo

### **Testes Independentes**
- ✅ Não dependem de outros módulos
- ✅ Headers simulados quando necessário
- ✅ Dados de exemplo incluídos
- ✅ Scripts de captura automática

### **Organização por Funcionalidade**
- 🔐 Autenticação
- 👥 Usuários/Funcionários
- 🔧 Administração
- 📊 Dashboard/Métricas
- 🔍 Health Checks

## 🎯 **CENÁRIOS DE TESTE ISOLADO**

### **Desenvolvimento de Nova Feature**
1. Iniciar apenas o módulo específico
2. Usar collection isolada
3. Testar sem interferência de outros serviços

### **Debug de Problemas**
1. Isolar o módulo com problema
2. Testar endpoints específicos
3. Verificar logs isoladamente

### **Validação de Deploy**
1. Testar cada módulo após deploy
2. Verificar health checks
3. Validar funcionalidades core

## 🔧 **SCRIPTS DE INICIALIZAÇÃO ISOLADA**

### **Scripts Disponíveis**
```bash
# Módulos individuais
dev-scripts/start-config-server.bat
dev-scripts/start-company-module.bat
dev-scripts/start-financial-module.bat
dev-scripts/start-monitoring.bat

# Sistema completo
dev-scripts/start-myerp.bat
```

### **Inicialização Manual**
```bash
# Qualquer módulo
cd [caminho-do-modulo]
mvn spring-boot:run -Dspring.profiles.active=local
```

## 📊 **BENEFÍCIOS DOS TESTES ISOLADOS**

### **Para Desenvolvimento**
- ✅ Feedback rápido
- ✅ Testes focados
- ✅ Debug simplificado
- ✅ Desenvolvimento paralelo

### **Para QA**
- ✅ Testes específicos por módulo
- ✅ Validação de contratos de API
- ✅ Cenários de erro isolados
- ✅ Performance individual

### **Para Deploy**
- ✅ Validação pós-deploy
- ✅ Rollback granular
- ✅ Monitoramento específico
- ✅ Troubleshooting direcionado

## 🎉 **CONCLUSÃO**

**SIM, VOCÊ PODE TESTAR CADA MÓDULO ISOLADAMENTE!**

Cada módulo do MyERP foi projetado para:
- ✅ Funcionar independentemente
- ✅ Ter sua própria collection Postman
- ✅ Permitir testes isolados completos
- ✅ Facilitar desenvolvimento e debug

**✅ COMPLETO**: Financial Module (porta 8086) criado com sucesso!

### **💰 Financial Module - NOVO!**
- **Porta**: 8086
- **Collection**: `financial-module.postman_collection.json`
- **Funcionalidades**: Boletos, PIX, Conciliação Bancária
- **Integração**: Company Module para dados bancários
- **Status**: ✅ **PRONTO PARA TESTES**