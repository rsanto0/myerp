# 🏢 MyERP - Sistema Empresarial Moderno (Em construção ...)

## 🎭 **Analogia: Prédio Empresarial Inteligente**

Imagine o **MyERP** como um **moderno prédio empresarial** onde cada departamento trabalha de forma independente, mas todos se comunicam perfeitamente através de um sistema inteligente.

```
🏢 PRÉDIO MyERP - ARQUITETURA COMPLETA
├── 📋 RECEPÇÃO (Eureka Server - 8761)
│   └── "Quem está onde e como encontrar"
├── 🔐 PORTARIA (API Gateway - 8080) 
│   └── "Controle de acesso e segurança"
├── 🛡️ SEGURANÇA (Auth Service - 8081)
│   └── "Autenticação e autorização"
├── 👥 RH (RH Module - 8082)
│   └── "Gestão de pessoas e ponto"
├── 🏢 ADMINISTRAÇÃO (Company Module - 8083)
│   └── "Gestão de empresas"
├── 📊 SUPERVISÃO (Monitoring Module - 8084)
│   └── "Monitoramento do sistema"
├── 💰 FINANCEIRO (Financial Module - 8085)
│   └── "Gestão financeira"
├── 📱 BIOMETRIA (Biometria Module - 8086)
│   └── "Controle biométrico"
└── 🔧 MANUTENÇÃO (Device Management - 8087)
    └── "Gestão de dispositivos"
```

## 🚀 **Visão Geral do Sistema**

### **📊 Estatísticas do Projeto**
- **🏗️ Infraestrutura**: 4 serviços (Gateway, Auth, Discovery, Config)
- **📦 Módulos de Negócio**: 6 módulos funcionais
- **📚 Bibliotecas Compartilhadas**: 2 libs (Common, Notification)
- **🐳 Containerização**: Docker completo
- **☁️ Cloud Ready**: Deploy AWS automatizado
- **🧪 Testes**: Collections Postman para todos os módulos

### **🎯 Tecnologias Principais**
- **Backend**: Java 17 + Spring Boot 3.3.2
- **Microserviços**: Spring Cloud 2023.0.3
- **Banco de Dados**: PostgreSQL + H2 (dev)
- **Segurança**: JWT + Spring Security
- **Containerização**: Docker + Docker Compose
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway

## 🏗️ **Arquitetura de Microserviços**

### **🔧 Infraestrutura (Infrastructure)**
| Serviço | Porta | Função | Documentação |
|---------|-------|--------|--------------|
| **Service Discovery** | 8761 | Registro e descoberta de serviços | [📋 Ver Detalhes](infrastructure/service-discovery/SERVICE-DISCOVERY-SUMMARY.md) |
| **API Gateway** | 8080 | Roteamento e autenticação | [🔐 Ver Detalhes](infrastructure/api-gateway/API-GATEWAY-SUMMARY.md) |
| **Auth Service** | 8081 | Autenticação JWT | [🛡️ Ver Detalhes](infrastructure/auth-service/AUTH-SERVICE-SUMMARY.md) |
| **Config Server** | 8888 | Configuração centralizada | [⚙️ Ver Detalhes](infrastructure/config-server/CONFIG-SERVER-SUMMARY.md) |

### **📦 Módulos de Negócio (Business Modules)**
| Módulo | Porta | Função | Documentação |
|--------|-------|--------|--------------|
| **RH Module** | 8082 | Gestão de pessoas e ponto | [👥 Ver Detalhes](modules/rh-module/RH-MODULE-SUMMARY.md) |
| **Company Module** | 8083 | Gestão de empresas | [🏢 Ver Detalhes](modules/company-module/COMPANY-MODULE-SUMMARY.md) |
| **Monitoring Module** | 8084 | Monitoramento do sistema | [📊 Ver Detalhes](modules/monitoring-module/MONITORING-MODULE-SUMMARY.md) |
| **Financial Module** | 8085 | Gestão financeira | [💰 Ver Detalhes](modules/financial-module/FINANCIAL-MODULE-SUMMARY.md) |
| **Biometria Module** | 8086 | Controle biométrico | [📱 Ver Detalhes](modules/biometria-module/) |
| **Device Management** | 8087 | Gestão de dispositivos | [🔧 Ver Detalhes](modules/device-management-module/DEVICE-MANAGEMENT-SUMMARY.md) |

