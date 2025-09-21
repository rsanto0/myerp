# ☁️ AWS SCRIPTS - MyERP

## 🚀 **INÍCIO RÁPIDO**

### **Deploy Simples (Recomendado)**
```bash
# Deploy básico para desenvolvimento
aws-manager.bat full-deploy dev us-east-1
```

### **Rollback Simples**
```bash
# Remove recursos básicos
aws-manager.bat rollback dev us-east-1
```

### **Status dos Recursos**
```bash
# Verificar o que está rodando
aws-manager.bat status
```

---

## 📋 **SCRIPTS DISPONÍVEIS**

| Script | Função | Uso |
|--------|--------|-----|
| **aws-manager.bat** | Orquestrador principal | `aws-manager.bat help` |
| **setup-aws.bat** | Configuração inicial | `setup-aws.bat` |
| **deploy-aws.bat** | Deploy simples (ECR + Parameter Store) | `deploy-aws.bat dev us-east-1` |
| **rollback-aws.bat** | Rollback simples | `rollback-aws.bat dev us-east-1` |

---

## 📖 **DOCUMENTAÇÃO COMPLETA**

Para documentação detalhada, consulte:
**[docs/deployment/AWS-DEPLOYMENT.md](../docs/deployment/AWS-DEPLOYMENT.md)**

Inclui:
- ✅ Guia passo a passo
- ✅ Arquitetura AWS
- ✅ Custos estimados
- ✅ Troubleshooting
- ✅ Melhores práticas

---

## 🎯 **COMANDOS ESSENCIAIS**

```bash
# Setup inicial (uma vez)
aws-manager.bat setup

# Deploy simples (desenvolvimento)
aws-manager.bat deploy dev us-east-1

# Rollback simples (quando necessário)
aws-manager.bat rollback dev us-east-1

# Status (verificar recursos)
aws-manager.bat status
```

**🚀 DEPLOY AWS SIMPLIFICADO E SEGURO! ☁️✅**