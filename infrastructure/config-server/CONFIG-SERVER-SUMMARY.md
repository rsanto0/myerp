# Config Server - MyERP

## 🎭 **Analogia: Departamento de TI Corporativo**

Imagine o **Config Server** como o **Departamento de TI** de uma grande empresa que gerencia todas as configurações e políticas corporativas.

### **🏢 Situação SEM Config Server**
```
🏢 MyERP Company
├── 🏦 Depto Financeiro    → Tem suas próprias regras
├── 👥 Depto RH           → Tem suas próprias regras  
├── 🔐 Depto Segurança    → Tem suas próprias regras
└── 📊 Depto Biometria    → Tem suas próprias regras
```
**Problemas:**
- ❌ Mudança de política? Avisar cada departamento
- ❌ Nova senha do sistema? Cada um atualiza separadamente
- ❌ Horário de funcionamento? Cada um tem sua versão
- ❌ Configuração de rede? Cada departamento configura

### **🏛️ COM Config Server (Centralizado)**
```
🏢 MyERP Company
├── 🖥️ DEPARTAMENTO DE TI (Config Server)
│   ├── 📋 Políticas Corporativas
│   ├── 🔑 Senhas e Credenciais
│   ├── ⏰ Horários Padrão
│   ├── 🌐 Configurações de Rede
│   └── 🔧 Regras por Ambiente
│
├── 🏦 Depto Financeiro    → Consulta o TI
├── 👥 Depto RH           → Consulta o TI
├── 🔐 Depto Segurança    → Consulta o TI
└── 📊 Depto Biometria    → Consulta o TI
```
**Vantagens:**
- ✅ Mudança de política? Atualiza em um lugar só
- ✅ Nova senha? Todos recebem automaticamente
- ✅ Novo horário? Sincronização instantânea
- ✅ Configuração única? Distribuição automática

## 🎯 **Função Principal**

**Centralizador de Configurações e Distribuidor Inteligente**

### **📋 Responsabilidades**
1. **Armazenar Configurações:** Todas as configs em um local
2. **Distribuir por Ambiente:** Dev, Test, Prod diferentes
3. **Refresh Dinâmico:** Mudanças sem restart
4. **Versionamento:** Controle de versões das configs
5. **Segurança:** Criptografia de dados sensíveis

## 🚀 **Funcionalidades Implementadas**

### **✅ Configurações Centralizadas**
- Configurações de todos os 8 módulos
- Profiles por ambiente (local, docker, aws)
- Configurações específicas por serviço
- Configurações globais compartilhadas

### **✅ Refresh Dinâmico**
- Mudanças sem restart dos serviços
- Endpoint `/actuator/refresh`
- Sincronização automática
- Zero downtime para ajustes

### **✅ Integração com Eureka**
- Service discovery automático
- Registro no Eureka Server
- Health checks integrados
- Load balancing de configurações

### **✅ Profiles por Ambiente**
- **Local:** H2, logs DEBUG, simulação
- **Docker:** PostgreSQL containers, logs INFO
- **AWS:** RDS, CloudWatch, Parameter Store

## 🗺️ **Mapa de Configurações**

### **📄 Configurações Globais (application.yml)**
```yaml
# Configurações compartilhadas por todos
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

management:
  endpoints:
    web:
      exposure:
        include: "*"

logging:
  level:
    com.myerp: DEBUG
```

### **🔧 Configurações Específicas**

