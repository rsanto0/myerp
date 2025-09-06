# Sistema de Ponto

Sistema de controle de ponto eletrônico desenvolvido em Spring Boot.

[![Postman Sync](https://github.com/rsanto0/sistema-ponto/actions/workflows/sync-postman.yml/badge.svg?branch=main)](https://github.com/rsanto0/sistema-ponto/actions/workflows/sync-postman.yml)
[![GitHub release](https://img.shields.io/github/v/release/rsanto0/sistema-ponto)](https://github.com/rsanto0/sistema-ponto/releases)
[![License](https://img.shields.io/github/license/rsanto0/sistema-ponto)](https://github.com/rsanto0/sistema-ponto/blob/main/LICENSE)

## Tecnologias

- Java 17
- Spring Boot 3.3.2
- Spring Data JPA
- Spring Security
- H2 Database
- Jakarta Validation

## Funcionalidades

### Funcionários (Role: FUNCIONARIO)
- Registro de ponto (entrada/saída)
- Consulta de registros de ponto próprios

### Administradores (Role: ADMIN)
- **Herança**: Administrador extends Funcionario
- **Gerenciamento de Funcionários**: criar/remover/listar
- **Visualização Completa**: todos os registros de ponto
- **Remoção**: registros de ponto
- **Controle de Acesso**: Validação de role via header X-User-Role

## API Endpoints

### Registros de Ponto (Protegido)
```
POST   /pontos/{id}/registrar  - Registrar ponto [FUNCIONARIO|ADMIN]
GET    /pontos/{id}            - Listar pontos do funcionário [FUNCIONARIO|ADMIN]
```

### Administração (Apenas ADMIN)
```
GET    /admin/funcionarios     - Listar funcionários [ADMIN]
POST   /admin/funcionarios     - Criar funcionário [ADMIN]
DELETE /admin/funcionarios/{id} - Remover funcionário [ADMIN]
GET    /admin/pontos           - Listar todos os pontos [ADMIN]
DELETE /admin/pontos/{id}       - Remover ponto [ADMIN]
```

### Headers de Segurança
Todos os endpoints protegidos recebem headers do Gateway:
```
X-User-Id: <userId>
X-User-Login: <login>
X-User-Role: <role>  # Validado nos endpoints admin
```

## Modelos de Dados

### Funcionário
```json
{
  "nome": "João Silva",
  "cpf": "98765432100",
  "login": "joao",
  "senha": "1234",
  "role": "FUNCIONARIO"
}
```

### Administrador (herda de Funcionário)
```json
{
  "nome": "Maria Admin",
  "cpf": "12345678900",
  "login": "maria.admin",
  "senha": "admin456",
  "role": "ADMIN"
}
```

### Roles Disponíveis
- `FUNCIONARIO` - Usuário padrão (pode registrar ponto)
- `ADMIN` - Administrador (herança + permissões administrativas)

### Registro de Ponto
```json
{
  "funcionario": "Funcionario",
  "tipo": "ENTRADA|SAIDA",
  "dataHora": "LocalDateTime"
}
```

## Como Executar

1. Clone o repositório
2. Execute: `mvn spring-boot:run`
3. Acesse: `http://localhost:8080`

## 📊 Logs Estruturados

O sistema utiliza logs estruturados com prefixos identificadores para acompanhar cada passo dos fluxos:

### Prefixos de Log
- `[LISTAR_FUNCIONARIOS]` - Listagem de funcionários
- `[REGISTRAR_PONTO]` - Registro de ponto eletrônico
- `[LISTAR_PONTOS]` - Consulta de registros de ponto
- `[ADMIN_CRIAR_FUNCIONARIO]` - Criação de funcionários pelo admin
- `[ADMIN_REMOVER_FUNCIONARIO]` - Remoção de funcionários pelo admin
- `[ADMIN_LISTAR_FUNCIONARIOS]` - Listagem de funcionários pelo admin
- `[ADMIN_LISTAR_PONTOS]` - Listagem de todos os pontos pelo admin
- `[ADMIN_REMOVER_PONTO]` - Remoção de pontos pelo admin

### Níveis de Log
- **INFO**: Início e conclusão de operações principais
- **DEBUG**: Passos detalhados da execução
- **WARN**: Situações de atenção (registros não encontrados)
- **ERROR**: Erros de sistema com stack trace

### Exemplo de Logs
```
[REGISTRAR_PONTO] Iniciando registro de ponto - Funcionário ID: 1, Tipo: ENTRADA
[REGISTRAR_PONTO] Funcionário encontrado: João Silva - Criando registro
[REGISTRAR_PONTO] Salvando registro no banco - Data/Hora: 2024-01-15T08:30:00
[REGISTRAR_PONTO] Ponto registrado com sucesso - ID: 5, Funcionário: João Silva, Tipo: ENTRADA
```

## 🔒 Segurança e Controle de Acesso

### Validação de Roles
- **Endpoints Admin**: Validam header `X-User-Role = "ADMIN"`
- **Resposta 403**: Para usuários sem permissão
- **Logs de Segurança**: Tentativas de acesso registradas

### Fluxo de Autenticação
1. Gateway valida JWT
2. Gateway injeta headers X-User-*
3. Controller valida role para endpoints admin
4. Acesso liberado ou negado

## Logs e Debug

- Logs DEBUG habilitados para `com.exemplo.ponto`
- Logs de segurança: `[ADMIN_ACCESS_DENIED]` / `[ADMIN_ACCESS_GRANTED]`
- Console com timestamp e thread info
- Logs estruturados para rastreamento de fluxos

## Collection Postman

Importe o arquivo `sistema-ponto-postman.json` no Postman para testar os endpoints.