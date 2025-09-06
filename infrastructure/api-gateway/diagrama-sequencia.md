# Diagrama de Sequência - API Gateway

## 🚪 Fluxo de Roteamento Público (Auth Service)

```mermaid
sequenceDiagram
    participant C as Cliente
    participant GW as API Gateway
    participant AS as Auth Service

    C->>GW: POST /auth/login {login, senha}
    Note over GW: Rota pública - sem JWT
    GW->>AS: POST /auth/login {login, senha}
    AS-->>GW: 200 {token, login, role, userId}
    GW-->>C: 200 {token, login, role, userId}
```

## 🔐 Fluxo de Roteamento Protegido (Sistema Ponto)

```mermaid
sequenceDiagram
    participant C as Cliente
    participant GW as API Gateway
    participant JAF as JwtAuthFilter
    participant SP as Sistema Ponto

    C->>GW: GET /api/funcionarios<br/>Authorization: Bearer <token>
    GW->>JAF: apply(config)
    JAF->>JAF: Extrair header Authorization
    
    alt Token ausente/inválido
        JAF->>JAF: validateToken() throws Exception
        JAF-->>GW: 401 Unauthorized
        GW-->>C: 401 Unauthorized
    else Token válido
        JAF->>JAF: validateToken(token)
        JAF->>JAF: Claims {sub, role, userId}
        JAF->>JAF: Adicionar headers:<br/>X-User-Id, X-User-Login, X-User-Role
        JAF->>SP: GET /funcionarios<br/>Headers: X-User-*
        SP-->>JAF: Lista funcionários
        JAF-->>GW: Lista funcionários
        GW-->>C: Lista funcionários
    end
```

## 🔄 Fluxo Completo de Autenticação + Acesso

```mermaid
sequenceDiagram
    participant C as Cliente
    participant GW as API Gateway
    participant AS as Auth Service
    participant JAF as JwtAuthFilter
    participant SP as Sistema Ponto

    Note over C,SP: 1. Login (Rota Pública)
    C->>GW: POST /auth/login {login, senha}
    GW->>AS: POST /auth/login {login, senha}
    AS->>AS: Validar credenciais + gerar JWT
    AS-->>GW: {token, login, role, userId}
    GW-->>C: {token, login, role, userId}

    Note over C,SP: 2. Acesso Protegido
    C->>GW: POST /api/pontos/1/registrar<br/>Authorization: Bearer <token>
    GW->>JAF: Aplicar filtro JWT
    JAF->>JAF: validateToken(token)
    JAF->>JAF: Extrair claims {userId: 1, role: ADMIN}
    JAF->>SP: POST /pontos/1/registrar<br/>X-User-Id: 1<br/>X-User-Login: admin<br/>X-User-Role: ADMIN
    SP->>SP: Processar registro usando headers
    SP-->>JAF: 200 Registro criado
    JAF-->>GW: 200 Registro criado
    GW-->>C: 200 Registro criado
```

## ⚙️ Fluxo Interno do JwtAuthFilter

```mermaid
sequenceDiagram
    participant GW as Gateway
    participant JAF as JwtAuthFilter
    participant JWT as JWT Library

    GW->>JAF: apply(config) - criar filtro
    JAF->>JAF: return (exchange, chain) -> { ... }
    
    Note over JAF: Para cada request
    JAF->>JAF: Extrair Authorization header
    JAF->>JAF: Verificar "Bearer " prefix
    JAF->>JAF: token = authHeader.substring(7)
    JAF->>JWT: validateToken(token)
    JWT->>JWT: Jwts.parser().verifyWith(key).parseSignedClaims()
    JWT-->>JAF: Claims {sub, role, userId, iat, exp}
    JAF->>JAF: exchange.getRequest().mutate().header()
    JAF->>GW: chain.filter(exchange) - continuar
```

## 🏗️ Arquitetura de Roteamento

```mermaid
graph TD
    A[Cliente :8080] --> B[API Gateway]
    
    B --> C{Rota?}
    C -->|/auth/**| D[Auth Service :8081]
    C -->|/api/**| E[JwtAuthFilter]
    
    E --> F{Token válido?}
    F -->|Não| G[401 Unauthorized]
    F -->|Sim| H[Sistema Ponto :8082]
    
    D --> I[Resposta Direta]
    H --> J[Resposta com Headers]
    
    I --> B
    J --> B
    B --> A
```

## 📊 Configuração de Rotas (application.yml)

```yaml
spring:
  cloud:
    gateway:
      routes:
        # Rota Pública - Auth Service
        - id: auth-service
          uri: http://localhost:8081
          predicates:
            - Path=/auth/**
          # Sem filtros JWT
        
        # Rota Protegida - Sistema Ponto
        - id: sistema-ponto
          uri: http://localhost:8082
          predicates:
            - Path=/api/**
          filters:
            - name: JwtAuthFilter  # Aplica validação JWT
            - StripPrefix=1        # Remove /api do path
```

## 🔑 Headers Injetados pelo Gateway

| Header Original | Headers Injetados |
|----------------|-------------------|
| `Authorization: Bearer <token>` | `X-User-Id: 1` |
| | `X-User-Login: admin` |
| | `X-User-Role: ADMIN` |

## 📈 Estados de Resposta

| Cenário | Status | Descrição |
|---------|--------|-----------|
| Rota pública | 200/401 | Depende do auth-service |
| Token ausente | 401 | Unauthorized |
| Token inválido | 401 | Unauthorized |
| Token válido | 200/4xx/5xx | Depende do microserviço |

## 🔄 Ciclo de Vida da Requisição

1. **Recepção**: Gateway recebe request na porta 8080
2. **Roteamento**: Identifica rota baseada no path
3. **Filtros**: Aplica JwtAuthFilter se rota protegida
4. **Validação**: Valida JWT usando secret compartilhado
5. **Headers**: Injeta informações do usuário
6. **Proxy**: Encaminha para microserviço de destino
7. **Resposta**: Retorna resposta do microserviço ao cliente