# Service Discovery (Eureka Server) - MyERP

## 🎭 **Analogia: Recepção de um Prédio Empresarial**

Imagine o **Eureka Server** como a **recepção principal** de um grande prédio empresarial onde funcionam várias empresas (microserviços).

### **🏢 Situação SEM Service Discovery**
```
🏢 Prédio MyERP (sem recepção)
├── 🏦 Sala 8081 - Financeiro    → "Onde fica o RH?"
├── 👥 Sala 8082 - RH           → "Onde fica a Biometria?"  
├── 📊 Sala 8083 - Biometria    → "Onde fica o Financeiro?"
└── 🔐 Sala 8080 - Portaria     → "Não sei onde ninguém está!"
```
**Problemas:**
- ❌ Ninguém sabe onde os outros estão
- ❌ Se alguém muda de sala, ninguém fica sabendo
- ❌ Visitantes ficam perdidos procurando departamentos
- ❌ Comunicação entre departamentos é difícil

### **🏛️ COM Service Discovery (Recepção Organizada)**
```
🏢 Prédio MyERP
├── 📋 RECEPÇÃO (Eureka Server - 8761)
│   ├── 📋 Lista Atualizada de Departamentos:
│   ├── 🏦 Financeiro → Sala 8081 ✅ Online
│   ├── 👥 RH → Sala 8082 ✅ Online
│   ├── 📊 Biometria → Sala 8083 ✅ Online
│   ├── 🔐 Portaria → Sala 8080 ✅ Online
│   └── 📊 Monitoramento → Sala 8084 ❌ Offline
│
├── 🏦 Financeiro (8081)    → "Preciso falar com RH"
│   └── 📞 Liga para Recepção: "RH está na sala 8082"
│
├── 👥 RH (8082)           → "Preciso falar com Biometria"  
│   └── 📞 Liga para Recepção: "Biometria está na sala 8083"
│
└── 🔐 Portaria (8080)     → "Onde está o Financeiro?"
    └── 📞 Liga para Recepção: "Financeiro está na sala 8081"
```
**Vantagens:**
- ✅ **Lista centralizada** de todos os departamentos
- ✅ **Status em tempo real** (online/offline)
- ✅ **Localização automática** de serviços
- ✅ **Atualizações dinâmicas** quando alguém muda

## 🎯 **Função Principal**

**Registro e Descoberta Automática de Serviços**

### **📋 Responsabilidades**
1. **Registrar Serviços:** Quando um serviço inicia, se registra no Eureka
2. **Descobrir Serviços:** Outros serviços consultam onde encontrar dependências
3. **Health Check:** Monitora se serviços estão online/offline
4. **Load Balancing:** Distribui carga entre múltiplas instâncias
5. **Failover:** Remove serviços offline da lista automaticamente

## 🚀 **Funcionalidades Implementadas**

### **✅ Registro Automático**
- Todos os 8 módulos se registram automaticamente
- Informações de host, porta e status
- Metadata personalizada por serviço
- Renovação automática de registro (heartbeat)

### **✅ Descoberta de Serviços**
- API Gateway descobre onde estão os módulos
- Módulos descobrem dependências (ex: Financial → Company)
- Balanceamento de carga automático
- Cache local para performance

### **✅ Dashboard Visual**
- Interface web em http://localhost:8761
- Status de todos os serviços em tempo real
- Informações de saúde e uptime
- Histórico de registros e desregistros

### **✅ Health Monitoring**
- Heartbeat a cada 30 segundos
- Remove serviços offline automaticamente
- Alertas quando serviços ficam indisponíveis
- Métricas de disponibilidade

## 🗺️ **Mapa de Serviços Registrados**

### **📊 Status Atual dos Serviços**
```
📋 EUREKA DASHBOARD (http://localhost:8761)

🟢 API-GATEWAY (1 instância)
   └── http://localhost:8080 - UP

🟢 AUTH-SERVICE (1 instância)  
   └── http://localhost:8081 - UP

🟢 RH-MODULE (1 instância)
   └── http://localhost:8082 - UP

🟢 BIOMETRIA-MODULE (1 instância)
   └── http://localhost:8083 - UP

🟢 MONITORING-MODULE (1 instância)
   └── http://localhost:8084 - UP

🟢 COMPANY-MODULE (1 instância)
   └── http://localhost:8085 - UP

🟢 FINANCIAL-MODULE (1 instância)
   └── http://localhost:8086 - UP

🔴 CONFIG-SERVER (0 instâncias)
   └── Não registrado (opcional)
```

## 🔄 **Fluxo de Service Discovery**

### **1️⃣ Registro de Serviço**
```
1. Financial Module inicia na porta 8086
2. Financial Module → Eureka: "Estou online na 8086"
3. Eureka registra: FINANCIAL-SERVICE → localhost:8086
4. Eureka responde: "Registrado com sucesso"
5. Financial Module envia heartbeat a cada 30s
```

### **2️⃣ Descoberta de Serviço**
```
1. Financial Module precisa chamar Company Module
2. Financial Module → Eureka: "Onde está COMPANY-SERVICE?"
3. Eureka responde: "localhost:8085"
4. Financial Module chama: http://localhost:8085/api/companies/1
5. Comunicação estabelecida com sucesso
```

### **3️⃣ Failover Automático**
```
1. Company Module para de responder (crash)
2. Eureka não recebe heartbeat por 90s
3. Eureka remove COMPANY-SERVICE da lista
4. Financial Module → Eureka: "Onde está COMPANY-SERVICE?"
5. Eureka responde: "Serviço indisponível"
6. Financial Module ativa fallback/circuit breaker
```

## ⚙️ **Como Usar**

