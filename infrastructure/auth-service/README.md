# Auth Service

Microserviço de autenticação desenvolvido em Spring Boot para gerenciamento de usuários e tokens JWT.

[![Postman Sync](https://github.com/rsanto0/auth-service/actions/workflows/sync-postman.yml/badge.svg?branch=main)](https://github.com/rsanto0/auth-service/actions/workflows/sync-postman.yml)
[![GitHub release](https://img.shields.io/github/v/release/rsanto0/auth-service)](https://github.com/rsanto0/auth-/releases)
[![License](https://img.shields.io/github/license/rsanto0/auth-service)](https://github.com/rsanto0/auth-service/blob/main/LICENSE)

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Data JPA**
- **H2 Database**
- **JWT (jsonwebtoken 0.12.3)**
- **Maven**

## 📋 Funcionalidades

- ✅ Autenticação de usuários
- ✅ Geração de tokens JWT
- ✅ Validação de tokens
- ✅ Criação de usuários
- ✅ Controle de roles (FUNCIONARIO/ADMIN)

## 🏗️ Arquitetura

```
src/main/java/com/exemplo/auth/
├── controller/     # Endpoints REST
├── service/        # Lógica de negócio
├── repository/     # Acesso a dados
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
└── config/         # Configurações
```

## 🔧 Configuração

### Banco de Dados
- **Tipo**: H2 em memória
- **URL**: `jdbc:h2:mem:auth_db`
- **Console**: http://localhost:8081/h2-console

### JWT
- **Expiração**: 24 horas (86400000ms)
- **Algoritmo**: HMAC-SHA

### Servidor
- **Porta**: 8081

## 👥 Usuários Padrão

O sistema cria automaticamente usuários padrão na inicialização:

### Administrador
- **Login**: `admin`
- **Senha**: `admin123`
- **Role**: `ADMIN`
- **Nome**: Administrador do Sistema
- **CPF**: 00000000000

### Funcionário Exemplo
- **Login**: `funcionario`
- **Senha**: `123456`
- **Role**: `FUNCIONARIO`
- **Nome**: Funcionário Exemplo
- **CPF**: 11111111111

## 📡 API Endpoints

### POST /auth/login
Autentica usuário e retorna token JWT.

**Request:**
```json
{
  "login": "admin",
  "senha": "admin123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "login": "admin",
  "role": "ADMIN",
  "userId": 1
}
```

**Response (401):** Credenciais inválidas

### POST /auth/validate
Valida token JWT e retorna claims.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "sub": "admin",
  "role": "ADMIN",
  "userId": 1,
  "iat": 1234567890,
  "exp": 1234654290
}
```

**Response (401):** Token inválido

### POST /auth/users
Cria novo usuário no sistema.

**Request:**
```json
{
  "login": "newuser",
  "senha": "password123",
  "nome": "Nome Completo",
  "cpf": "12345678901",
  "role": "FUNCIONARIO"
}
```

**Response (200):**
```json
{
  "id": 2,
  "login": "newuser",
  "nome": "Nome Completo",
  "cpf": "12345678901",
  "role": "FUNCIONARIO"
}
```

## 🏃‍♂️ Executando

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Comandos
```bash
# Compilar
mvn clean compile

# Executar testes
mvn test

# Executar aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: http://localhost:8081

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar com relatório de cobertura
mvn test jacoco:report
```

### Cobertura de Testes
- ✅ AuthController - Login válido/inválido
- ✅ AuthController - Validação de token
- ✅ AuthController - Criação de usuários
- ✅ JwtService - Geração/validação de tokens
- ✅ UserRepository - Busca por login

## 📊 Logs

O sistema utiliza logs estruturados com prefixos identificadores:

- `[LOGIN]` - Operações de autenticação
- `[VALIDATE]` - Validação de tokens
- `[CREATE_USER]` - Criação de usuários
- `[INIT]` - Inicialização de dados padrão

**Níveis:**
- **INFO**: Operações bem-sucedidas
- **WARN**: Falhas de autenticação/validação
- **ERROR**: Erros de sistema
- **DEBUG**: Informações detalhadas

## 🔒 Segurança

### ⚠️ Pontos de Atenção
- Senhas armazenadas em texto plano (implementar BCrypt)
- Falta validação de entrada nos endpoints
- CORS não configurado
- Tratamento de erros básico

### 🛡️ Melhorias Recomendadas
1. Implementar hash de senhas com BCrypt
2. Adicionar validação de entrada (@Valid)
3. Configurar CORS para produção
4. Implementar rate limiting
5. Adicionar auditoria de acessos

## 🗃️ Modelo de Dados

### User
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | Identificador único |
| login | String | Login único do usuário |
| senha | String | Senha (texto plano) |
| nome | String | Nome completo |
| cpf | String | CPF do usuário |
| role | Role | FUNCIONARIO ou ADMIN |

### Role (Enum)
- `FUNCIONARIO` - Usuário padrão
- `ADMIN` - Administrador

## 📝 Exemplos de Uso

### Fluxo de Autenticação
1. **Login**: `POST /auth/login` com credenciais
2. **Receber token**: JWT válido por 24h
3. **Usar token**: Header `Authorization: Bearer <token>`
4. **Validar**: `POST /auth/validate` quando necessário

### Criação de Usuário Admin
```bash
curl -X POST http://localhost:8081/auth/users \
  -H "Content-Type: application/json" \
  -d '{
    "login": "admin",
    "senha": "admin123",
    "nome": "Administrador",
    "cpf": "00000000000",
    "role": "ADMIN"
  }'
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.