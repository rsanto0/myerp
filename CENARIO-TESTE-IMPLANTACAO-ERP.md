# 🏢 CENÁRIO DE TESTE - IMPLANTAÇÃO ERP MyERP

## 📋 **CENÁRIO: IMPLANTAÇÃO NA EMPRESA "TECH SOLUTIONS LTDA"**

### 🎯 **CONTEXTO DA EMPRESA**
- **Razão Social**: Tech Solutions Ltda
- **CNPJ**: 12.345.678/0001-90
- **Segmento**: Desenvolvimento de Software
- **Funcionários**: 25 colaboradores
- **Plano Contratado**: STANDARD (50 usuários)
- **Necessidades**: Controle de ponto, gestão financeira, biometria

---

## 🚀 **FASE 1: PREPARAÇÃO DO AMBIENTE**

### **1.1 Inicialização do Sistema**
```bash
# Executar scripts de inicialização
cd dev-scripts
start-myerp.bat

# Aguardar todos os serviços subirem
# Verificar logs de cada módulo
```

### **1.2 Verificação de Saúde do Sistema**
```bash
# Monitoring Dashboard
GET http://localhost:8084/api/monitoring/dashboard

# Verificar se todos os 8 serviços estão UP:
# - Eureka Server (8761)
# - Config Server (8888) 
# - API Gateway (8080)
# - Auth Service (8081)
# - RH Module (8082)
# - Biometria Module (8083)
# - Company Module (8085)
# - Financial Module (8086)
```

**✅ Critério de Sucesso**: Todos os serviços com status "UP"

---

## 🏢 **FASE 2: CADASTRO DA EMPRESA (COMPANY MODULE)**

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
Content-Type: application/json

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

**✅ Critério de Sucesso**: Empresa criada com plano STANDARD e feature flags ativas

---

## 🔐 **FASE 3: CONFIGURAÇÃO DE USUÁRIOS (AUTH SERVICE)**

### **3.1 Login com Admin Padrão**
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "login": "admin",
  "senha": "admin123"
}

# Salvar o token JWT retornado
```

### **3.2 Criar Administrador da Empresa**
```bash
POST http://localhost:8080/auth/users
Content-Type: application/json
Authorization: Bearer {token}

{
  "login": "admin.techsol",
  "senha": "TechSol@2024",
  "nome": "Carlos Silva - Administrador",
  "cpf": "12345678901",
  "role": "ADMIN"
}
```

### **3.3 Criar Funcionários**
```bash
# Funcionário 1 - RH
POST http://localhost:8080/auth/users
{
  "login": "maria.rh",
  "senha": "Maria@123",
  "nome": "Maria Santos - RH",
  "cpf": "98765432100",
  "role": "FUNCIONARIO"
}

# Funcionário 2 - Desenvolvedor
POST http://localhost:8080/auth/users
{
  "login": "joao.dev",
  "senha": "Joao@456",
  "nome": "João Oliveira - Desenvolvedor",
  "cpf": "11122233344",
  "role": "FUNCIONARIO"
}

# Funcionário 3 - Financeiro
POST http://localhost:8080/auth/users
{
  "login": "ana.fin",
  "senha": "Ana@789",
  "nome": "Ana Costa - Financeiro",
  "cpf": "55566677788",
  "role": "FUNCIONARIO"
}
```

### **3.4 Validar Tokens**
```bash
POST http://localhost:8080/auth/validate
Authorization: Bearer {token}

# Verificar claims do JWT
```

**✅ Critério de Sucesso**: 4 usuários criados (1 admin + 3 funcionários)

---

## 👥 **FASE 4: GESTÃO DE RH (RH MODULE)**

### **4.1 Login como Admin da Empresa**
```bash
POST http://localhost:8080/auth/login
{
  "login": "admin.techsol",
  "senha": "TechSol@2024"
}
```

### **4.2 Cadastrar Funcionários no Sistema de Ponto**
```bash
# Cadastrar Maria - RH
POST http://localhost:8080/api/admin/funcionarios
Authorization: Bearer {admin_token}
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "login": "maria.rh",
  "senha": "Maria@123",
  "role": "FUNCIONARIO"
}

# Cadastrar João - Desenvolvedor
POST http://localhost:8080/api/admin/funcionarios
{
  "nome": "João Oliveira",
  "cpf": "11122233344", 
  "login": "joao.dev",
  "senha": "Joao@456",
  "role": "FUNCIONARIO"
}

