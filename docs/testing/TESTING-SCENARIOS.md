# 🧪 CENÁRIOS DE TESTE - MyERP

## 🎯 **VISÃO GERAL**

Este guia consolida todos os cenários de teste do MyERP, desde testes unitários até cenários completos de implantação. Inclui testes manuais, automatizados e cenários de produção.

---

## 🏢 **CENÁRIO PRINCIPAL: IMPLANTAÇÃO ERP**

### **📋 CONTEXTO DA EMPRESA**
**Empresa:** Tech Solutions Ltda
- **CNPJ:** 12.345.678/0001-90
- **Funcionários:** 25 colaboradores
- **Plano:** STANDARD (50 usuários)
- **Necessidades:** RH, Financeiro, Biometria

---

## 🚀 **FASE 1: PREPARAÇÃO DO AMBIENTE**

### **1.1 Inicialização do Sistema**
```bash
# Executar scripts de inicialização
cd dev-scripts/core
start-myerp.bat

# Aguardar todos os serviços subirem
# Verificar logs de cada módulo
```

### **1.2 Verificação de Saúde**
```bash
# Health check completo
cd dev-scripts/testing
health-check.bat

# Ou via monitoring dashboard
GET http://localhost:8084/api/monitoring/dashboard
```

**✅ Critério de Sucesso:** Todos os 8 serviços com status "UP"

---

## 🏢 **FASE 2: CADASTRO DA EMPRESA**

### **2.1 Criar Empresa Principal**
```bash
POST http://localhost:8080/api/company/empresas
Content-Type: application/json

{
  "razaoSocial": "Tech Solutions Ltda",
  "nomeFantasia": "TechSol",
  "cnpj": "12345678000190",
  "inscricaoEstadual": "123456789",
  "email": "contato@techsol.com.br",
  "telefone": "(11) 3333-4444",
  "endereco": {
    "logradouro": "Av. Paulista, 1000",
    "numero": "1000",
    "complemento": "Sala 501",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01310-100"
  },
  "tipoPlano": "STANDARD",
  "ativa": true
}
```

### **2.2 Configurar Conta Bancária**
```bash
POST http://localhost:8080/api/company/empresas/{empresaId}/contas-bancarias
{
  "banco": "001",
  "nomeBanco": "Banco do Brasil",
  "agencia": "1234",
  "conta": "56789-0",
  "tipoConta": "CORRENTE",
  "principal": true
}
```

### **2.3 Verificar Feature Flags**
```bash
GET http://localhost:8080/api/company/empresas/{empresaId}/feature-flags

# Deve retornar features do plano STANDARD:
# - biometriaAvancada: true
# - relatoriosPersonalizados: true
# - integracoesBancarias: true
```

**✅ Critério de Sucesso:** Empresa criada com plano STANDARD e feature flags ativas

---

## 🔐 **FASE 3: CONFIGURAÇÃO DE USUÁRIOS**

### **3.1 Login com Admin Padrão**
```bash
POST http://localhost:8080/auth/login
{
  "login": "admin",
  "senha": "admin123"
}
# Salvar token JWT
```

### **3.2 Criar Usuários da Empresa**
```bash
# Admin da empresa
POST http://localhost:8080/auth/users
{
  "login": "admin.techsol",
  "senha": "TechSol@2024",
  "nome": "Carlos Silva - Administrador",
  "cpf": "12345678901",
  "role": "ADMIN"
}

# Funcionário RH
POST http://localhost:8080/auth/users
{
  "login": "maria.rh",
  "senha": "Maria@123",
  "nome": "Maria Santos - RH",
  "cpf": "98765432100",
  "role": "FUNCIONARIO"
}

# Funcionário Desenvolvedor
POST http://localhost:8080/auth/users
{
  "login": "joao.dev",
  "senha": "Joao@456",
  "nome": "João Oliveira - Desenvolvedor",
  "cpf": "11122233344",
  "role": "FUNCIONARIO"
}

# Funcionário Financeiro
POST http://localhost:8080/auth/users
{
  "login": "ana.fin",
  "senha": "Ana@789",
  "nome": "Ana Costa - Financeiro",
  "cpf": "55566677788",
  "role": "FUNCIONARIO"
}
```

