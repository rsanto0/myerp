# Segurança - API Gateway

## 🛡️ Arquitetura de Segurança

### Camadas de Proteção
1. **Autenticação JWT** - Gateway valida tokens
2. **Autorização por Roles** - Microserviços validam permissões
3. **Headers de Contexto** - Informações de usuário injetadas

## 🔐 Fluxo de Segurança

### 1. Autenticação (Gateway)
```
Cliente → JWT Token → Gateway
├── Token Válido → Injeta Headers → Microserviço
└── Token Inválido → 401 Unauthorized
```

### 2. Autorização (Microserviço)
```
Headers Recebidos:
├── X-User-Id: <userId>
├── X-User-Login: <login>
└── X-User-Role: <FUNCIONARIO|ADMIN>

Validação:
├── Endpoint Público → Sem validação
├── Endpoint Protegido → JWT obrigatório
└── Endpoint Admin → JWT + Role ADMIN
```

## 📋 Controle de Acesso por Endpoint

### Auth Service (Público)
- `POST /auth/login` - ✅ Sem autenticação
- `POST /auth/validate` - ✅ JWT obrigatório

### Sistema Ponto (Protegido)
- `POST /pontos/{id}/registrar` - 🔒 JWT obrigatório
- `GET /pontos/{id}` - 🔒 JWT obrigatório

### Administração (Admin)
- `GET /admin/funcionarios` - 👑 JWT + Role ADMIN
- `POST /admin/funcionarios` - 👑 JWT + Role ADMIN
- `DELETE /admin/funcionarios/{id}` - 👑 JWT + Role ADMIN
- `GET /admin/pontos` - 👑 JWT + Role ADMIN
- `DELETE /admin/pontos/{id}` - 👑 JWT + Role ADMIN

## 🚨 Códigos de Resposta

| Código | Descrição | Causa |
|--------|-----------|-------|
| 200 | OK | Acesso autorizado |
| 401 | Unauthorized | JWT inválido/ausente |
| 403 | Forbidden | Role insuficiente |
| 404 | Not Found | Recurso não encontrado |

## 🔍 Logs de Segurança

### Gateway
- `[JWT]` - Validação de tokens
- `[FILTER]` - Aplicação de filtros

### Microserviços
- `[ADMIN_ACCESS_GRANTED]` - Acesso admin autorizado
- `[ADMIN_ACCESS_DENIED]` - Tentativa de acesso negada

## ⚠️ Considerações de Segurança

### Implementado
- ✅ Validação JWT
- ✅ Controle de acesso por roles
- ✅ Headers de contexto seguros
- ✅ Logs de auditoria

### Melhorias Recomendadas
- 🔄 Rate limiting
- 🔄 CORS configurado
- 🔄 HTTPS obrigatório
- 🔄 Refresh tokens
- 🔄 Auditoria completa

## 🧪 Testando Segurança

### Cenário 1: Usuário FUNCIONARIO tenta acessar admin
```bash
# Login como funcionário
POST /auth/login {"login":"funcionario","senha":"123"}

# Tentativa de acesso admin (deve falhar)
GET /api/admin/funcionarios
# Resposta: 403 Forbidden
```

### Cenário 2: Usuário ADMIN acessa recursos
```bash
# Login como admin
POST /auth/login {"login":"admin","senha":"admin123"}

# Acesso admin (deve funcionar)
GET /api/admin/funcionarios
# Resposta: 200 OK + lista de funcionários
```