# Auth Service 🔐

## 🎭 Analogia: Segurança de um Shopping Center

Imagine o **Auth Service** como o **posto de segurança** na entrada de um shopping. Ele verifica quem você é e te dá um **crachá de visitante** (JWT Token).

### 🛡️ Papel do Segurança (Auth Service)
- **Verifica identidade:** "Seu nome e senha, por favor"
- **Emite crachá:** "Aqui está seu crachá válido por 24 horas"
- **Valida crachá:** "Deixe-me verificar se seu crachá é válido"
- **Define permissões:** "Você é ADMIN ou FUNCIONARIO?"

## 🎯 Função Principal
**Autenticação e Autorização Centralizada**

### 📋 Responsabilidades
1. **Login:** Validar credenciais (usuário/senha)
2. **Geração JWT:** Criar tokens seguros
3. **Validação JWT:** Verificar se token é válido
4. **Gerenciamento de Usuários:** CRUD de usuários
5. **Controle de Roles:** ADMIN vs FUNCIONARIO

## 🌟 Importância no Sistema
- **🔒 Segurança:** Ponto único de autenticação
- **🎫 Tokens JWT:** Evita login repetido
- **👥 Controle de Acesso:** Define quem pode fazer o quê
- **🔄 Centralização:** Uma fonte de verdade para usuários

## 🗄️ Estrutura do Banco (auth_db)

### Tabela: users
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    login VARCHAR(255) UNIQUE,
    senha VARCHAR(255),
    nome VARCHAR(255),
    cpf VARCHAR(255),
    role VARCHAR(255) CHECK (role IN ('FUNCIONARIO','ADMIN'))
);
```

## 🔄 Fluxo de Relacionamento

```mermaid
graph TD
    A[Auth Service :8081] --> B[PostgreSQL auth_db]
    A --> C[JWT Token Generation]
    A --> D[User Management]
    
    E[API Gateway :8080] --> A
    F[Frontend/Postman] --> E
    
    G[RH Module :8082] -.-> A
    H[Biometria Module :8083] -.-> A
    
    A --> I[Eureka Server :8761]
    
    J[Admin Users] --> A
    K[Employee Users] --> A
    
    style A fill:#e8f5e8
    style B fill:#e1f5fe
    style C fill:#fff3e0
    style E fill:#f3e5f5
```

## 🚦 Fluxo de Autenticação

### 1️⃣ **Login do Usuário**
```
1. User → API Gateway: POST /auth/login {login, senha}
2. API Gateway → Auth Service: Encaminha requisição
3. Auth Service → PostgreSQL: Busca usuário
4. Auth Service: Valida senha
5. Auth Service: Gera JWT Token
6. Auth Service → User: Retorna {token, role, userId}
```

### 2️⃣ **Uso do Token**
```
1. User → API Gateway: GET /api/funcionarios + Header: Authorization: Bearer <token>
2. API Gateway → Auth Service: POST /auth/validate + token
3. Auth Service: Valida JWT
4. Auth Service → API Gateway: {userId, login, role}
5. API Gateway: Injeta headers X-User-* 
6. API Gateway → RH Module: Requisição com headers
```

### 3️⃣ **Criação de Usuário**
```
1. Admin → API Gateway: POST /auth/users {nome, cpf, login, senha, role}
2. API Gateway → Auth Service: Cria usuário
3. Auth Service → PostgreSQL: INSERT INTO users
4. Auth Service → Admin: Usuário criado com sucesso
```

## 📡 Endpoints Principais

### 🔓 Públicos (sem autenticação)
```http
POST /auth/login          # Login do usuário
POST /auth/validate       # Validação de token (usado pelo Gateway)
POST /auth/users          # Criação de usuários
```

### 📊 Exemplos de Uso

#### Login
```json
POST /auth/login
{
  "login": "admin",
  "senha": "admin123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "login": "admin",
  "role": "ADMIN",
  "userId": 1
}
```

#### Validação de Token
```http
POST /auth/validate
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

Response:
{
  "sub": "admin",
  "role": "ADMIN", 
  "userId": 1,
  "exp": 1234567890
}
```

## 🔐 Segurança JWT

### 🎫 Estrutura do Token
```
Header: {"alg": "HS256", "typ": "JWT"}
Payload: {"sub": "admin", "role": "ADMIN", "userId": 1, "exp": 1234567890}
Signature: HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

### ⏰ Configurações
- **Expiração:** 24 horas (86400000ms)
- **Algoritmo:** HMAC-SHA256
- **Secret:** Compartilhado com API Gateway

## 👥 Usuários Padrão

### Administrador
```
Login: admin
Senha: admin123
Role: ADMIN
```

### Funcionário Exemplo
```
Login: funcionario
Senha: 123456
Role: FUNCIONARIO
```

## ⚠️ Pontos Críticos

### 🔑 **Secret JWT**
- Deve ser o mesmo em Auth Service e API Gateway
- Nunca expor em logs ou código público

### 🗄️ **Banco de Dados**
- Senhas em texto plano (melhorar com BCrypt)
- Backup regular da tabela users

### 🔄 **Dependências**
- PostgreSQL deve estar rodando
- Eureka Server para registro

## 🛠️ Troubleshooting

### Problema: "401 Unauthorized"
**Soluções:**
- Verificar credenciais
- Verificar se token não expirou
- Confirmar secret JWT

### Problema: "Connection refused"
**Soluções:**
- Verificar se PostgreSQL está rodando
- Confirmar configurações de banco

## 🎯 Resumo da Analogia

**Auth Service** = **Segurança do Shopping**
- **Verifica identidade** na entrada
- **Emite crachá** (JWT Token) válido
- **Controla acesso** às diferentes áreas
- **Monitora permissões** (ADMIN vs FUNCIONARIO)

**Sem a segurança, qualquer um entra em qualquer lugar!** 🛡️✨