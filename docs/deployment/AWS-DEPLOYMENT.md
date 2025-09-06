# ☁️ DEPLOY AWS - MyERP

## 🚀 **VISÃO GERAL**

Este guia consolida todas as informações sobre deploy do MyERP na AWS, incluindo setup, deploy, rollback e troubleshooting. O sistema utiliza CloudFormation, ECS, RDS e Route 53 para uma infraestrutura completa e escalável.

---

## 🎯 **MODO SIMPLES (RECOMENDADO)**

### **🚀 Deploy Completo (Uma linha)**
```bash
# Setup + Deploy automático
cd aws
aws-manager.bat full-deploy prod us-east-1 myerp.com
```

### **🗑️ Rollback Completo**
```bash
# Remove tudo do ambiente
aws-manager.bat rollback prod us-east-1
```

### **📊 Verificar Status**
```bash
# Status de todos os recursos
aws-manager.bat status
```

---

## 🔧 **MODO AVANÇADO**

### **1. Setup Inicial (Uma vez)**
```bash
aws-manager.bat setup
```

### **2. Deploy (Quantas vezes precisar)**
```bash
aws-manager.bat deploy prod us-east-1 myerp.com
```

### **3. Rollback (Quando necessário)**
```bash
aws-manager.bat rollback prod us-east-1
```

---

## 📋 **SCRIPTS DISPONÍVEIS**

### **aws-manager.bat - Orquestrador Principal**
**Comandos:**
```bash
aws-manager.bat setup                           # Setup inicial
aws-manager.bat deploy [env] [region] [domain]  # Deploy
aws-manager.bat rollback [env] [region]         # Rollback
aws-manager.bat status                          # Status
aws-manager.bat full-deploy [env] [region] [domain] # Setup + Deploy
aws-manager.bat help                            # Ajuda
```

### **setup-aws.bat - Configuração Inicial**
**Função:** Configuração única do ambiente AWS
- ✅ Verifica AWS CLI instalado
- ✅ Configura credenciais (Access Key, Secret Key)
- ✅ Define região padrão
- ✅ Testa conexão
- ✅ Obtém ID da conta
- ✅ Cria flag de setup completo

### **deploy-aws.bat - Deploy da Aplicação**
**Função:** Deploy completo na AWS
- ✅ Verifica pré-requisitos
- ✅ Cria infraestrutura (CloudFormation)
- ✅ Configura Parameter Store
- ✅ Configura DNS (Route 53)
- ✅ Faz build das aplicações
- ✅ Fornece URLs finais

### **rollback-aws.bat - Rollback Seguro**
**Função:** Remoção completa dos recursos
- ✅ Confirmação obrigatória ("CONFIRMO")
- ✅ Remove na ordem correta (ECS → DNS → Infrastructure)
- ✅ Limpa recursos órfãos
- ✅ Verificação final

---

## 🏗️ **ARQUITETURA AWS**

### **Componentes Principais**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Route 53      │    │  Application    │    │   Database      │
│   (DNS)         │    │  Load Balancer  │    │   (RDS)         │
│                 │    │                 │    │                 │
│ api.myerp.com   │───▶│  Target Groups  │───▶│  PostgreSQL     │
│ app.myerp.com   │    │                 │    │  Multi-AZ       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   ECS Cluster   │
                       │                 │
                       │ ┌─────────────┐ │
                       │ │ API Gateway │ │
                       │ │ Auth Service│ │
                       │ │ RH Module   │ │
                       │ │ Biometria   │ │
                       │ │ Company     │ │
                       │ │ Financial   │ │
                       │ │ Monitoring  │ │
                       │ └─────────────┘ │
                       └─────────────────┘
```

### **Stacks CloudFormation**
1. **myerp-infrastructure-[env]** - VPC, RDS, ALB, Security Groups
2. **myerp-ecs-[env]** - ECS Cluster, Services, Task Definitions
3. **myerp-dns-[env]** - Route 53, SSL Certificates

---

## 🔄 **FLUXOS DE DEPLOY**

### **🎯 Fluxo Iniciante**
```bash
# 1. Deploy completo
aws-manager.bat full-deploy prod us-east-1 myerp.com

# 2. Verificar
aws-manager.bat status

