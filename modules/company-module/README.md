# Company Module - MyERP

## 🏢 **Módulo de Gestão de Empresas Multi-tenant**

### ✅ **Funcionalidades Implementadas**
- **Cadastro de Empresas** com dados completos
- **Multi-tenant** com isolamento por empresa
- **Feature Flags** por empresa
- **Planos diferenciados** (Basic, Standard, Premium, Enterprise)
- **Grupos de manutenção** (A, B, C) para escalonamento
- **Departamentos** e **Cargos** hierárquicos
- **Configurações personalizadas** por empresa

### 🎯 **Arquitetura Multi-tenant Segura**

#### **Isolamento de Dados**
- Todas as entidades têm `empresa_id`
- Filtros automáticos por empresa
- Feature flags específicas por cliente
- Configurações personalizadas

#### **Planos e Manutenção**
```
BASIC (Grupo C):    10 usuários,  sem feature flags
STANDARD (Grupo B): 50 usuários,  com feature flags  
PREMIUM (Grupo A):  200 usuários, com feature flags
ENTERPRISE (Grupo A): 1000+ usuários, instância dedicada
```

#### **Escalonamento de Manutenção**
```
Grupo A (Premium/Enterprise): Terça 2h-4h
Grupo B (Standard):          Quarta 2h-4h  
Grupo C (Basic):             Quinta 2h-4h

Resultado: Máximo 33% dos clientes offline
```

### 🚀 **Como Executar**

#### **1. Iniciar PostgreSQL**
```bash
# Criar banco específico
createdb myerp_company
```

#### **2. Executar Módulo**
```bash
cd modules/company-module
mvn spring-boot:run
```

#### **3. Acessar**
- **API**: http://localhost:8085
- **Health**: http://localhost:8085/actuator/health
- **Eureka**: Registra automaticamente como `COMPANY-SERVICE`

### 📊 **Estrutura de Dados**

#### **Empresas**
- Dados fiscais (CNPJ, IE, IM)
- Endereço completo
- Configurações (timezone, moeda)
- Plano e grupo de manutenção
- Feature flags personalizadas

#### **Departamentos**
- Vinculados à empresa
- Centro de custo
- Responsável

#### **Cargos**
- Hierarquia definida (1-26)
- Salário base
- Carga horária
- Vinculação a departamento

### 🎯 **Hierarquia de Cargos**

```
1-2:   PRESIDÊNCIA (Presidente, Vice-Presidente)
3-7:   DIRETORIA (Executivo, Financeiro, RH, TI, Comercial)
8-12:  GERÊNCIA (Vendas, Produção, Qualidade, Financeiro, RH)
13-15: COORDENAÇÃO (Projetos, Equipe, Vendas)
16-18: SUPERVISÃO (Turno, Área, Qualidade)
19-26: OPERACIONAL (Analistas, Assistentes, Técnicos, Operadores)
```

### 🔧 **Configurações**

#### **Banco de Dados**
- **Database**: `myerp_company`
- **Porta**: 8085
- **Migrations**: Flyway automático

#### **Feature Flags Disponíveis**
- `BIOMETRIA_FACIAL`: Reconhecimento facial
- `DASHBOARD_AVANCADO`: Dashboard premium
- `RELATORIOS_CUSTOMIZADOS`: Relatórios personalizados
- `API_INTEGRACAO`: APIs de terceiros

#### **Configurações por Empresa**
- `HORARIO_FUNCIONAMENTO`: Horário de trabalho
- `TOLERANCIA_PONTO`: Tolerância em minutos
- `EMAIL_NOTIFICACOES`: Email para notificações
- `LOGO_EMPRESA`: Logo em Base64

### 🛡️ **Segurança**

#### **Isolamento Multi-tenant**
- Filtros automáticos por `empresa_id`
- Validação de acesso por empresa
- Feature flags por cliente
- Configurações isoladas

#### **Rollback Seguro**
- Migrations versionadas
- Dados de exemplo seguros
- Constraints de integridade
- Índices otimizados

### 📈 **Próximos Passos**

1. **Integração com Auth Service** - JWT com empresa_id
2. **Integração com RH Module** - Usuários vinculados a empresa
3. **Dashboard Multi-tenant** - Visão por empresa
4. **APIs de Integração** - Webhooks e callbacks
5. **Billing Module** - Cobrança por plano

### 🧪 **Dados de Teste**

O sistema cria automaticamente 3 empresas de exemplo:

#### **Padaria do João** (BASIC - Grupo C)
- CNPJ: 12.345.678/0001-90
- Departamentos: Produção, Vendas, Administração
- Cargos: Proprietário, Padeiro, Atendente

#### **Oficina do Pedro** (STANDARD - Grupo B)  
- CNPJ: 98.765.432/0001-10
- Departamentos: Mecânica, Atendimento
- Cargos: Proprietário, Mecânico Senior, Auxiliar

#### **Consultoria da Ana** (PREMIUM - Grupo A)
- CNPJ: 11.222.333/0001-44
- Departamentos: Consultoria, Administrativo
- Cargos: Sócia Diretora, Consultor Senior, Analista

### 🔍 **Monitoramento**

- **Health Check**: Verifica conexão com BD
- **Metrics**: JVM e aplicação
- **Logs**: Estruturados com empresa_id
- **Eureka**: Service discovery automático

---

**🎯 Company Module: Base sólida para ERP Multi-tenant escalável!**