# Cadastrar Ana - Financeiro
POST http://localhost:8080/api/admin/funcionarios
{
  "nome": "Ana Costa",
  "cpf": "55566677788",
  "login": "ana.fin", 
  "senha": "Ana@789",
  "role": "FUNCIONARIO"
}
```

### **4.3 Listar Funcionários Cadastrados**
```bash
GET http://localhost:8080/api/admin/funcionarios
Authorization: Bearer {admin_token}

# Verificar se os 3 funcionários foram cadastrados
```

### **4.4 Teste de Registro de Ponto**
```bash
# Login como funcionário
POST http://localhost:8080/auth/login
{
  "login": "maria.rh",
  "senha": "Maria@123"
}

# Registrar entrada (08:00)
POST http://localhost:8080/api/pontos/{funcionarioId}/registrar
Authorization: Bearer {funcionario_token}

# Verificar registro
GET http://localhost:8080/api/pontos/{funcionarioId}
Authorization: Bearer {funcionario_token}
```

### **4.5 Simulação de Jornada Completa**
```bash
# Maria - Jornada do dia
# 08:00 - Entrada
POST /api/pontos/{mariaId}/registrar

# 12:00 - Saída para almoço  
POST /api/pontos/{mariaId}/registrar

# 13:00 - Retorno do almoço
POST /api/pontos/{mariaId}/registrar

# 17:00 - Saída
POST /api/pontos/{mariaId}/registrar
```

### **4.6 Relatório Administrativo**
```bash
# Admin visualiza todos os pontos
GET http://localhost:8080/api/admin/pontos
Authorization: Bearer {admin_token}

# Verificar registros de todos os funcionários
```

**✅ Critério de Sucesso**: 3 funcionários cadastrados, registros de ponto funcionando

---

## 🔬 **FASE 5: BIOMETRIA E DETECÇÃO (BIOMETRIA MODULE)**

### **5.1 Configurar Câmera de Entrada**
```bash
POST http://localhost:8080/api/biometria/configuracao-camera
Authorization: Bearer {admin_token}
{
  "nomeConfiguracao": "Câmera Entrada Principal",
  "alias": "camera_entrada",
  "tipoCamera": "USB",
  "indiceUsb": 0,
  "resolucao": "1280x720",
  "fps": 30,
  "ativa": true
}
```

### **5.2 Configurar Câmera de Saída**
```bash
POST http://localhost:8080/api/biometria/configuracao-camera
{
  "nomeConfiguracao": "Câmera Saída Principal", 
  "alias": "camera_saida",
  "tipoCamera": "IP",
  "enderecoIp": "192.168.1.100",
  "porta": 8080,
  "usuario": "admin",
  "senha": "camera123",
  "ativa": true
}
```

### **5.3 Cadastrar Usuários Biométricos**
```bash
# Cadastrar Maria para biometria
POST http://localhost:8080/api/biometria/usuarios
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "funcionarioId": {mariaId},
  "ativo": true
}

# Repetir para João e Ana
```

### **5.4 Simular Detecções Automáticas**
```bash
# Simulação: Maria chegando às 08:05 (dentro da tolerância)
POST http://localhost:8080/api/biometria/simular-deteccao?nomeFuncionario=Maria Santos&movimento=ENTRADA

# Simulação: João chegando às 08:25 (fora da tolerância)  
POST http://localhost:8080/api/biometria/simular-deteccao?nomeFuncionario=João Oliveira&movimento=ENTRADA

# Simulação: Ana saindo às 17:10 (dentro da tolerância)
POST http://localhost:8080/api/biometria/simular-deteccao?nomeFuncionario=Ana Costa&movimento=SAIDA
```

### **5.5 Verificar Eventos e Sugestões**
```bash
# Listar todos os eventos
GET http://localhost:8080/api/biometria/eventos

# Listar eventos pendentes de validação (João - fora da tolerância)
GET http://localhost:8080/api/biometria/eventos/pendentes

# Dashboard de estatísticas
GET http://localhost:8080/api/biometria/dashboard
```

**✅ Critério de Sucesso**: Câmeras configuradas, detecções automáticas funcionando, sugestões para RH

---

## 💰 **FASE 6: GESTÃO FINANCEIRA (FINANCIAL MODULE)**

### **6.1 Configurar Dados Bancários**
```bash
POST http://localhost:8080/api/financial/configuracao-bancaria
Authorization: Bearer {admin_token}
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