# 3. Rollback se necessário
aws-manager.bat rollback prod us-east-1
```

### **🔧 Fluxo Profissional**
```bash
# 1. Setup uma vez
aws-manager.bat setup

# 2. Deploy múltiplos ambientes
aws-manager.bat deploy dev us-east-1 dev.myerp.com
aws-manager.bat deploy prod us-east-1 myerp.com

# 3. Rollback específico
aws-manager.bat rollback dev us-east-1
```

### **⚙️ Fluxo Manual**
```bash
# 1. Setup manual
setup-aws.bat

# 2. Deploy manual
set AWS_ACCOUNT_ID=123456789012
deploy-aws.bat prod us-east-1 myerp.com

# 3. Rollback manual
rollback-aws.bat prod us-east-1
```

---

## 🔐 **CONFIGURAÇÃO E SEGURANÇA**

### **Pré-requisitos**
- AWS CLI instalado
- Conta AWS com permissões administrativas
- Domínio registrado (opcional)

### **Credenciais Necessárias**
```bash
# AWS Access Keys
AWS_ACCESS_KEY_ID=AKIA...
AWS_SECRET_ACCESS_KEY=...
AWS_DEFAULT_REGION=us-east-1
```

### **Permissões IAM Necessárias**
- CloudFormation (criar/deletar stacks)
- ECS (gerenciar clusters e serviços)
- RDS (criar/gerenciar bancos)
- Route 53 (gerenciar DNS)
- ECR (repositórios de imagens)
- Parameter Store (configurações)

---

## 💰 **CUSTOS ESTIMADOS**

### **Ambiente de Produção (us-east-1)**
| Recurso | Especificação | Custo/Mês |
|---------|---------------|-----------|
| **ECS Tasks** | 7 serviços (0.25 vCPU, 0.5GB) | ~$25 |
| **RDS PostgreSQL** | db.t3.micro | ~$15 |
| **Application Load Balancer** | 1 ALB | ~$20 |
| **Route 53** | Hosted Zone + queries | ~$5 |
| **CloudWatch** | Logs e métricas | ~$10 |
| **Total Estimado** | | **~$75/mês** |

### **Ambiente de Desenvolvimento**
| Recurso | Especificação | Custo/Mês |
|---------|---------------|-----------|
| **ECS Tasks** | 3 serviços principais | ~$15 |
| **RDS PostgreSQL** | db.t3.micro | ~$15 |
| **ALB** | 1 ALB | ~$20 |
| **Total Estimado** | | **~$50/mês** |

---

## 🚨 **ROLLBACK E SEGURANÇA**

### **Rollback Seguro**
```bash
# Confirmação obrigatória
aws-manager.bat rollback prod us-east-1
# Digite: CONFIRMO
```

### **Ordem de Remoção**
1. **ECS Services** (aplicações)
2. **Route 53** (DNS)
3. **Infrastructure** (VPC, RDS, ALB)
4. **Recursos órfãos** (ECR, CloudWatch, Parameter Store)

### **Verificação Pós-Rollback**
```bash
# Verificar stacks removidas
aws cloudformation list-stacks --query "StackSummaries[?contains(StackName, 'myerp')]"

# Verificar ECR limpo
aws ecr describe-repositories --query "repositories[?contains(repositoryName, 'myerp')]"
```

---

## 🔍 **MONITORAMENTO E LOGS**

### **CloudWatch Logs**
```bash
# Logs por serviço
/aws/ecs/myerp-api-gateway
/aws/ecs/myerp-auth-service
/aws/ecs/myerp-rh-module
/aws/ecs/myerp-biometria-module
/aws/ecs/myerp-company-module
/aws/ecs/myerp-financial-module
/aws/ecs/myerp-monitoring-module
```

### **Métricas Importantes**
- CPU e memória dos containers
- Latência do Load Balancer
- Conexões do RDS
- Erros HTTP (4xx, 5xx)

### **Alertas Recomendados**
- CPU > 80% por 5 minutos
- Memória > 90% por 5 minutos
- Erro 5xx > 10 por minuto
- RDS conexões > 80%

---

## 🛠️ **TROUBLESHOOTING**

### **Problemas Comuns**

#### **Erro: AWS CLI não instalado**
```bash
# Baixar e instalar
https://aws.amazon.com/cli/
```

#### **Erro: Credenciais inválidas**
```bash
# Reconfigurar
aws-manager.bat setup
```

#### **Erro: Stack CREATE_FAILED**
```bash
# Ver detalhes no console AWS
# Ou verificar logs CloudFormation
aws cloudformation describe-stack-events --stack-name myerp-infrastructure-prod
```

#### **Erro: RDS com proteção**
```bash
# Remover proteção primeiro
aws rds modify-db-instance --db-instance-identifier myerp-prod-db --no-deletion-protection
```

### **Stack Travada em DELETE_FAILED**
```bash
# Forçar deleção via console AWS
# Ou tentar rollback novamente
aws-manager.bat rollback prod us-east-1
```

---

## 📊 **CENÁRIOS DE USO**

### **Cenário 1: Primeira Implantação**
```bash
# Tudo em uma linha
aws-manager.bat full-deploy prod us-east-1 myerp.com

