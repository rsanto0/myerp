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
├── ☁️ aws/              # Deploy AWS (CloudFormation, Scripts)
├── 📚 docs/             # Documentação completa
├── 🚀 dev-scripts/      # Scripts de desenvolvimento
├── 📡 postman/          # Collections API
└── 🔗 shared/           # Bibliotecas compartilhadas
```

## 🎯 **Módulos Implementados**

| Módulo | Porta | Status | Descrição |
|--------|-------|--------|-----------|
| **Eureka Server** | 8761 | ✅ | Service Discovery |
| **API Gateway** | 8080 | ✅ | Gateway principal |
| **Auth Service** | 8081 | ✅ | Autenticação JWT |
| **RH Module** | 8082 | ✅ | Recursos Humanos |
| **Biometria Module** | 8083 | ✅ | Controle biométrico |
| **Monitoring Module** | 8084 | ✅ | Dashboard visual |

## 🛠️ **Tecnologias**

- **Backend**: Java 17, Spring Boot 3.3.2, Spring Cloud
- **Database**: PostgreSQL 15
- **Security**: JWT, Spring Security
- **Containers**: Docker, Docker Compose
- **Cloud**: AWS (CloudFormation, ECS, RDS, Route 53)
- **Monitoring**: Spring Actuator, Custom Dashboard

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