**✅ Critério de Sucesso:** 4 usuários criados (1 admin + 3 funcionários)

---

## 👥 **FASE 4: GESTÃO DE RH**

### **4.1 Cadastrar Funcionários no Sistema**
```bash
# Login como admin da empresa
POST http://localhost:8080/auth/login
{
  "login": "admin.techsol",
  "senha": "TechSol@2024"
}

# Cadastrar cada funcionário
POST http://localhost:8080/api/admin/funcionarios
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "login": "maria.rh",
  "senha": "Maria@123",
  "role": "FUNCIONARIO"
}
# Repetir para João e Ana
```

### **4.2 Teste de Jornada Completa**
```bash
# Login como funcionário
POST http://localhost:8080/auth/login
{
  "login": "maria.rh",
  "senha": "Maria@123"
}

# Jornada do dia
# 08:00 - Entrada
POST /api/pontos/{mariaId}/registrar

# 12:00 - Saída almoço
POST /api/pontos/{mariaId}/registrar

# 13:00 - Retorno almoço
POST /api/pontos/{mariaId}/registrar

# 17:00 - Saída
POST /api/pontos/{mariaId}/registrar
```

### **4.3 Relatório Administrativo**
```bash
# Admin visualiza todos os pontos
GET http://localhost:8080/api/admin/pontos
```

**✅ Critério de Sucesso:** 3 funcionários cadastrados, jornada completa registrada

---

## 🔬 **FASE 5: BIOMETRIA E DETECÇÃO**

### **5.1 Configurar Câmeras**
```bash
# Câmera de entrada
POST http://localhost:8080/api/biometria/configuracao-camera
{
  "nomeConfiguracao": "Câmera Entrada Principal",
  "alias": "camera_entrada",
  "tipoCamera": "USB",
  "indiceUsb": 0,
  "resolucao": "1280x720",
  "fps": 30,
  "ativa": true
}

# Câmera de saída
POST http://localhost:8080/api/biometria/configuracao-camera
{
  "nomeConfiguracao": "Câmera Saída Principal",
  "alias": "camera_saida",
  "tipoCamera": "IP",
  "enderecoIp": "192.168.1.100",
  "porta": 8080,
  "ativa": true
}
```

### **5.2 Cadastrar Usuários Biométricos**
```bash
# Cadastrar funcionários para biometria
POST http://localhost:8080/api/biometria/usuarios
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "funcionarioId": {mariaId},
  "ativo": true
}
# Repetir para João e Ana
```

### **5.3 Simular Detecções Automáticas**
```bash
# Maria chegando às 08:05 (dentro da tolerância)
POST http://localhost:8080/api/biometria/simular-deteccao?nomeFuncionario=Maria Santos&movimento=ENTRADA

# João chegando às 08:25 (fora da tolerância)
POST http://localhost:8080/api/biometria/simular-deteccao?nomeFuncionario=João Oliveira&movimento=ENTRADA

# Ana saindo às 17:10 (dentro da tolerância)
POST http://localhost:8080/api/biometria/simular-deteccao?nomeFuncionario=Ana Costa&movimento=SAIDA
```

### **5.4 Verificar Eventos e Sugestões**
```bash
# Listar eventos
GET http://localhost:8080/api/biometria/eventos

# Eventos pendentes (João - fora da tolerância)
GET http://localhost:8080/api/biometria/eventos/pendentes

# Dashboard estatísticas
GET http://localhost:8080/api/biometria/dashboard
```

**✅ Critério de Sucesso:** Câmeras configuradas, detecções funcionando, sugestões para RH

---

## 💰 **FASE 6: GESTÃO FINANCEIRA**

### **6.1 Configurar Dados Bancários**
```bash
POST http://localhost:8080/api/financial/configuracao-bancaria
{
  "banco": "001",
  "agencia": "1234",
  "conta": "56789-0",
  "chavePix": "12345678000190",
  "tipoChavePix": "CNPJ"
}
```