# URLs disponíveis:
# - https://api.myerp.com
# - https://app.myerp.com
# - https://monitoring.myerp.com
```

### **Cenário 2: Múltiplos Ambientes**
```bash
# Setup uma vez
aws-manager.bat setup

# Deploy dev
aws-manager.bat deploy dev us-east-1 dev.myerp.com

# Deploy prod
aws-manager.bat deploy prod us-east-1 myerp.com
```

### **Cenário 3: Rollback de Emergência**
```bash
# Rollback rápido e seguro
aws-manager.bat rollback prod us-east-1
# Confirma com "CONFIRMO"
# Remove tudo em 15-20 minutos
```

### **Cenário 4: Verificação de Status**
```bash
# Ver tudo que está rodando
aws-manager.bat status

# Mostra:
# - Stacks CloudFormation
# - Repositórios ECR
# - Parâmetros SSM
```

---

## 🎯 **MELHORES PRÁTICAS**

### **Deploy**
- Sempre testar em ambiente dev primeiro
- Fazer backup de dados importantes
- Monitorar custos regularmente
- Usar tags consistentes nos recursos

### **Segurança**
- Rotacionar Access Keys regularmente
- Usar IAM roles específicas
- Habilitar CloudTrail para auditoria
- Configurar MFA na conta AWS

### **Monitoramento**
- Configurar alertas CloudWatch
- Monitorar logs de aplicação
- Acompanhar métricas de performance
- Revisar custos mensalmente

---

## 📋 **CHECKLIST DE DEPLOY**

### **Pré-Deploy**
- [ ] AWS CLI instalado e configurado
- [ ] Credenciais AWS válidas
- [ ] Domínio disponível (se usando)
- [ ] Código compilado e testado

### **Durante Deploy**
- [ ] Setup executado com sucesso
- [ ] Stacks CloudFormation criadas
- [ ] Serviços ECS rodando
- [ ] DNS configurado
- [ ] URLs acessíveis

### **Pós-Deploy**
- [ ] Health checks passando
- [ ] Logs funcionando
- [ ] Métricas coletadas
- [ ] Alertas configurados
- [ ] Custos monitorados

### **Rollback (Se necessário)**
- [ ] Backup de dados críticos
- [ ] Confirmação da operação
- [ ] Stacks removidas na ordem
- [ ] Recursos órfãos limpos
- [ ] Custos pararam de acumular

---

## 🎉 **RESUMO EXECUTIVO**

### **✅ O que o sistema oferece:**
- **Deploy automatizado** em uma linha
- **Infraestrutura completa** na AWS
- **Rollback seguro** com confirmação
- **Monitoramento integrado** com CloudWatch
- **Múltiplos ambientes** (dev, prod)

### **🚀 Para começar:**
1. **Instalar AWS CLI**
2. **Executar:** `aws-manager.bat full-deploy prod us-east-1 myerp.com`
3. **Aguardar 15-20 minutos**
4. **Acessar URLs fornecidas**

### **💰 Custos esperados:**
- **Desenvolvimento:** ~$50/mês
- **Produção:** ~$75/mês
- **Rollback:** Para custos em algumas horas

**🎯 DEPLOY AWS COMPLETO, SEGURO E AUTOMATIZADO! ☁️✅**