# MyERP - Sistema ERP Modular

## Arquitetura

### Estrutura do Projeto
```
myErp/
├── infrastructure/          # Serviços de infraestrutura
│   ├── api-gateway/        # Gateway de APIs
│   ├── auth-service/       # Serviço de autenticação
│   ├── config-server/      # Servidor de configuração
│   └── service-discovery/  # Descoberta de serviços (Eureka)
├── modules/                # Módulos de negócio
│   ├── rh-module/         # Recursos Humanos (Ponto, Funcionários)
│   ├── financeiro-module/ # Financeiro (Contas, Fluxo de Caixa)
│   ├── vendas-module/     # Vendas (Pedidos, Clientes)
│   ├── estoque-module/    # Estoque (Produtos, Movimentações)
│   └── compras-module/    # Compras (Fornecedores, Pedidos)
└── shared/                # Bibliotecas compartilhadas
    ├── common-lib/        # DTOs e utilitários comuns
    └── security-lib/      # Configurações de segurança
```

## Como Executar

### 🚀 Inicialização Automática (Recomendado)
```bash
# Iniciar todo o sistema
start-myerp.bat

# Parar todo o sistema
stop-myerp.bat
```

### 📋 Inicialização Manual
1. **PostgreSQL:** `start-postgres.bat`
2. **Eureka Server:** `cd infrastructure/service-discovery && mvn spring-boot:run`
3. **Auth Service:** `cd infrastructure/auth-service && mvn spring-boot:run`
4. **API Gateway:** `cd infrastructure/api-gateway && mvn spring-boot:run`
5. **Módulos:** `cd modules/rh-module/sistema-ponto && mvn spring-boot:run`

### 📚 Documentação Detalhada
- **Guia Completo:** [STARTUP-GUIDE.md](STARTUP-GUIDE.md)
- **PostgreSQL:** [POSTGRES-SETUP.md](POSTGRES-SETUP.md)

## 🔌 Portas dos Serviços

| Serviço | Porta | Database | Status |
|---------|-------|----------|--------|
| PostgreSQL | 5432 | - | ✅ Configurado |
| pgAdmin | 5050 | - | ✅ Configurado |
| Eureka Server | 8761 | - | ✅ Configurado |
| Config Server | 8888 | - | ✅ Configurado |
| API Gateway | 8080 | - | ✅ Configurado |
| Auth Service | 8081 | auth_db | ✅ PostgreSQL |
| RH Module | 8082 | rh_db | ✅ PostgreSQL |
| Biometria Module | 8083 | biometria_db | ✅ PostgreSQL |

## Adicionando Novos Módulos

1. Crie diretório em `modules/novo-modulo/`
2. Adicione no `pom.xml` pai na seção `<modules>`
3. Configure dependências do `common-lib`
4. Registre no Eureka
5. Configure rotas no API Gateway

## Padrões de Desenvolvimento

### Estrutura de Módulo
```
modulo/
├── src/main/java/com/myerp/modulo/
│   ├── controller/     # Controllers REST
│   ├── service/        # Lógica de negócio
│   ├── repository/     # Acesso a dados
│   ├── entity/         # Entidades JPA
│   └── dto/           # DTOs específicos
├── src/main/resources/
│   └── application.yml
└── pom.xml
```

### Comunicação Entre Módulos
- Use Feign Client para chamadas síncronas
- Use eventos assíncronos para desacoplamento
- Sempre passe pelo API Gateway

### Segurança
- Autenticação centralizada no Auth Service
- JWT tokens para autorização
- Validação de tokens no API Gateway

## 🔒 Licença

**Todos os direitos reservados.** Este projeto é proprietário e não possui licença pública.

### ⚠️ Uso Restrito
- ❌ **Uso comercial** não autorizado
- ❌ **Modificação** não autorizada
- ❌ **Distribuição** não autorizada
- ✅ **Visualização** apenas para fins educacionais
- 📧 **Contato necessário** para qualquer uso