### **6.2 Gerar Boleto de Cobrança**
```bash
POST http://localhost:8080/api/financial/boletos
{
  "valor": 1500.00,
  "dataVencimento": "2024-10-15",
  "sacado": {
    "nome": "Cliente Exemplo Ltda",
    "cpfCnpj": "98765432000100",
    "endereco": "Rua das Flores, 123"
  },
  "descricao": "Desenvolvimento de Sistema"
}
```

### **6.3 Gerar Cobrança PIX**
```bash
POST http://localhost:8080/api/financial/pix/cobranca
{
  "valor": 750.00,
  "descricao": "Consultoria Técnica",
  "pagador": {
    "nome": "João Cliente",
    "cpf": "12345678901"
  }
}
```

### **6.4 Relatórios Financeiros**
```bash
# Relatório de boletos
GET http://localhost:8080/api/financial/relatorios/boletos?periodo=2024-09

# Dashboard financeiro
GET http://localhost:8080/api/financial/dashboard
```

**✅ Critério de Sucesso:** Boletos e PIX gerados, relatórios funcionando

---

## 📊 **FASE 7: MONITORAMENTO**

### **7.1 Dashboard Geral**
```bash
GET http://localhost:8080/api/monitoring/dashboard

# Verificar:
# - Status dos 8 serviços
# - Métricas de memória e CPU
# - Uptime do sistema
# - Percentual de saúde geral
```

### **7.2 Health Check Automatizado**
```bash
# Script automatizado
cd dev-scripts/testing
health-check.bat

# Verifica todos os serviços automaticamente
```

**✅ Critério de Sucesso:** Todos os serviços monitorados e saudáveis

---

## 🧪 **FASE 8: TESTES DE INTEGRAÇÃO**

### **8.1 Fluxo Completo: Funcionário Novo**
```bash
# 1. Admin cria usuário no Auth
POST /auth/users

# 2. Admin cadastra no RH
POST /api/admin/funcionarios

# 3. Admin cadastra na Biometria
POST /api/biometria/usuarios

# 4. Funcionário faz login
POST /auth/login

# 5. Funcionário registra ponto
POST /api/pontos/{id}/registrar

# 6. Biometria detecta automaticamente
POST /api/biometria/simular-deteccao

# 7. Admin verifica integração
GET /api/admin/pontos
```

### **8.2 Teste de Limites do Plano**
```bash
# Tentar criar mais de 50 usuários (limite STANDARD)
# Deve retornar erro de limite excedido

# Verificar feature flags
GET /api/company/empresas/{id}/feature-flags
```

### **8.3 Teste de Segurança**
```bash
# Tentar acessar endpoint admin sem permissão
GET /api/admin/funcionarios
# (sem token admin - deve retornar 403)

# Tentar acessar dados de outro funcionário
GET /api/pontos/{outroFuncionarioId}
# (deve retornar 403)
```

**✅ Critério de Sucesso:** Integrações funcionando, segurança validada

---

## 🧪 **TESTES AUTOMATIZADOS**

### **Testes Unitários**
```bash
# Compilação e testes
cd dev-scripts/testing
test-compilation.bat

# Cada módulo individualmente
mvn test -pl infrastructure/auth-service
mvn test -pl modules/rh-module
mvn test -pl modules/biometria-module
```

### **Testes de Integração**
```bash
# Collection Postman completa
# Importar: postman/MyERP-Implantacao-Completa.postman_collection.json
# Executar sequencialmente as 8 fases
```

### **Testes de Performance**
```bash
# Health check de todos os serviços
cd dev-scripts/testing
health-check.bat

# Verificar tempo de resposta < 2s
# Verificar uso de memória < 80%
```

---

## 📋 **TESTES STANDALONE**

### **Teste Individual por Módulo**

#### **Auth Service**
```bash
# Teste de login
POST http://localhost:8081/auth/login
{
  "login": "admin",
  "senha": "admin123"
}

# Teste de validação
POST http://localhost:8081/auth/validate
Authorization: Bearer {token}
```

