# MyERP - Deploy AWS: Passo a Passo Simples

## 🎯 **Analogia: Mudança de Casa**

Imagine que você está **mudando sua empresa** de um escritório local para um **prédio comercial moderno** (AWS):

1. **🏢 Escolher o prédio** = Configurar conta AWS
2. **🔑 Pegar as chaves** = Configurar credenciais
3. **📋 Assinar contrato** = Definir ID da conta
4. **🚚 Fazer a mudança** = Deploy dos sistemas

## 📋 **Pré-requisitos (O que você precisa)**

### **1. Conta AWS**
- Acesse: https://aws.amazon.com
- Crie uma conta (cartão de crédito necessário)
- **Anote seu ID da conta** (12 dígitos, ex: 123456789012)

### **2. Credenciais de Acesso**
No console AWS:
1. Vá em **IAM** → **Users** → **Create User**
2. Nome: `myerp-deploy-user`
3. Attach policies: `AdministratorAccess`
4. **Anote**: Access Key ID e Secret Access Key

## 🚀 **Deploy Passo a Passo**

### **PASSO 1: Configurar AWS CLI**
```bash
# Windows
setup-aws.bat

# Ou manual:
aws configure
# AWS Access Key ID: AKIA...
# AWS Secret Access Key: xyz...
# Default region: us-east-1
# Default output format: json
```

### **PASSO 2: Definir ID da Conta**

**Windows:**
```cmd
REM Descobrir seu ID da conta
aws sts get-caller-identity

REM Definir variável (SUBSTITUA pelo seu ID)
set AWS_ACCOUNT_ID=123456789012
```

**Linux/Mac:**
```bash
export AWS_ACCOUNT_ID=123456789012
```

### **PASSO 3: Executar Deploy**

**Windows:**
```cmd
cd aws
deploy-aws.bat prod us-east-1 myerp.com
```

**Linux/Mac:**
```bash
cd aws/
./deploy-aws.sh prod us-east-1 myerp.com
```

## 📝 **Exemplo Prático**

### **Cenário: João quer fazer deploy**

#### **1. João tem conta AWS: 987654321098**
```bash
export AWS_ACCOUNT_ID=987654321098
```

#### **2. João quer ambiente de produção na região us-east-1**
```bash
cd aws/
./deploy-aws.sh prod us-east-1 joao-empresa.com
```

#### **3. Resultado para João:**
- **API**: https://api.prod.joao-empresa.com
- **Monitoring**: https://monitoring.prod.joao-empresa.com

## 🔧 **Configurações por Ambiente**

### **Desenvolvimento**
```bash
export AWS_ACCOUNT_ID=123456789012
./deploy-aws.sh dev us-east-1 minha-empresa.com
```
**Resultado**: https://api.dev.minha-empresa.com

### **Produção**
```bash
export AWS_ACCOUNT_ID=123456789012
./deploy-aws.sh prod us-east-1 minha-empresa.com
```
**Resultado**: https://api.prod.minha-empresa.com

## ❓ **Perguntas Frequentes**

### **P: Onde encontro meu ID da conta AWS?**
**R:** No console AWS, canto superior direito, ou execute:
```bash
aws sts get-caller-identity --query Account --output text
```

### **P: Preciso de um domínio próprio?**
**R:** Não obrigatório. Pode usar:
```bash
./deploy-aws.sh prod us-east-1 myerp-teste.com
```

### **P: Quanto vai custar?**
**R:** 
- **Desenvolvimento**: ~$50/mês
- **Produção**: ~$150/mês

### **P: Como deletar tudo?**
**R:**
```bash
# Deletar na ordem inversa
aws cloudformation delete-stack --stack-name myerp-ecs-prod
aws cloudformation delete-stack --stack-name myerp-dns-prod  
aws cloudformation delete-stack --stack-name myerp-infrastructure-prod
```

## 🚨 **Troubleshooting**

### **Erro: "AWS_ACCOUNT_ID not set"**
```bash
# Solução:
export AWS_ACCOUNT_ID=SEU_ID_AQUI
```

### **Erro: "Credentials not configured"**
```bash
# Solução:
aws configure
```

### **Erro: "Stack already exists"**
```bash
# Solução: Usar nome diferente
./deploy-aws.sh prod2 us-east-1 myerp.com
```

## ✅ **Checklist Final**

Antes de executar o deploy:
- [ ] Conta AWS criada
- [ ] ID da conta anotado (12 dígitos)
- [ ] AWS CLI instalado
- [ ] Credenciais configuradas
- [ ] Variável AWS_ACCOUNT_ID definida
- [ ] Domínio escolhido

**Comando final:**
```bash
export AWS_ACCOUNT_ID=SEU_ID_AQUI
cd aws/
./deploy-aws.sh prod us-east-1 seu-dominio.com
```

**Resultado:** Sistema MyERP rodando na AWS! 🎉