#### **API Gateway (api-gateway.yml)**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: http://localhost:8081
          predicates:
            - Path=/auth/**
```

#### **Financial Service (financial-service.yml)**
```yaml
financial:
  boleto:
    banco-padrao: "001"
  pix:
    chave-padrao: "empresa@myerp.com"
```

#### **Company Service (company-service.yml)**
```yaml
company:
  multi-tenant:
    enabled: true
  feature-flags:
    biometria-facial: true
```

## 🔄 **Fluxo de Configuração**

### **1️⃣ Inicialização do Serviço**
```
1. Serviço inicia com bootstrap.yml
2. Consulta Config Server (8888)
3. Carrega configurações baseadas no profile
4. Aplica configurações localmente
5. Registra no Eureka
```

### **2️⃣ Refresh Dinâmico**
```
1. Admin atualiza configuração no Config Server
2. Admin chama /actuator/refresh no serviço
3. Serviço recarrega configurações
4. Aplica novas configurações sem restart
5. Logs confirmam atualização
```

### **3️⃣ Profiles por Ambiente**
```
# Desenvolvimento
GET /financial-service/local

# Docker
GET /financial-service/docker  

# Produção AWS
GET /financial-service/aws
```

## ⚙️ **Como Usar**

### **1. Executar Config Server**
```bash
# Via script
dev-scripts/core/start-myerp.bat

# Manual
cd infrastructure/config-server
mvn spring-boot:run
```

### **2. Endpoints Disponíveis**
- **Config Server:** http://localhost:8888
- **Configurações:** http://localhost:8888/{service}/{profile}
- **Health:** http://localhost:8888/actuator/health
- **Refresh:** http://localhost:8888/actuator/refresh

### **3. Testar Configurações**
```bash
# Configurações globais
GET http://localhost:8888/application/default

# Financial Module - local
GET http://localhost:8888/financial-service/local

# Company Module - AWS
GET http://localhost:8888/company-service/aws
```

## 🔗 **Integração com Módulos**

### **Como os Módulos Consomem**
Cada módulo tem um `bootstrap.yml`:
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

### **Ordem de Inicialização**
```
1. Config Server (8888)    ← PRIMEIRO!
2. Eureka Server (8761)
3. Auth Service (8081)
4. Demais módulos...
```

## 📊 **Configurações por Módulo**

| Módulo | Configurações Principais |
|--------|-------------------------|
| **API Gateway** | Rotas, filtros JWT, timeouts |
| **Auth Service** | JWT secret, expiração, algoritmo |
| **RH Module** | Horários, tolerâncias, validações |
| **Biometria Module** | Câmeras, confiança, simulação |
| **Company Module** | Multi-tenant, feature flags |
| **Financial Module** | Boletos, PIX, conciliação |
| **Monitoring Module** | Alertas, métricas, thresholds |

## 🧪 **Testes com Postman**

### **Collection Completa**
- ✅ Configurações por serviço
- ✅ Profiles por ambiente  
- ✅ Health checks
- ✅ Refresh dinâmico
- ✅ Validação de configs

### **Cenários de Teste**
1. **Obter configurações globais**
2. **Testar configurações específicas**
3. **Validar profiles diferentes**
4. **Testar refresh dinâmico**
5. **Verificar health checks**

## 🎯 **Benefícios**

### **🎯 Centralização**
- ✅ Uma fonte única de configurações
- ✅ Consistência entre ambientes
- ✅ Versionamento centralizado
- ✅ Auditoria de mudanças

### **⚡ Flexibilidade**
- ✅ Configurações por ambiente
- ✅ Configurações por serviço
- ✅ Refresh sem downtime
- ✅ Feature flags dinâmicas

### **🔒 Segurança**
- ✅ Configurações sensíveis centralizadas
- ✅ Controle de acesso
- ✅ Criptografia (planejado)
- ✅ Auditoria de acessos

## 🚀 **Próximos Passos**

### **🔄 Melhorias Planejadas**
1. **Git Backend:** Configurações versionadas
2. **Encryption:** Dados sensíveis criptografados
3. **Web UI:** Interface de gerenciamento
4. **Webhooks:** Notificações de mudanças
5. **Backup:** Estratégia de backup das configurações

### **🌟 Funcionalidades Avançadas**
1. **Config Templates:** Templates reutilizáveis
2. **Validation:** Validação de configurações
3. **Rollback:** Voltar versões anteriores
4. **Monitoring:** Métricas de uso de configs

## 🛠️ **Troubleshooting**

### **Problema: Serviço não encontra Config Server**
```bash
# Verificar se Config Server está rodando
curl http://localhost:8888/actuator/health

# Verificar bootstrap.yml do serviço
spring.cloud.config.uri: http://localhost:8888
```

### **Problema: Configurações não atualizando**
```bash
# Forçar refresh
POST http://localhost:8082/actuator/refresh

# Verificar logs do serviço
[CONFIG] Refreshing configuration...
```

### **Problema: Profile não encontrado**
```bash
# Verificar se arquivo existe
config-repo/financial-service-aws.yml

# Testar endpoint
GET http://localhost:8888/financial-service/aws
```

## 🎉 **Conclusão**

O **Config Server** está **100% funcional** e oferece:

- ✅ **Centralização** de todas as configurações
- ✅ **Gestão de ambientes** (local, docker, aws)
- ✅ **Refresh dinâmico** sem downtime
- ✅ **Integração perfeita** com todos os 8 módulos
- ✅ **Testes completos** via Postman
- ✅ **Preparado para produção**

### **🎯 Analogia Final**

**Config Server** = **Departamento de TI Corporativo**
- **Centraliza todas as políticas** da empresa
- **Distribui configurações** para todos os departamentos
- **Atualiza em tempo real** sem parar operações
- **Garante consistência** em todos os ambientes
- **Controla acesso** e mantém segurança

**Sem o TI, cada departamento faria suas próprias regras - seria um caos! 🖥️✨**

---

**MyERP Config Server - O cérebro das configurações! ⚙️🚀**