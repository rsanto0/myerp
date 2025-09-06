# 📚 DOCUMENTAÇÃO COMPLETA DAS CLASSES - MyERP

## 🏗️ **VISÃO GERAL DA ARQUITETURA**

O sistema MyERP é composto por **8 módulos** interconectados que trabalham em conjunto para formar um ERP completo:

### **🔧 Infraestrutura (3 módulos)**
- **Service Discovery** (8761) - Registro de serviços
- **API Gateway** (8080) - Roteamento e autenticação
- **Auth Service** (8081) - Autenticação JWT

### **📦 Módulos de Negócio (5 módulos)**
- **RH Module** (8082) - Recursos Humanos e Ponto
- **Biometria Module** (8083) - Controle Biométrico
- **Monitoring Module** (8084) - Monitoramento do Sistema
- **Company Module** (8085) - Gestão Multi-tenant
- **Financial Module** (8086) - Gestão Financeira

## 🔗 **CORRELAÇÕES ENTRE MÓDULOS**

### **Fluxo Principal de Integração:**
```
API Gateway (8080) → Auth Service (8081) → Módulos de Negócio
                                        ↓
Company Module (8085) ← → Financial Module (8086)
                     ↓
RH Module (8082) ← → Biometria Module (8083)
                     ↓
Monitoring Module (8084) - monitora todos
```

### **Dependências de Integração:**
- **Financial Module** depende do **Company Module** para dados bancários
- **Biometria Module** integra com **RH Module** para cadastro de funcionários
- **RH Module** usa **Auth Service** para validação de usuários
- **Monitoring Module** monitora todos os outros módulos
- **API Gateway** roteia para todos os módulos de negócio

---

## 📋 **ÍNDICE DE CLASSES POR MÓDULO**

### 🔧 **INFRAESTRUTURA**

#### **Service Discovery (8761)**
- [EurekaServerApplication](#eurekaserverapplication) - Classe principal do Eureka Server

#### **API Gateway (8080)**
- [GatewayApplication](#gatewayapplication) - Classe principal do Gateway
- [JwtAuthFilter](#jwtauthfilter) - Filtro de autenticação JWT

#### **Auth Service (8081)**
- [AuthApplication](#authapplication) - Classe principal do Auth Service
- [AuthController](#authcontroller) - Endpoints de autenticação
- [JwtService](#jwtservice) - Serviço de geração/validação JWT
- [User](#user) - Entidade de usuário
- [UserRepository](#userrepository) - Repositório de usuários

### 📦 **MÓDULOS DE NEGÓCIO**

#### **RH Module (8082)**
- [PontoApplication](#pontoapplication) - Classe principal do RH
- [PontoController](#pontocontroller) - Endpoints de ponto eletrônico
- [AdminController](#admincontroller) - Endpoints administrativos
- [Funcionario](#funcionario) - Entidade de funcionário
- [RegistroPonto](#registroponto) - Entidade de registro de ponto
- [BiometriaClient](#biometriaclient) - Cliente Feign para Biometria Module

#### **Biometria Module (8083)**
- [BiometriaApplication](#biometriaapplication) - Classe principal da Biometria
- [BiometriaController](#biometriacontroller) - Endpoints de biometria
- [CameraController](#cameracontroller) - Endpoints de câmera
- [BiometriaUsuario](#biometriausuario) - Entidade de biometria
- [ConfiguracaoCamera](#configuracaocamera) - Entidade de configuração de câmera
- [TipoBiometria](#tipobiometria) - Enum de tipos biométricos

#### **Monitoring Module (8084)**
- [MonitoringApplication](#monitoringapplication) - Classe principal do Monitoring
- [MonitoringController](#monitoringcontroller) - Endpoints de monitoramento
- [HealthService](#healthservice) - Serviço de health check

#### **Company Module (8085)**
- [CompanyApplication](#companyapplication) - Classe principal do Company
- [EmpresaController](#empresacontroller) - Endpoints de empresas
- [ContaBancariaController](#contabancariacontroller) - Endpoints bancários
- [Empresa](#empresa) - Entidade de empresa
- [ContaBancaria](#contabancaria) - Entidade de conta bancária
- [TipoPlano](#tipoplano) - Enum de tipos de plano
- [BancoBrasil](#bancobrasil) - Enum de bancos brasileiros

#### **Financial Module (8086)**
- [FinancialApplication](#financialapplication) - Classe principal do Financial
- [BoletoController](#boletocontroller) - Endpoints de boletos
- [PixController](#pixcontroller) - Endpoints PIX
- [DashboardController](#dashboardcontroller) - Dashboard financeiro
- [Boleto](#boleto) - Entidade de boleto
- [TransacaoPix](#transacaopix) - Entidade de transação PIX
- [ConciliacaoBancaria](#conciliacaobancaria) - Entidade de conciliação
- [CompanyClient](#companyclient) - Cliente Feign para Company Module

### 🔗 **SHARED**

#### **Common Lib**
- [Usuario](#usuario) - Entidade base unificada
- [Role](#role) - Enum de roles do sistema

---

## 🔗 **PROCESSOS DE NEGÓCIO E INTEGRAÇÕES**

### **Processo 1: Cadastro de Funcionário com Biometria**
```
1. Admin cria funcionário no RH Module
2. RH Module chama Biometria Module via Feign Client
3. Biometria Module registra dados biométricos
4. Sistema fica pronto para controle de ponto biométrico
```
**Classes envolvidas:** `AdminController`, `BiometriaClient`, `BiometriaController`, `BiometriaUsuario`

### **Processo 2: Gestão Financeira Multi-tenant**
```
1. Company Module gerencia dados bancários da empresa
2. Financial Module consulta contas via Feign Client
3. Financial Module processa boletos/PIX
4. Conciliação bancária é realizada
```
**Classes envolvidas:** `ContaBancariaController`, `CompanyClient`, `BoletoController`, `PixController`

### **Processo 3: Autenticação e Autorização**
```
1. Cliente faz login via API Gateway
2. Gateway valida com Auth Service
3. JWT é gerado e injetado nos headers
4. Módulos validam permissões via headers
```
**Classes envolvidas:** `JwtAuthFilter`, `AuthController`, `JwtService`, `AdminController`

### **Processo 4: Monitoramento do Sistema**
```
1. Monitoring Module consulta health de todos os serviços
2. Métricas são coletadas via Actuator
3. Dashboard consolidado é gerado
4. Alertas são disparados se necessário
```
**Classes envolvidas:** `MonitoringController`, `HealthService`, `DashboardController`

---

## 📊 **MATRIZ DE DEPENDÊNCIAS**

| Módulo | Depende de | Usado por | Tipo de Integração |
|--------|------------|-----------|-------------------|
| **Service Discovery** | - | Todos | Service Registry |
| **API Gateway** | Auth Service | Clientes | HTTP Routing |
| **Auth Service** | - | Gateway, RH | JWT/HTTP |
| **RH Module** | Auth, Biometria | Gateway | Feign Client |
| **Biometria Module** | - | RH | Feign Client |
| **Monitoring Module** | Todos | - | HTTP Health Check |
| **Company Module** | - | Financial | Feign Client |
| **Financial Module** | Company | - | Feign Client |

---

*Esta documentação será expandida com detalhes de cada classe nas seções seguintes...*