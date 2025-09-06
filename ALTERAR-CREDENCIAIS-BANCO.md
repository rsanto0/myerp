# 🔐 COMO ALTERAR CREDENCIAIS DO BANCO DE DADOS

## 📋 **LOCAIS QUE DEVEM SER ALTERADOS**

### **1. DOCKER COMPOSE (Obrigatório)**
**Arquivo:** `docker-compose.yml`
```yaml
# ALTERAR AQUI:
services:
  postgres:
    environment:
      POSTGRES_USER: novo_usuario     # ← ALTERAR
      POSTGRES_PASSWORD: nova_senha   # ← ALTERAR
```

### **2. CONFIG SERVER - CONFIGURAÇÕES GLOBAIS**
**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/application.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

### **3. CONFIG SERVER - SERVIÇOS ESPECÍFICOS**

**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/auth-service.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/rh-service.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/biometria-service.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/company-service.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `infrastructure/config-server/src/main/resources/config-repo/financial-service.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

### **4. CONFIGURAÇÕES LOCAIS DOS MÓDULOS**

**Arquivo:** `infrastructure/auth-service/src/main/resources/application.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `infrastructure/auth-service/src/main/resources/application-local.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `modules/rh-module/src/main/resources/application.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `modules/biometria-module/src/main/resources/application.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `modules/company-module/src/main/resources/application.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

**Arquivo:** `modules/financial-module/src/main/resources/application.yml`
```yaml
# ALTERAR AQUI:
spring:
  datasource:
    username: novo_usuario    # ← ALTERAR
    password: nova_senha      # ← ALTERAR
```

---

## 🚀 **PASSO A PASSO PARA ALTERAR**

### **EXEMPLO: Alterar para usuario "myerp_admin" e senha "MyErp@2024"**

### **1. Parar o Sistema**
```bash
# Parar todos os serviços
docker-compose down
```

### **2. Alterar Docker Compose**
```yaml
# docker-compose.yml
services:
  postgres:
    environment:
      POSTGRES_USER: myerp_admin
      POSTGRES_PASSWORD: MyErp@2024
```

### **3. Alterar Config Server - Global**
```yaml
# infrastructure/config-server/src/main/resources/config-repo/application.yml
spring:
  datasource:
    username: myerp_admin
    password: MyErp@2024
```

### **4. Alterar Config Server - Todos os Serviços**
```bash
# Alterar em TODOS estes arquivos:
infrastructure/config-server/src/main/resources/config-repo/auth-service.yml
infrastructure/config-server/src/main/resources/config-repo/rh-service.yml
infrastructure/config-server/src/main/resources/config-repo/biometria-service.yml
infrastructure/config-server/src/main/resources/config-repo/company-service.yml
infrastructure/config-server/src/main/resources/config-repo/financial-service.yml

# Em cada arquivo, alterar:
spring:
  datasource:
    username: myerp_admin
    password: MyErp@2024
```

### **5. Alterar Configurações Locais**
```bash
# Alterar em TODOS estes arquivos:
infrastructure/auth-service/src/main/resources/application.yml
infrastructure/auth-service/src/main/resources/application-local.yml
modules/rh-module/src/main/resources/application.yml
modules/biometria-module/src/main/resources/application.yml
modules/company-module/src/main/resources/application.yml
modules/financial-module/src/main/resources/application.yml

# Em cada arquivo, alterar:
spring:
  datasource:
    username: myerp_admin
    password: MyErp@2024
```

### **6. Recriar Banco de Dados**
```bash
# Remover volumes antigos
docker-compose down -v

# Subir PostgreSQL com novas credenciais
docker-compose up -d postgres

# Aguardar inicialização
ping 127.0.0.1 -n 10 >nul

# Criar bancos com novo usuário
cd dev-scripts
create-databases.bat
```

### **7. Iniciar Sistema**
```bash
# Iniciar todos os serviços
start-myerp.bat
```

---

## 📋 **CHECKLIST DE ALTERAÇÃO**

### **Arquivos Docker**
- [ ] `docker-compose.yml` - POSTGRES_USER e POSTGRES_PASSWORD

### **Config Server**
- [ ] `config-repo/application.yml` - Configurações globais
- [ ] `config-repo/auth-service.yml` - Auth Service
- [ ] `config-repo/rh-service.yml` - RH Module
- [ ] `config-repo/biometria-service.yml` - Biometria Module
- [ ] `config-repo/company-service.yml` - Company Module
- [ ] `config-repo/financial-service.yml` - Financial Module

### **Configurações Locais**
- [ ] `auth-service/application.yml` - Auth local
- [ ] `auth-service/application-local.yml` - Auth local profile
- [ ] `rh-module/application.yml` - RH local
- [ ] `biometria-module/application.yml` - Biometria local
- [ ] `company-module/application.yml` - Company local
- [ ] `financial-module/application.yml` - Financial local

### **Verificação**
- [ ] PostgreSQL iniciado com novas credenciais
- [ ] Bancos criados com novo usuário
- [ ] Todos os módulos conectando sem erro
- [ ] Health checks passando

---

## 🛠️ **SCRIPT AUTOMÁTICO**

### **Criar Script para Alterar Tudo**
```bash
# alterar-credenciais.bat
@echo off
set NOVO_USER=myerp_admin
set NOVA_SENHA=MyErp@2024

echo Alterando credenciais para %NOVO_USER%/%NOVA_SENHA%

# Usar PowerShell para substituir em massa
powershell -Command "(Get-Content docker-compose.yml) -replace 'POSTGRES_USER: myerp_user', 'POSTGRES_USER: %NOVO_USER%' | Set-Content docker-compose.yml"
powershell -Command "(Get-Content docker-compose.yml) -replace 'POSTGRES_PASSWORD: myerp_pass', 'POSTGRES_PASSWORD: %NOVA_SENHA%' | Set-Content docker-compose.yml"

# Repetir para todos os arquivos...
echo Credenciais alteradas!
```

---

## ⚠️ **IMPORTANTE**

### **Ordem Obrigatória:**
1. **Parar sistema** primeiro
2. **Alterar Docker Compose** (obrigatório)
3. **Alterar Config Server** (centralizadas)
4. **Alterar configurações locais** (fallback)
5. **Recriar banco** com novas credenciais
6. **Iniciar sistema**

### **Não Esquecer:**
- ✅ **Docker Compose** é o mais importante
- ✅ **Remover volumes** antigos (-v)
- ✅ **Recriar bancos** com novo usuário
- ✅ **Testar conexões** após alteração

---

## 🎯 **RESUMO DOS 13 ARQUIVOS**

### **Obrigatórios (9 arquivos):**
1. `docker-compose.yml`
2. `config-repo/application.yml`
3. `config-repo/auth-service.yml`
4. `config-repo/rh-service.yml`
5. `config-repo/biometria-service.yml`
6. `config-repo/company-service.yml`
7. `config-repo/financial-service.yml`
8. `auth-service/application.yml`
9. `auth-service/application-local.yml`

### **Locais dos Módulos (4 arquivos):**
10. `rh-module/application.yml`
11. `biometria-module/application.yml`
12. `company-module/application.yml`
13. `financial-module/application.yml`

**🔐 TOTAL: 13 ARQUIVOS DEVEM SER ALTERADOS PARA MUDAR AS CREDENCIAIS! 🔐**