### **1. Executar Eureka Server**
```bash
# Via script (PRIMEIRO serviço a iniciar!)
dev-scripts/core/start-myerp.bat

# Manual
cd infrastructure/service-discovery
mvn spring-boot:run
```

### **2. Acessar Dashboard**
- **Eureka Dashboard:** http://localhost:8761
- **Aplicações Registradas:** Lista em tempo real
- **Health Status:** Status de cada serviço
- **Instance Info:** Detalhes de cada instância

### **3. APIs Disponíveis**
```bash
# Listar todas as aplicações
GET http://localhost:8761/eureka/apps

# Informações de uma aplicação específica
GET http://localhost:8761/eureka/apps/FINANCIAL-SERVICE

# Health check do próprio Eureka
GET http://localhost:8761/actuator/health
```

## 🔗 **Integração com Módulos**

### **Como os Módulos se Registram**
Cada módulo tem configuração Eureka no `application.yml`:
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
```

### **Ordem de Inicialização CRÍTICA**
```
1. Eureka Server (8761)    ← PRIMEIRO SEMPRE!
2. Config Server (8888)    ← Segundo (se usado)
3. Auth Service (8081)     ← Terceiro
4. Demais módulos...       ← Qualquer ordem
```

## 📊 **Serviços por Categoria**

### **🏗️ Infrastructure Services**
| Serviço | Nome Eureka | Porta | Status |
|---------|-------------|-------|--------|
| **Eureka Server** | N/A (é o próprio) | 8761 | ✅ |
| **API Gateway** | API-GATEWAY | 8080 | ✅ |
| **Auth Service** | AUTH-SERVICE | 8081 | ✅ |

### **📦 Business Modules**
| Serviço | Nome Eureka | Porta | Status |
|---------|-------------|-------|--------|
| **RH Module** | RH-MODULE | 8082 | ✅ |
| **Biometria Module** | BIOMETRIA-MODULE | 8083 | ✅ |
| **Monitoring Module** | MONITORING-MODULE | 8084 | ✅ |
| **Company Module** | COMPANY-MODULE | 8085 | ✅ |
| **Financial Module** | FINANCIAL-MODULE | 8086 | ✅ |

## 🧪 **Testes com Postman**

### **Collection Completa**
- ✅ Listar todas as aplicações registradas
- ✅ Verificar status de serviços específicos
- ✅ Health checks do Eureka
- ✅ Métricas de registro/desregistro
- ✅ Testes de descoberta de serviços

### **Cenários de Teste**
1. **Verificar serviços registrados**
2. **Testar descoberta de serviços**
3. **Simular falha de serviço**
4. **Verificar failover automático**
5. **Monitorar health checks**

## 🎯 **Benefícios**

### **🎯 Descoberta Automática**
- ✅ Serviços se encontram automaticamente
- ✅ Não precisa hardcoded IPs/portas
- ✅ Configuração dinâmica
- ✅ Ambiente agnóstico

### **⚡ Alta Disponibilidade**
- ✅ Failover automático
- ✅ Load balancing integrado
- ✅ Health monitoring contínuo
- ✅ Recuperação automática

### **🔒 Simplicidade**
- ✅ Zero configuração manual
- ✅ Registro automático
- ✅ Dashboard visual
- ✅ APIs REST simples

## 🛠️ **Troubleshooting**

### **Problema: Serviço não aparece no Eureka**
```bash
# Verificar se Eureka está rodando
curl http://localhost:8761/actuator/health

# Verificar configuração do serviço
eureka.client.service-url.defaultZone: http://localhost:8761/eureka

# Verificar logs do serviço
[EUREKA] Registering service with Eureka Server...
```

### **Problema: Serviço aparece como DOWN**
```bash
# Verificar health check do serviço
curl http://localhost:8082/actuator/health

# Verificar heartbeat
[EUREKA] Heartbeat sent to Eureka Server

# Verificar conectividade
ping localhost
```

### **Problema: Discovery não funciona**
```bash
# Verificar se fetch-registry está true
eureka.client.fetch-registry: true

# Verificar cache local
[EUREKA] Fetching registry information from server

# Forçar refresh
POST http://localhost:8082/actuator/refresh
```

## 🚀 **Próximos Passos**

### **🔄 Melhorias Planejadas**
1. **Eureka Cluster:** Múltiplas instâncias para HA
2. **Security:** Autenticação para registro
3. **Metrics:** Métricas avançadas de descoberta
4. **Zones:** Suporte a múltiplas zonas AWS
5. **Custom Metadata:** Informações personalizadas

### **🌟 Funcionalidades Avançadas**
1. **Service Mesh:** Integração com Istio
2. **Circuit Breaker:** Hystrix integration
3. **Tracing:** Distributed tracing
4. **Alerting:** Notificações de falhas

## 🎉 **Conclusão**

O **Eureka Server** está **100% funcional** e oferece:

- ✅ **Descoberta automática** de todos os 8 módulos
- ✅ **Dashboard visual** em tempo real
- ✅ **Health monitoring** contínuo
- ✅ **Failover automático** para alta disponibilidade
- ✅ **Load balancing** integrado
- ✅ **Zero configuração manual**

### **🎯 Analogia Final**

**Eureka Server** = **Recepção do Prédio Empresarial**
- **Sabe onde todos estão** em tempo real
- **Direciona visitantes** para o departamento certo
- **Monitora quem está presente** (online/offline)
- **Atualiza informações** automaticamente
- **Facilita comunicação** entre departamentos

**Sem a recepção, seria um caos - ninguém acharia ninguém! 📋✨**

---

**MyERP Service Discovery - O coração da comunicação entre serviços! 🔍🚀**