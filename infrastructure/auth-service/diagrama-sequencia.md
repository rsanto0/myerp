# Diagrama de Sequência - Auth Service

## 🔐 Fluxo de Login

```mermaid
sequenceDiagram
    participant C as Cliente
    participant AC as AuthController
    participant UR as UserRepository
    participant JS as JwtService
    participant DB as Database

    C->>AC: POST /auth/login {login, senha}
    AC->>UR: findByLogin(login)
    UR->>DB: SELECT * FROM users WHERE login = ?
    DB-->>UR: User data
    UR-->>AC: User object
    
    alt Usuário não encontrado
        AC-->>C: 401 Unauthorized
    else Senha inválida
        AC-->>C: 401 Unauthorized
    else Credenciais válidas
        AC->>JS: generateToken(login, role, userId)
        JS->>JS: getSigningKey()
        JS->>JS: Jwts.builder().subject().claim().sign()
        JS-->>AC: JWT token
        AC-->>C: 200 OK {token, login, role, userId}
    end
```

## ✅ Fluxo de Validação de Token

```mermaid
sequenceDiagram
    participant C as Cliente
    participant AC as AuthController
    participant JS as JwtService

    C->>AC: POST /auth/validate<br/>Header: Authorization Bearer <token>
    AC->>AC: authHeader.replace("Bearer ", "")
    AC->>JS: validateToken(token)
    JS->>JS: getSigningKey()
    JS->>JS: Jwts.parser().verifyWith().parseSignedClaims()
    
    alt Token válido
        JS-->>AC: Claims {sub, role, userId, iat, exp}
        AC-->>C: 200 OK Claims
    else Token inválido/expirado
        JS-->>AC: Exception
        AC-->>C: 401 Unauthorized
    end
```

## 👤 Fluxo de Criação de Usuário

```mermaid
sequenceDiagram
    participant C as Cliente
    participant AC as AuthController
    participant UR as UserRepository
    participant DB as Database

    C->>AC: POST /auth/users {login, senha, nome, cpf, role}
    AC->>UR: save(user)
    UR->>DB: INSERT INTO users VALUES (...)
    DB-->>UR: User with generated ID
    UR-->>AC: Saved User
    AC-->>C: 200 OK User {id, login, nome, cpf, role}
```

## 🏗️ Fluxo Completo via API Gateway

```mermaid
sequenceDiagram
    participant C as Cliente
    participant GW as API Gateway
    participant AS as Auth Service
    participant SP as Sistema Ponto

    Note over C,SP: 1. Login inicial
    C->>GW: POST /auth/login {login, senha}
    GW->>AS: POST /auth/login {login, senha}
    AS-->>GW: {token, login, role, userId}
    GW-->>C: {token, login, role, userId}

    Note over C,SP: 2. Requisição protegida
    C->>GW: GET /api/funcionarios<br/>Authorization: Bearer <token>
    GW->>GW: JwtAuthFilter.filter()
    GW->>AS: POST /auth/validate<br/>Authorization: Bearer <token>
    AS-->>GW: Claims {sub, role, userId}
    GW->>GW: Adicionar headers:<br/>X-User-Id, X-User-Login, X-User-Role
    GW->>SP: GET /funcionarios<br/>Headers: X-User-*
    SP-->>GW: Lista funcionários
    GW-->>C: Lista funcionários
```

## 🔑 Componentes JWT

```mermaid
graph TD
    A[JwtService] --> B[generateToken]
    A --> C[validateToken]
    A --> D[getSigningKey]
    
    B --> E[Jwts.builder]
    E --> F[subject: login]
    E --> G[claim: role]
    E --> H[claim: userId]
    E --> I[issuedAt: now]
    E --> J[expiration: now + 24h]
    E --> K[signWith: HMAC-SHA]
    
    C --> L[Jwts.parser]
    L --> M[verifyWith: secret]
    L --> N[parseSignedClaims]
    N --> O[Claims payload]
    
    D --> P[Keys.hmacShaKeyFor]
    P --> Q[secret.getBytes]
```

## 📊 Estados de Resposta

| Endpoint | Sucesso | Erro |
|----------|---------|------|
| `/auth/login` | 200 + AuthResponse | 401 Unauthorized |
| `/auth/validate` | 200 + Claims | 401 Unauthorized |
| `/auth/users` | 200 + User | 500 Internal Error |

## 🔄 Ciclo de Vida do Token

1. **Geração**: Login válido → JWT criado com 24h de validade
2. **Uso**: Token enviado em requests protegidos via header
3. **Validação**: Gateway valida token antes de rotear
4. **Expiração**: Token expira após 24h, necessário novo login