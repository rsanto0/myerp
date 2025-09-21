# ☁️ Deploy AWS - MyERP

## 🚀 **Status Atual**

⚠️ **EM DESENVOLVIMENTO** - Scripts de deploy AWS estão sendo implementados

## 📋 **Scripts Disponíveis**

### **setup-aws.bat**
Configuração inicial do ambiente AWS:
```bash
cd aws/
setup-aws.bat
```
- Configura AWS CLI
- Define credenciais
- Testa conexão
- Obtém Account ID

### **deploy-aws.bat**
Deploy da aplicação (em desenvolvimento):
```bash
set AWS_ACCOUNT_ID=123456789012
deploy-aws.bat prod us-east-1 myerp.com
```

### **rollback-aws.bat**
Rollback seguro (em desenvolvimento):
```bash
rollback-aws.bat prod us-east-1
```

## 🏗️ **Arquitetura Planejada**

### **Componentes AWS**
- **ECS Fargate** - Containers dos 8 módulos
- **RDS PostgreSQL** - Banco de dados
- **Application Load Balancer** - Roteamento
- **Route 53** - DNS (opcional)
- **CloudFormation** - Infrastructure as Code

### **Módulos para Deploy**
| Módulo | Porta | Container |
|--------|-------|-----------|
| Eureka Server | 8761 | `myerp/eureka-server` |
| API Gateway | 8080 | `myerp/api-gateway` |
| Auth Service | 8081 | `myerp/auth-service` |
| RH Module | 8082 | `myerp/rh-module` |
| Biometria Module | 8083 | `myerp/biometria-module` |
| Monitoring Module | 8084 | `myerp/monitoring-module` |
| Company Module | 8085 | `myerp/company-module` |
| Financial Module | 8086 | `myerp/financial-module` |

## 💰 **Custos Estimados**

### **Ambiente Produção (us-east-1)**
| Recurso | Especificação | Custo/Mês |
|---------|---------------|-----------|
| ECS Tasks | 8 serviços (0.25 vCPU, 0.5GB) | ~$30 |
| RDS PostgreSQL | db.t3.micro | ~$15 |
| Application Load Balancer | 1 ALB | ~$20 |
| Route 53 | Hosted Zone | ~$5 |
| **Total Estimado** | | **~$70/mês** |

## 🔧 **Pré-requisitos**

### **Ferramentas Necessárias**
- AWS CLI instalado
- Docker instalado
- Conta AWS com permissões administrativas

### **Credenciais AWS**
```bash
AWS_ACCESS_KEY_ID=AKIA...
AWS_SECRET_ACCESS_KEY=...
AWS_DEFAULT_REGION=us-east-1
```

## 📊 **Como Usar (Quando Pronto)**

### **1. Setup Inicial**
```bash
cd aws/
setup-aws.bat
```

### **2. Deploy**
```bash
set AWS_ACCOUNT_ID=123456789012
deploy-aws.bat prod us-east-1 myerp.com
```

### **3. Verificar**
```bash
# URLs esperadas:
# https://api.myerp.com
# https://app.myerp.com
```

### **4. Rollback (se necessário)**
```bash
rollback-aws.bat prod us-east-1
```

## 🛠️ **Desenvolvimento em Andamento**

### **✅ Implementado**
- Scripts base de setup
- Dockerfiles de todos os módulos
- Estrutura de CloudFormation

### **🔄 Em Desenvolvimento**
- Script completo de deploy
- Configuração ECS
- Integração RDS
- DNS automático

### **📋 Próximos Passos**
1. Finalizar scripts de deploy
2. Testar em ambiente AWS
3. Documentar processo completo
4. Implementar CI/CD

## 📚 **Documentação Relacionada**

- [Docker Guide](DOCKER-GUIDE.md) - Containerização completa
- [Startup Guide](../STARTUP-GUIDE.md) - Desenvolvimento local
- [Module Summaries](../MODULE-SUMMARIES.md) - Detalhes dos módulos

---

**🚧 Deploy AWS em desenvolvimento - Containerização 100% pronta! 🐳**