### **📚 Bibliotecas Compartilhadas (Shared Libraries)**
| Biblioteca | Função | Documentação |
|------------|--------|--------------|
| **Common Lib** | Utilitários e DTOs compartilhados | [🔧 Ver Detalhes](shared/common-lib/) |
| **Notification Lib** | Sistema de notificações | [📢 Ver Detalhes](shared/notification-lib/NOTIFICATION-LIB-SUMMARY.md) |

## 🚀 **Quick Start**

### **1️⃣ Pré-requisitos**
```bash
# Ferramentas necessárias
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL (ou usar Docker)
```

### **2️⃣ Inicialização Rápida**
```bash
# 1. Clonar repositório
git clone <repository-url>
cd myErp

# 2. Iniciar banco de dados
cd dev-scripts/database
criar-container-postgres-docker.bat

# 3. Iniciar sistema completo
cd ../
myerp.bat
```

### **3️⃣ Verificar Sistema**
```bash
# Eureka Dashboard
http://localhost:8761

# API Gateway
http://localhost:8080

# Health Checks
http://localhost:8080/actuator/health
```

## 🔄 **Fluxo de Comunicação**

### **📡 Como os Serviços se Comunicam**
```
1. 🌐 Cliente → API Gateway (8080)
2. 🔐 Gateway → Auth Service (8081) [validar JWT]
3. 📋 Gateway → Eureka (8761) [descobrir serviço]
4. 🎯 Gateway → Módulo Específico [rotear requisição]
5. 📊 Módulo → Outros Módulos [comunicação interna]
```

### **🔒 Segurança Integrada**
- **JWT Tokens** para autenticação
- **Headers automáticos** injetados pelo Gateway
- **Controle de roles** (FUNCIONARIO/ADMIN)
- **Validação centralizada** no Auth Service

## 🧪 **Testes e Desenvolvimento**

### **📋 Collections Postman**
Cada módulo possui sua collection Postman completa:
- [🔐 API Gateway Collection](infrastructure/api-gateway/api-gateway-postman.json)
- [🛡️ Auth Service Collection](infrastructure/auth-service/auth-service-postman.json)
- [👥 RH Module Collection](modules/rh-module/rh-module.postman_collection.json)
- [🏢 Company Module Collection](modules/company-module/company-module.postman_collection.json)
- [💰 Financial Module Collection](modules/financial-module/financial-module.postman_collection.json)
- [📊 Monitoring Module Collection](modules/monitoring-module/monitoring-module.postman_collection.json)

### **🔧 Scripts de Desenvolvimento**
```bash
# Localização: dev-scripts/
├── 🚀 myerp.bat                    # Iniciar sistema completo
├── 🗄️ database/                    # Scripts de banco
├── 🧪 postman/                     # Collections organizadas
└── 📖 README.md                    # Guia dos scripts
```

## 🐳 **Containerização**

### **🔧 Docker Compose**
```bash
# Desenvolvimento
docker-compose -f dev-scripts/docker-compose.yml up

# Produção
docker-compose -f dev-scripts/docker-compose.prod.yml up
```

### **📦 Imagens Docker**
Cada módulo possui seu próprio `Dockerfile` otimizado para produção.

## ☁️ **Deploy AWS**

### **🚀 Deploy Automatizado**
```bash
# Setup inicial AWS
cd dev-scripts/aws
setup-aws.bat

# Deploy completo
deploy-aws.bat
```

### **🏗️ Infraestrutura AWS**
- **ECS Fargate** para containers
- **Application Load Balancer** para roteamento
- **RDS PostgreSQL** para banco de dados
- **CloudWatch** para logs e métricas

## 📊 **Monitoramento**