### **6.4 Simular Conciliação Bancária**
```bash
# Upload de extrato bancário (simulado)
POST http://localhost:8080/api/financial/conciliacao/processar
Content-Type: multipart/form-data
file: extrato_bancario.csv

# Verificar transações conciliadas
GET http://localhost:8080/api/financial/conciliacao/resultado
```

### **6.5 Relatórios Financeiros**
```bash
# Relatório de boletos
GET http://localhost:8080/api/financial/relatorios/boletos?periodo=2024-09

# Relatório PIX
GET http://localhost:8080/api/financial/relatorios/pix?periodo=2024-09

# Dashboard financeiro
GET http://localhost:8080/api/financial/dashboard
```

**✅ Critério de Sucesso**: Boletos e PIX gerados, conciliação funcionando

---

## 📊 **FASE 7: MONITORAMENTO E SAÚDE (MONITORING MODULE)**

### **7.1 Dashboard Geral do Sistema**
```bash
GET http://localhost:8080/api/monitoring/dashboard

# Verificar:
# - Status de todos os 8 serviços
# - Métricas de memória e CPU
# - Uptime do sistema
# - Percentual de saúde geral
```

### **7.2 Monitoramento Individual**
```bash
# Status detalhado dos serviços
GET http://localhost:8080/api/monitoring/services

# Métricas do sistema
GET http://localhost:8080/api/monitoring/metrics

# Health check
GET http://localhost:8080/api/monitoring/health
```

### **7.3 Verificação de Performance**
```bash
# Actuator de cada serviço
GET http://localhost:8081/actuator/health  # Auth
GET http://localhost:8082/actuator/health  # RH
GET http://localhost:8083/actuator/health  # Biometria
GET http://localhost:8085/actuator/health  # Company
GET http://localhost:8086/actuator/health  # Financial
```

**✅ Critério de Sucesso**: Todos os serviços monitorados e saudáveis

---

## 🧪 **FASE 8: TESTES DE INTEGRAÇÃO**

### **8.1 Fluxo Completo: Funcionário Novo**
```bash
# 1. Admin cria usuário no Auth
POST /auth/users (novo funcionário)

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

# 7. Verificar integração
GET /api/admin/pontos (admin vê tudo)
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

### **8.4 Teste de Tolerância a Falhas**
```bash
# Parar um serviço e verificar comportamento
# Monitoring deve detectar serviço DOWN
# Gateway deve retornar erro apropriado
```

**✅ Critério de Sucesso**: Integrações funcionando, segurança validada

---

## 📋 **FASE 9: VALIDAÇÃO FINAL**

### **9.1 Checklist de Funcionalidades**
- [ ] ✅ Empresa cadastrada com plano STANDARD
- [ ] ✅ 4 usuários criados (1 admin + 3 funcionários)
- [ ] ✅ Funcionários cadastrados no RH
- [ ] ✅ Registros de ponto funcionando
- [ ] ✅ Biometria configurada e detectando
- [ ] ✅ Boletos e PIX gerados
- [ ] ✅ Conciliação bancária processada
- [ ] ✅ Monitoramento ativo
- [ ] ✅ Integrações validadas

### **9.2 Relatórios de Implantação**
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

### **9.3 Documentação de Entrega**
- ✅ **Usuários criados**: 4 usuários funcionais
- ✅ **Plano ativo**: STANDARD (50 usuários, features ativas)
- ✅ **Módulos funcionais**: 8 módulos operacionais
- ✅ **Integrações**: Todos os módulos integrados
- ✅ **Monitoramento**: Dashboard ativo
- ✅ **Segurança**: Autenticação e autorização funcionando

---

## 🎯 **CRITÉRIOS DE SUCESSO GERAL**

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

## 📞 **SUPORTE PÓS-IMPLANTAÇÃO**

### **Monitoramento Contínuo**
- Dashboard: http://localhost:8084
- Logs: Verificar console de cada serviço
- Health: Endpoints /actuator/health

### **Backup e Manutenção**
- Backup PostgreSQL diário
- Logs rotacionados
- Monitoramento de performance

### **Treinamento Usuários**
- Admin: Gestão completa do sistema
- RH: Controle de funcionários e pontos
- Funcionários: Registro de ponto

**🎉 IMPLANTAÇÃO CONCLUÍDA COM SUCESSO! 🎉**

**A Tech Solutions Ltda agora possui um ERP completo e funcional com todos os módulos integrados!**