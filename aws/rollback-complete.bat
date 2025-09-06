# MyERP - Rollback Completo AWS

## 🚨 **ATENÇÃO: DESTRUIÇÃO TOTAL**

Este guia remove **TODOS** os recursos AWS do MyERP. **Dados serão perdidos permanentemente!**

## 🎯 **Cenários de Rollback**

### **1. Rollback de Ambiente Específico**
Remove apenas um ambiente (dev, prod, etc):
```bash
# Windows
rollback-complete.bat prod us-east-1

# Linux/Mac  
./rollback-complete.sh prod us-east-1
```

### **2. Rollback Total (Todos os Ambientes)**
Remove tudo relacionado ao MyERP:
```bash
# Windows
rollback-total.bat us-east-1

# Linux/Mac
./rollback-total.sh us-east-1
```

## 📋 **Ordem de Remoção (Importante!)**

### **PASSO 1: CloudFormation Stacks**
```bash
# 1. ECS (aplicações)
aws cloudformation delete-stack --stack-name myerp-ecs-prod

# 2. DNS (domínios)  
aws cloudformation delete-stack --stack-name myerp-dns-prod

# 3. Infrastructure (VPC, RDS, etc)
aws cloudformation delete-stack --stack-name myerp-infrastructure-prod
```

### **PASSO 2: Recursos Órfãos**
```bash
# ECR Repositories
aws ecr delete-repository --repository-name myerp-api-gateway --force
aws ecr delete-repository --repository-name myerp-auth-service --force
aws ecr delete-repository --repository-name myerp-rh-module --force
aws ecr delete-repository --repository-name myerp-biometria-module --force
aws ecr delete-repository --repository-name myerp-monitoring-module --force

# S3 Buckets
aws s3 rm s3://myerp-prod-assets --recursive
aws s3 rb s3://myerp-prod-assets

# CloudWatch Logs
aws logs delete-log-group --log-group-name /aws/ecs/myerp-api-gateway
aws logs delete-log-group --log-group-name /aws/ecs/myerp-auth-service
aws logs delete-log-group --log-group-name /aws/ecs/myerp-rh-module
aws logs delete-log-group --log-group-name /aws/ecs/myerp-biometria-module
aws logs delete-log-group --log-group-name /aws/ecs/myerp-monitoring-module

# Parameter Store
aws ssm delete-parameter --name /myerp/prod/database/password
aws ssm delete-parameter --name /myerp/prod/jwt/secret

# Secrets Manager
aws secretsmanager delete-secret --secret-id myerp-prod-db-credentials --force-delete-without-recovery
```

## 🛠️ **Scripts Automatizados**

### **Script Principal: rollback-complete.bat**
```bash
# Uso
rollback-complete.bat [AMBIENTE] [REGIÃO]

# Exemplos
rollback-complete.bat prod us-east-1
rollback-complete.bat dev us-west-2
```

**O que faz:**
- ✅ Deleta stacks CloudFormation na ordem correta
- ✅ Remove recursos órfãos
- ✅ Limpa logs e configurações
- ✅ Verifica se tudo foi removido

### **Script Total: rollback-total.bat**
```bash
# Remove TODOS os ambientes
rollback-total.bat us-east-1
```

## 💰 **Impacto Financeiro**

### **Após Rollback:**
- ✅ **Cobrança para** em algumas horas
- ✅ **RDS** para de cobrar imediatamente
- ✅ **ECS** para de cobrar imediatamente
- ✅ **Load Balancer** para de cobrar imediatamente

### **Custos Residuais:**
- ⚠️ **S3** pode ter cobrança mínima por alguns dias
- ⚠️ **CloudWatch Logs** podem ter retenção configurada
- ⚠️ **Route 53** hosted zones (se criadas)

## 🔍 **Verificação Pós-Rollback**

### **1. Verificar Stacks**
```bash
aws cloudformation list-stacks --query "StackSummaries[?contains(StackName, 'myerp')]"
```
**Resultado esperado:** Vazio ou apenas stacks com status `DELETE_COMPLETE`

### **2. Verificar ECR**
```bash
aws ecr describe-repositories --query "repositories[?contains(repositoryName, 'myerp')]"
```
**Resultado esperado:** Vazio

### **3. Verificar S3**
```bash
aws s3 ls | grep myerp
```
**Resultado esperado:** Vazio

### **4. Verificar RDS**
```bash
aws rds describe-db-instances --query "DBInstances[?contains(DBInstanceIdentifier, 'myerp')]"
```
**Resultado esperado:** Vazio

## 🚨 **Cenários de Emergência**

### **Stack Travada (DELETE_FAILED)**
```bash
# Forçar deleção
aws cloudformation delete-stack --stack-name myerp-ecs-prod --retain-resources

# Ou deletar recursos manualmente via console
```

### **RDS com Proteção**
```bash
# Remover proteção primeiro
aws rds modify-db-instance --db-instance-identifier myerp-prod-db --no-deletion-protection
aws rds delete-db-instance --db-instance-identifier myerp-prod-db --skip-final-snapshot
```

### **Load Balancer com Dependências**
```bash
# Listar target groups
aws elbv2 describe-target-groups --query "TargetGroups[?contains(TargetGroupName, 'myerp')]"

# Deletar target groups primeiro
aws elbv2 delete-target-group --target-group-arn arn:aws:elasticloadbalancing:...
```

## 📝 **Checklist de Rollback**

### **Antes do Rollback:**
- [ ] Backup de dados importantes (se necessário)
- [ ] Confirmar ambiente correto
- [ ] Verificar dependências externas
- [ ] Notificar equipe

### **Durante o Rollback:**
- [ ] Executar na ordem correta (ECS → DNS → Infrastructure)
- [ ] Aguardar conclusão de cada stack
- [ ] Monitorar erros
- [ ] Limpar recursos órfãos

### **Após o Rollback:**
- [ ] Verificar lista de stacks vazia
- [ ] Confirmar ECR limpo
- [ ] Verificar S3 limpo
- [ ] Monitorar fatura AWS
- [ ] Documentar lições aprendidas

## 🔄 **Rollback Parcial (Cenários Específicos)**

### **Apenas Aplicações (Manter Infraestrutura)**
```bash
# Deletar apenas ECS
aws cloudformation delete-stack --stack-name myerp-ecs-prod
```

### **Apenas DNS (Manter Apps e Infra)**
```bash
# Deletar apenas DNS
aws cloudformation delete-stack --stack-name myerp-dns-prod
```

### **Reset Completo (Manter VPC)**
```bash
# Deletar ECS e DNS, manter Infrastructure
aws cloudformation delete-stack --stack-name myerp-ecs-prod
aws cloudformation delete-stack --stack-name myerp-dns-prod
```

## ⏱️ **Tempo Estimado**

- **Rollback Simples**: 10-15 minutos
- **Rollback Completo**: 20-30 minutos  
- **Rollback com Problemas**: 1-2 horas

## 🆘 **Suporte**

Se algo der errado:
1. **Não entre em pânico**
2. **Documente o erro**
3. **Verifique logs do CloudFormation**
4. **Use console AWS para deleção manual**
5. **Monitore custos**

---

**⚠️ LEMBRE-SE: Rollback é irreversível! Dados serão perdidos permanentemente!**