### **📈 Métricas Disponíveis**
- **Health Checks** em todos os serviços
- **Eureka Dashboard** para status dos serviços
- **Logs estruturados** com prefixos organizados
- **Monitoring Module** para métricas customizadas

### **🔍 Observabilidade**
```bash
# Endpoints de monitoramento
/actuator/health     # Status do serviço
/actuator/info       # Informações do serviço
/actuator/metrics    # Métricas detalhadas
```

## 🎯 **Casos de Uso Principais**

### **👥 Gestão de RH**
- ✅ Cadastro de funcionários
- ✅ Controle de ponto automático
- ✅ Integração com biometria
- ✅ Relatórios de presença

### **🏢 Gestão Empresarial**
- ✅ Cadastro de empresas
- ✅ Gestão de departamentos
- ✅ Controle de acesso por empresa

### **💰 Gestão Financeira**
- ✅ Controle de receitas/despesas
- ✅ Relatórios financeiros
- ✅ Integração com outros módulos

### **📱 Controle Biométrico**
- ✅ Detecção facial automática
- ✅ Registro de ponto por biometria
- ✅ Interface web para configuração

## 🔧 **Configuração Avançada**

### **⚙️ Profiles Disponíveis**
- **local**: Desenvolvimento local (H2 database)
- **docker**: Execução em containers
- **aws**: Deploy em produção AWS

### **🗄️ Bancos de Dados Suportados**
- **PostgreSQL** (produção)
- **H2** (desenvolvimento)
- **MySQL** (configurável)
- **Oracle** (configurável)

## 🚀 **Roadmap**

### **🔄 Próximas Funcionalidades**
1. **Service Mesh** (Istio) para comunicação avançada
2. **Event Sourcing** para auditoria completa
3. **CQRS** para separação de leitura/escrita
4. **GraphQL** API para frontend
5. **Kubernetes** deployment

### **🌟 Melhorias Planejadas**
1. **Dashboard Web** unificado
2. **Mobile App** para funcionários
3. **Relatórios PDF** automatizados
4. **Integração ERP** externa
5. **Machine Learning** para análises

## 🤝 **Contribuição**

### **📋 Como Contribuir**
1. Fork o projeto
2. Crie uma branch para sua feature
3. Implemente seguindo os padrões existentes
4. Adicione testes e documentação
5. Abra um Pull Request

### **📖 Padrões do Projeto**
- **Logs estruturados** com prefixos
- **Collections Postman** para cada módulo
- **Documentação detalhada** em cada serviço
- **Docker** para todos os componentes

## 📞 **Suporte**

### **🔧 Troubleshooting**
- [📋 Service Discovery Issues](infrastructure/service-discovery/SERVICE-DISCOVERY-SUMMARY.md#troubleshooting)
- [🔐 Gateway Problems](infrastructure/api-gateway/API-GATEWAY-SUMMARY.md#troubleshooting)
- [🛡️ Auth Service Issues](infrastructure/auth-service/AUTH-SERVICE-SUMMARY.md#troubleshooting)

### **📚 Documentação Adicional**
- [🚀 Scripts de Desenvolvimento](dev-scripts/README.md)
- [🧪 Guia de Testes Postman](dev-scripts/postman/README.md)

## 🎉 **Conclusão**

O **MyERP** é um sistema empresarial moderno e completo que oferece:

- ✅ **Arquitetura de microserviços** robusta e escalável
- ✅ **Segurança integrada** com JWT e controle de roles
- ✅ **Monitoramento completo** com métricas e logs
- ✅ **Deploy automatizado** para AWS
- ✅ **Testes abrangentes** com Postman
- ✅ **Documentação detalhada** para cada componente

### **🎯 Analogia Final**

**MyERP** = **Prédio Empresarial Inteligente**
- **Cada departamento** trabalha independentemente
- **Sistema de comunicação** conecta todos
- **Segurança centralizada** controla acessos
- **Monitoramento contínuo** garante funcionamento
- **Escalabilidade** para crescer conforme necessário