#### **RH Module**
```bash
# Teste de registro de ponto
POST http://localhost:8082/api/pontos/1/registrar
Authorization: Bearer {token}

# Teste de listagem
GET http://localhost:8082/api/pontos/1
Authorization: Bearer {token}
```

#### **Biometria Module**
```bash
# Teste de simulação
POST http://localhost:8083/api/biometria/simular-deteccao?nomeFuncionario=Teste&movimento=ENTRADA

# Teste de dashboard
GET http://localhost:8083/api/biometria/dashboard
```

#### **Company Module**
```bash
# Teste de criação de empresa
POST http://localhost:8085/api/company/empresas
{
  "razaoSocial": "Empresa Teste",
  "cnpj": "12345678000199",
  "tipoPlano": "BASIC"
}
```

#### **Financial Module**
```bash
# Teste de boleto
POST http://localhost:8086/api/financial/boletos
{
  "valor": 100.00,
  "dataVencimento": "2024-12-31",
  "sacado": {
    "nome": "Cliente Teste",
    "cpfCnpj": "12345678901"
  }
}
```

#### **Monitoring Module**
```bash
# Teste de dashboard
GET http://localhost:8084/api/monitoring/dashboard

# Teste de serviços
GET http://localhost:8084/api/monitoring/services
```

---

## 📊 **CRITÉRIOS DE SUCESSO**

### **Técnicos**
- ✅ Todos os 8 serviços UP no monitoring
- ✅ Tempo de resposta < 2 segundos
- ✅ Zero erros 500 nos endpoints
- ✅ Autenticação JWT funcionando
- ✅ Banco PostgreSQL conectado

### **Funcionais**
- ✅ Empresa criada e configurada
- ✅ Usuários podem fazer login
- ✅ Registros de ponto funcionando
- ✅ Biometria detectando automaticamente
- ✅ Boletos e PIX sendo gerados
- ✅ Relatórios disponíveis

### **Negócio**
- ✅ Fluxo completo de funcionário
- ✅ Controle de acesso por roles
- ✅ Limites de plano respeitados
- ✅ Feature flags funcionando
- ✅ Integrações entre módulos

---

## 📋 **CHECKLIST FINAL**

### **Funcionalidades Validadas**
- [ ] ✅ Empresa cadastrada com plano STANDARD
- [ ] ✅ 4 usuários criados (1 admin + 3 funcionários)
- [ ] ✅ Funcionários cadastrados no RH
- [ ] ✅ Registros de ponto funcionando
- [ ] ✅ Biometria configurada e detectando
- [ ] ✅ Boletos e PIX gerados
- [ ] ✅ Conciliação bancária processada
- [ ] ✅ Monitoramento ativo
- [ ] ✅ Integrações validadas

### **Relatórios de Validação**
```bash
# Relatório de usuários
GET /api/admin/funcionarios

# Relatório de pontos do dia
GET /api/admin/pontos

# Relatório de eventos biométricos
GET /api/biometria/eventos

# Relatório financeiro
GET /api/financial/dashboard

# Status geral do sistema
GET /api/monitoring/dashboard
```

---

## 🎯 **RESUMO EXECUTIVO**

### **Cenário Principal**
- **Empresa:** Tech Solutions Ltda (25 funcionários)
- **Plano:** STANDARD (50 usuários, features ativas)
- **Módulos:** 8 módulos integrados e funcionais
- **Tempo de teste:** ~2 horas para cenário completo

### **Cobertura de Testes**
- ✅ **Testes unitários** por módulo
- ✅ **Testes de integração** entre módulos
- ✅ **Testes de segurança** (autenticação/autorização)
- ✅ **Testes de performance** (tempo resposta/memória)
- ✅ **Testes de negócio** (fluxos completos)

### **Ferramentas de Teste**
- ✅ **Scripts automatizados** (health-check.bat, test-compilation.bat)
- ✅ **Collections Postman** (cenários completos)
- ✅ **Monitoring dashboard** (métricas em tempo real)
- ✅ **Logs estruturados** (debug e troubleshooting)

**🎉 CENÁRIOS DE TESTE COMPLETOS E VALIDADOS! 🧪✅**