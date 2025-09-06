# ☁️ AWS SCRIPTS - MyERP

## 🚀 **INÍCIO RÁPIDO**

### **Deploy Completo (Recomendado)**
```bash
# Uma linha faz tudo
aws-manager.bat full-deploy prod us-east-1 myerp.com
```

### **Rollback Seguro**
```bash
# Remove tudo
aws-manager.bat rollback prod us-east-1
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
| **deploy-aws.bat** | Deploy da aplicação | `deploy-aws.bat prod us-east-1 myerp.com` |
| **rollback-aws.bat** | Rollback seguro | `rollback-aws.bat prod us-east-1` |

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

# Deploy (quantas vezes precisar)
aws-manager.bat deploy prod us-east-1 myerp.com

# Rollback (quando necessário)
aws-manager.bat rollback prod us-east-1

# Status (verificar recursos)
aws-manager.bat status
```

**🚀 DEPLOY AWS SIMPLIFICADO E SEGURO! ☁️✅**