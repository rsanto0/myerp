# Collections Postman - MyERP 📮

## 🎭 Analogia: Manual de Instruções para Visitantes

Imagine as **Collections do Postman** como **manuais de instruções** que você dá para visitantes da empresa, explicando como usar cada departamento e testar se tudo está funcionando.

## 📋 Collections Disponíveis

### 1️⃣ **MyERP-System.postman_collection.json**
**Analogia:** Manual completo do visitante
- ✅ Todos os endpoints organizados por módulo
- ✅ Scripts automáticos para salvar tokens
- ✅ Exemplos de uso de cada funcionalidade
- ✅ Documentação inline de cada request

### 2️⃣ **MyERP-Tests.postman_collection.json**
**Analogia:** Checklist de inspeção de qualidade
- ✅ Testes automatizados com validações
- ✅ Fluxos completos de usuário
- ✅ Verificação de segurança e permissões
- ✅ Relatórios de teste automáticos

### 3️⃣ **MyERP-Environment.postman_environment.json**
**Analogia:** Lista de endereços e contatos
- ✅ URLs de todos os serviços
- ✅ Variáveis para tokens JWT
- ✅ IDs de usuários para testes
- ✅ Configurações de ambiente

## 🚀 Como Usar

### 📥 **Importar no Postman**
1. Abra o Postman
2. Clique em **Import**
3. Selecione os 3 arquivos JSON
4. Clique em **Import**

### 🌍 **Configurar Environment**
1. Selecione **MyERP - Development** no dropdown de environments
2. As variáveis serão preenchidas automaticamente pelos scripts

### 🔄 **Executar Fluxos**

#### **Fluxo Básico:**
```
1. Auth Service → Login Admin
2. RH Module → Listar Funcionários (Admin)
3. Biometria Module → Simular Detecção
4. Biometria Module → Dashboard
```

#### **Fluxo de Funcionário:**
```
1. Auth Service → Login Funcionário
2. RH Module → Registrar Ponto
3. RH Module → Consultar Pontos
```

## 📊 Estrutura das Collections

### 🔐 **Auth Service**
```
├── Login Admin (salva jwt_token)
├── Login Funcionário (salva jwt_token_funcionario)
├── Validar Token
└── Criar Usuário
```

### 👥 **RH Module**
```
├── Registrar Ponto (Funcionário)
├── Consultar Pontos (Funcionário)
├── Listar Funcionários (Admin)
├── Criar Funcionário (Admin)
└── Listar Todos os Pontos (Admin)
```

### 📷 **Biometria Module**
```
├── Simular Detecção - Entrada
├── Simular Detecção - Saída
├── Listar Eventos de Detecção
├── Dashboard Biometria
└── Listar Sugestões para RH
```

## 🧪 Testes Automatizados

### ✅ **Validações Incluídas**
- Status codes corretos (200, 403, 401)
- Estrutura das respostas JSON
- Presença de campos obrigatórios
- Validação de tokens JWT
- Controle de permissões por role

### 📈 **Relatórios de Teste**
Execute a collection de testes e veja:
- ✅ Quantos testes passaram
- ❌ Quais testes falharam
- ⏱️ Tempo de execução
- 📊 Cobertura de endpoints

## 🔧 Scripts Automáticos

### 🎫 **Captura de Tokens**
```javascript
// Salva token automaticamente após login
if (pm.response.code === 200) {
    const response = pm.response.json();
    pm.environment.set('jwt_token', response.token);
    pm.environment.set('user_id', response.userId);
}
```

### 🔍 **Validações de Teste**
```javascript
pm.test('Login realizado com sucesso', function () {
    pm.response.to.have.status(200);
});

pm.test('Token JWT retornado', function () {
    const response = pm.response.json();
    pm.expect(response.token).to.not.be.empty;
});
```

## 🌍 Variáveis de Environment

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `base_url` | URL do API Gateway | http://localhost:8080 |
| `eureka_url` | URL do Eureka Server | http://localhost:8761 |
| `auth_url` | URL do Auth Service | http://localhost:8081 |
| `rh_url` | URL do RH Module | http://localhost:8082 |
| `biometria_url` | URL do Biometria Module | http://localhost:8083 |
| `jwt_token` | Token do Admin | (preenchido automaticamente) |
| `jwt_token_funcionario` | Token do Funcionário | (preenchido automaticamente) |
| `user_id` | ID do usuário logado | (preenchido automaticamente) |

## 🎯 Fluxos de Teste Recomendados

### 🔄 **Teste Completo do Sistema**
1. Execute **MyERP-Tests** collection
2. Verifique se todos os testes passaram
3. Analise relatório de cobertura

### 🐛 **Debug de Problemas**
1. Use **MyERP-System** collection
2. Execute requests individuais
3. Analise responses e logs

### 📊 **Monitoramento**
1. Configure **Monitor** no Postman
2. Execute testes periodicamente
3. Receba alertas de falhas

## ⚠️ Pré-requisitos

### 🚀 **Sistema Rodando**
- PostgreSQL (porta 5432)
- Eureka Server (porta 8761)
- Auth Service (porta 8081)
- API Gateway (porta 8080)
- RH Module (porta 8082)
- Biometria Module (porta 8083)

### 👥 **Usuários Padrão**
- **Admin:** login=admin, senha=admin123
- **Funcionário:** login=funcionario, senha=123456

## 🎯 Resumo da Analogia

**Collections Postman** = **Manual de Instruções da Empresa**
- **📋 Manual Completo** (System Collection) - Como usar cada departamento
- **✅ Checklist de Qualidade** (Tests Collection) - Verificar se tudo funciona
- **📞 Lista de Contatos** (Environment) - Endereços e informações importantes

**Com esses manuais, qualquer visitante consegue usar a empresa sem se perder!** 📮✨