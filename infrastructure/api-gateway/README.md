# API Gateway

[![Sync Postman Collection](https://github.com/rsanto0/api-gateway/actions/workflows/sync-postman.yml/badge.svg?branch=main)](https://github.com/rsanto0/api-gateway/actions/workflows/sync-postman.yml)


Gateway de API desenvolvido com Spring Cloud Gateway para roteamento e autenticação JWT de microserviços.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Cloud Gateway 2023.0.3**
- **Spring WebFlux**
- **JWT (jsonwebtoken 0.12.3)**
- **Maven**

## 📋 Funcionalidades

- ✅ Roteamento de requisições para microserviços
- ✅ Autenticação JWT automática
- ✅ Injeção de headers de usuário
- ✅ Logs estruturados
- ✅ Configuração declarativa de rotas

## 🏗️ Arquitetura

```
src/main/java/com/exemplo/gateway/
├── filter/             # Filtros personalizados
│   └── JwtAuthFilter   # Validação JWT
├── config/             # Configurações
└── GatewayApplication  # Classe principal
```

## 🔧 Configuração

### Servidor
- **Porta**: 8080

### JWT
- **Algoritmo**: HMAC-SHA
- **Secret**: Configurável via `jwt.secret`

### Rotas Configuradas

#### Auth Service (Público)
- **Padrão**: `/auth/**`
- **Destino**: http://localhost:8081
- **Autenticação**: Não requerida

#### Sistema Ponto (Protegido)
- **Padrão**: `/api/**`
- **Destino**: http://localhost:8082
- **Autenticação**: JWT obrigatório
- **Filtros**: JwtAuthFilter + StripPrefix

## 🔐 Autenticação JWT

### Headers Injetados
Após validação do JWT, o gateway adiciona headers para os microserviços:

```
X-User-Id: <userId>
X-User-Login: <login>
X-User-Role: <role>
```

### Fluxo de Autenticação
1. Cliente envia requisição com `Authorization: Bearer <token>`
2. Gateway valida JWT usando secret compartilhado
3. Se válido, injeta headers de usuário
4. Encaminha para microserviço de destino
5. Se inválido, retorna 401 Unauthorized

## 📡 Roteamento

### Requisições Públicas
```bash
# Login (sem autenticação)
POST http://localhost:8080/auth/login

# Validação de token
POST http://localhost:8080/auth/validate

# Criação de usuários
POST http://localhost:8080/auth/users
```

### Requisições Protegidas (Funcionarios)
```bash
# Sistema de ponto (requer JWT)
POST http://localhost:8080/api/pontos/1/registrar
GET  http://localhost:8080/api/pontos/1

# Headers obrigatórios
Authorization: Bearer <jwt-token>
```

### Requisições Administrativas (Apenas ADMIN)
```bash
# Administração (requer JWT + Role ADMIN)
GET    http://localhost:8080/api/admin/funcionarios
POST   http://localhost:8080/api/admin/funcionarios  # Criar Funcionário/Admin
DELETE http://localhost:8080/api/admin/funcionarios/1
GET    http://localhost:8080/api/admin/pontos
DELETE http://localhost:8080/api/admin/pontos/1

# Headers obrigatórios
Authorization: Bearer <jwt-token-admin>

# Exemplo: Criar Funcionário
POST http://localhost:8080/api/admin/funcionarios
{
  "nome": "João Silva",
  "cpf": "98765432100",
  "login": "joao",
  "senha": "1234",
  "role": "FUNCIONARIO"
}

# Exemplo: Criar Administrador
POST http://localhost:8080/api/admin/funcionarios
{
  "nome": "Maria Admin",
  "cpf": "12345678900",
  "login": "maria.admin",
  "senha": "admin456",
  "role": "ADMIN"
}
```

## 🏃♂️ Executando

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Auth Service rodando na porta 8081
- Sistema Ponto rodando na porta 8082

### Comandos
```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

Gateway disponível em: http://localhost:8080

## 📚 Collection Postman

O projeto inclui uma collection Postman completa (`api-gateway-postman.json`) com:

- ✅ Todos os endpoints do Auth Service
- ✅ Todos os endpoints do Sistema Ponto
- ✅ Variáveis de ambiente pré-configuradas
- ✅ Scripts automáticos para captura de JWT
- ✅ Sincronização automática via GitHub Actions

### Importar Collection
1. Abra o Postman
2. Import → Upload Files
3. Selecione `api-gateway-postman.json`
4. Configure as variáveis de ambiente se necessário

## 🔄 Integração com Microserviços

### Auth Service (8081)
- Endpoints públicos para login e validação
- Geração de tokens JWT
- Gerenciamento de usuários

### Sistema Ponto (8082)
- Endpoints protegidos por JWT
- Recebe headers de usuário do gateway
- Controle de ponto e funcionários

## 🔐 Controle de Acesso

### Headers Injetados pelo Gateway
Após validação do JWT, o gateway injeta headers para controle de acesso:

```
X-User-Id: <userId>
X-User-Login: <login>
X-User-Role: <role>  # FUNCIONARIO ou ADMIN
```

### Proteção por Roles
- **Endpoints Públicos**: Sem autenticação
- **Endpoints Protegidos**: JWT válido obrigatório
- **Endpoints Admin**: JWT válido + Role ADMIN obrigatório

### Respostas de Segurança
- **401 Unauthorized**: Token JWT inválido/ausente
- **403 Forbidden**: Usuário sem permissão (role insuficiente)

## 📊 Logs

Logs DEBUG habilitados para `com.exemplo.gateway`:

- `[JWT]` - Validação de tokens
- `[ROUTE]` - Roteamento de requisições
- `[FILTER]` - Execução de filtros
- `[ADMIN_ACCESS]` - Controle de acesso administrativo

## 🤖 GitHub Actions

### Workflow: Sync Postman Collection
- **Trigger**: Push com alterações em `*-postman.json`
- **Função**: Sincroniza collection com Postman Cloud
- **Configuração**: Requer secrets `POSTMAN_API_KEY` e `COLLECTION_UID`
- **Execução Manual**: Disponível via GitHub Actions

## ⚙️ Configuração Avançada

### Personalizar Rotas
Edite `application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: novo-servico
          uri: http://localhost:8083
          predicates:
            - Path=/novo/**
          filters:
            - name: JwtAuthFilter
```

### Configurar JWT Secret
```yaml
jwt:
  secret: sua-chave-secreta-aqui
```

## 🛡️ Segurança

### Pontos de Atenção
- Secret JWT deve ser o mesmo em todos os serviços
- CORS não configurado
- Rate limiting não implementado

### Melhorias Recomendadas
1. Configurar CORS para produção
2. Implementar rate limiting
3. Adicionar circuit breaker
4. Configurar SSL/TLS
5. Implementar cache de validação JWT

## 🔧 Troubleshooting

### Erro 401 - Unauthorized
- Verificar se token JWT está no header
- Validar formato: `Authorization: Bearer <token>`
- Confirmar se secret é o mesmo do auth-service

### Erro 503 - Service Unavailable
- Verificar se microserviços estão rodando
- Confirmar portas configuradas (8081, 8082)
- Checar conectividade de rede

### Logs de Debug
```bash
# Habilitar logs detalhados
logging.level.com.exemplo.gateway=DEBUG
```

## 📝 Exemplo de Uso

### 1. Obter Token
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","senha":"admin123"}'
```

### 2. Usar Token
```bash
curl -X GET http://localhost:8080/api/funcionarios \
  -H "Authorization: Bearer <token-jwt>"
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.