# Config Server - MyERP

## ⚙️ Funcionalidades

### ✅ Implementado
- **Configurações Centralizadas** para todos os 8 módulos
- **Profiles por Ambiente** (local, docker, aws)
- **Configurações Específicas** por serviço
- **Refresh Dinâmico** sem restart
- **Integração Eureka** para service discovery
- **Health Checks** e monitoramento

### 🔄 Planejado
- Configurações via Git repository
- Criptografia de configurações sensíveis
- Versionamento de configurações
- Interface web de gerenciamento

## 🚀 Como usar

### 1. Executar o Config Server
```bash
# Script dedicado
dev-scripts/start-config-server.bat

# Ou manual
cd infrastructure/config-server
mvn spring-boot:run
```

### 2. Endpoints Disponíveis
- **Config Server**: http://localhost:8888
- **Configurações**: http://localhost:8888/{service}/{profile}
- **Health**: http://localhost:8888/actuator/health
- **Refresh**: http://localhost:8888/actuator/refresh

### 3. Testar com Postman
- **Collection**: `config-server.postman_collection.json`
- **Testes isolados** de todas as configurações
- **Profiles diferentes** por ambiente

## 📋 Configurações Disponíveis

### **Configurações Globais (application.yml)**
- Eureka Client comum
- Management endpoints
- Logging padrão
- JWT compartilhado
- Database padrão

### **API Gateway (api-gateway.yml)**
- Rotas para todos os módulos
- Configurações de filtros JWT
- Timeouts e retry
- Logs específicos

### **Financial Service (financial-service.yml)**
- Configurações de boletos
- Parâmetros PIX
- Conciliação bancária
- Integração Company Module

### **Company Service (company-service.yml)**
- Multi-tenant settings
- Feature flags globais
- Grupos de manutenção
- Configurações bancárias

### **Biometria Service (biometria-service.yml)**
- Configurações de câmera
- Parâmetros biométricos
- Detecção automática
- Simulação de testes

## 🔧 Configuração por Ambiente

### **Local (default)**
- H2 em memória
- Logs DEBUG
- Simulação habilitada
- Eureka local

### **Docker**
- PostgreSQL containers
- Logs INFO
- Service discovery via container names
- Configurações de rede

### **AWS**
- RDS PostgreSQL
- CloudWatch logs
- Parameter Store integration
- Secrets Manager

## 📊 Exemplos de Uso

### **Obter Configurações**
```bash
# Configurações globais
GET http://localhost:8888/application/default

# Financial Module - ambiente local
GET http://localhost:8888/financial-service/local

# Company Module - ambiente AWS
GET http://localhost:8888/company-service/aws
```

### **Refresh Dinâmico**
```bash
# Atualizar configurações sem restart
POST http://localhost:8888/actuator/refresh
```

## 🔗 Integração com Módulos

### **Como os Módulos Consomem**
1. Módulo inicia com `bootstrap.yml`
2. Consulta Config Server na porta 8888
3. Carrega configurações baseadas no profile ativo
4. Pode fazer refresh via `/actuator/refresh`

### **Exemplo bootstrap.yml**
```yaml
spring:
  application:
    name: financial-service
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
  profiles:
    active: local
```

## 🎯 Benefícios

### **Centralização**
- ✅ Uma fonte única de configurações
- ✅ Consistência entre ambientes
- ✅ Versionamento centralizado
- ✅ Auditoria de mudanças

### **Flexibilidade**
- ✅ Configurações por ambiente
- ✅ Configurações por serviço
- ✅ Refresh sem downtime
- ✅ Feature flags dinâmicas

### **Segurança**
- ✅ Configurações sensíveis centralizadas
- ✅ Controle de acesso
- ✅ Criptografia (planejado)
- ✅ Auditoria de acessos

## 🧪 Testes Isolados

### **Collection Postman**
- ✅ **Configurações por serviço**
- ✅ **Profiles por ambiente**
- ✅ **Health checks**
- ✅ **Refresh dinâmico**

### **Cenários de Teste**
1. **Obter configurações globais**
2. **Testar configurações específicas**
3. **Validar profiles diferentes**
4. **Testar refresh dinâmico**
5. **Verificar health checks**

## 🚀 Próximos Passos

1. **Git Backend**: Configurações versionadas
2. **Encryption**: Dados sensíveis criptografados
3. **Web UI**: Interface de gerenciamento
4. **Webhooks**: Notificações de mudanças
5. **Backup**: Estratégia de backup das configurações

## 🎉 Conclusão

O **Config Server** está **100% funcional** e pronto para:
- ✅ **Centralizar configurações** de todos os 8 módulos
- ✅ **Gerenciar profiles** por ambiente
- ✅ **Refresh dinâmico** sem downtime
- ✅ **Testes isolados** completos
- ✅ **Integração Eureka** para descoberta

**O sistema MyERP agora tem configurações centralizadas e gerenciadas! ⚙️🚀**