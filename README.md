# MyERP - Sistema ERP Modular

[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![AWS](https://img.shields.io/badge/AWS-Ready-orange.svg)](https://aws.amazon.com/)

## 🚀 **Início Rápido**

### **Desenvolvimento Local**
```bash
# 1. Iniciar PostgreSQL
dev-scripts/start-postgres.bat

# 2. Iniciar sistema completo
dev-scripts/start-myerp.bat

# 3. Acessar
# - API Gateway: http://localhost:8080
# - Monitoring: http://localhost:8084
```

### **Deploy AWS**
```bash
# 1. Configurar AWS
cd aws/
setup-aws.bat

# 2. Deploy
set AWS_ACCOUNT_ID=123456789012
deploy-aws.bat prod us-east-1 myerp.com
```

## 📁 **Estrutura do Projeto**

```
myErp/
├── 🏗️ infrastructure/     # Serviços base (Gateway, Auth, Eureka)
├── 📦 modules/           # Módulos de negócio (RH, Biometria, etc)
├── 🔗 shared/            # Bibliotecas compartilhadas
│   ├── common-lib/       # DTOs e entidades comuns
│   └── notification-lib/ # Email, SMS, WhatsApp
├── ☁️ aws/              # Deploy AWS (CloudFormation, Scripts)
├── 📚 docs/             # Documentação completa
├── 🚀 dev-scripts/      # Scripts de desenvolvimento
└── 📡 postman/          # Collections API
```

## 🎯 **Módulos Implementados**

| Módulo | Porta | Status | Descrição | Documentação |
|--------|-------|--------|-----------|---------------|
| **Eureka Server** | 8761 | ✅ | Service Discovery | [📋 Summary](infrastructure/service-discovery/SERVICE-DISCOVERY-SUMMARY.md) |
| **API Gateway** | 8080 | ✅ | Gateway principal | [📋 Summary](infrastructure/api-gateway/API-GATEWAY-SUMMARY.md) |
| **Auth Service** | 8081 | ✅ | Autenticação JWT | [📋 Summary](infrastructure/auth-service/AUTH-SERVICE-SUMMARY.md) |
| **Config Server** | 8888 | ✅ | Configurações | [📋 Summary](infrastructure/config-server/CONFIG-SERVER-SUMMARY.md) |
| **RH Module** | 8082 | ✅ | Recursos Humanos | [📋 Summary](modules/rh-module/RH-MODULE-SUMMARY.md) |
| **Biometria Module** | 8083 | ✅ | Controle biométrico | [📋 Summary](modules/biometria-module/BIOMETRIA-MODULE-SUMMARY.md) |
| **Monitoring Module** | 8084 | ✅ | Dashboard visual | [📋 Summary](modules/monitoring-module/MONITORING-MODULE-SUMMARY.md) |
| **Company Module** | 8085 | ✅ | Gestão multi-tenant | [📋 Summary](modules/company-module/COMPANY-MODULE-SUMMARY.md) |
| **Financial Module** | 8086 | ✅ | Gestão financeira | [📋 Summary](modules/financial-module/FINANCIAL-MODULE-SUMMARY.md) |
| **Device Management** | 8087 | ✅ | Gestão de dispositivos | [📋 Summary](modules/device-management-module/DEVICE-MANAGEMENT-SUMMARY.md) |

## 🛠️ **Tecnologias**

- **Backend**: Java 17, Spring Boot 3.3.2, Spring Cloud
- **Database**: PostgreSQL 15
- **Security**: JWT, Spring Security
- **Containers**: Docker, Docker Compose
- **Cloud**: AWS (CloudFormation, ECS, RDS, Route 53)
- **Monitoring**: Spring Actuator, Custom Dashboard

## 🐳 **Containerização**

Todos os módulos possuem **Dockerfile** para deploy independente:

### **Infrastructure**
- `service-discovery/Dockerfile` - Eureka Server (8761)
- `api-gateway/Dockerfile` - Gateway Principal (8080)
- `auth-service/Dockerfile` - Autenticação JWT (8081)
- `config-server/Dockerfile` - Configurações (8888)

### **Modules**
- `rh-module/Dockerfile` - Recursos Humanos (8082)
- `biometria-module/Dockerfile` - Controle Biométrico (8083)
- `monitoring-module/Dockerfile` - Dashboard (8084)
- `company-module/Dockerfile` - Gestão Empresas (8085)
- `financial-module/Dockerfile` - Financeiro (8086)

### **Build & Deploy**
```bash
# Build individual
docker build -t myerp/auth-service:1.0.0 infrastructure/auth-service/

# Build todos os módulos
docker-compose build

# Deploy produção
docker-compose -f docker-compose.prod.yml up -d
```

## 📚 **Documentação**

| Documento | Descrição |
|-----------|-----------|
| [📖 Estrutura do Projeto](docs/ESTRUTURA-PROJETO.md) | Organização completa |
| [🚀 Guia de Inicialização](docs/STARTUP-GUIDE.md) | Como iniciar o sistema |
| [🗄️ Setup PostgreSQL](docs/POSTGRES-SETUP.md) | Configuração do banco |
| [🏗️ Arquitetura do Sistema](docs/SYSTEM-ARCHITECTURE.md) | Visão técnica |
| [🧪 Cenários de Teste](docs/CENARIOS-TESTE-COMPLETO.md) | Testes completos |

## ☁️ **Deploy AWS**

| Arquivo | Descrição |
|---------|-----------|
| [☁️ Passo a Passo AWS](aws/PASSO-A-PASSO.md) | Guia completo AWS |
| [📋 Deploy Completo](aws/README-AWS-DEPLOY.md) | Documentação técnica |
| `setup-aws.bat` | Configuração inicial |
| `deploy-aws.bat` | Deploy automatizado |

## 🔧 **Scripts Disponíveis**

### **Desenvolvimento**
```bash
dev-scripts/start-myerp.bat      # Iniciar sistema completo
dev-scripts/stop-myerp.bat       # Parar sistema
dev-scripts/start-monitoring.bat # Só monitoramento
dev-scripts/test-profiles.bat    # Testar configurações
```

### **PostgreSQL**
```bash
dev-scripts/start-postgres.bat   # Iniciar PostgreSQL
setup-postgres.bat               # Configurar PostgreSQL
connect-db.bat                   # Conectar ao banco
```

## 🧪 **Testes**

### **Collections Postman**
- `postman/MyERP-System.postman_collection.json` - APIs completas
- `postman/MyERP-Tests.postman_collection.json` - Testes automatizados
- `postman/MyERP-Environment.postman_environment.json` - Variáveis

### **Cenários de Teste**
Veja [Cenários Completos](docs/CENARIOS-TESTE-COMPLETO.md)

## 🔐 **Segurança**

- **Autenticação**: JWT com refresh tokens
- **Autorização**: Role-based (ADMIN, FUNCIONARIO)
- **Comunicação**: HTTPS em produção
- **Secrets**: AWS Secrets Manager
- **Configurações**: AWS Parameter Store

## 📊 **Monitoramento**

### **Dashboard Visual**
- **URL**: http://localhost:8084
- **Métricas**: JVM, Sistema, Serviços
- **Health Checks**: Automáticos
- **Logs**: Centralizados

### **Endpoints de Saúde**
- `/actuator/health` - Status geral
- `/actuator/metrics` - Métricas detalhadas
- `/actuator/info` - Informações da aplicação

## 🌍 **Ambientes**

### **Local (Desenvolvimento)**
```bash
# Profile: local
# Database: localhost:5432
# Services: localhost:808X
```

### **Docker (Containers)**
```bash
# Profile: docker
# Database: postgres:5432
# Services: service-name:808X
```

### **AWS (Produção)**
```bash
# Profile: aws
# Database: RDS endpoint
# Services: Route 53 domains
```

## 🔄 **Roadmap**

### **✅ Implementado**
- Arquitetura de microserviços
- Autenticação e autorização
- Módulos RH e Biometria
- Dashboard de monitoramento
- **Notification Library** (Email, SMS, WhatsApp)
- Deploy AWS completo

### **🔄 Em Desenvolvimento**
- Módulos Financeiro e Vendas
- Interface web completa
- Testes automatizados

### **📋 Planejado**
- CI/CD Pipeline
- Kubernetes deployment
- Machine Learning integration

## 🤝 **Contribuição**

Este é um projeto proprietário. Para contribuições:
1. Entre em contato com a equipe
2. Siga os padrões estabelecidos
3. Documente todas as mudanças

## 📄 **Licença**

**Todos os direitos reservados.** Este projeto é proprietário e não possui licença pública.

## 📞 **Suporte**

- **Documentação**: Pasta `docs/`
- **Issues**: Contate a equipe de desenvolvimento
- **Deploy**: Siga os guias em `aws/`

---

**MyERP - Sistema ERP moderno, escalável e pronto para a nuvem! 🚀**