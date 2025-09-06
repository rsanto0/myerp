# Módulo Monitoramento - MyERP

## 📊 Funcionalidades

### ✅ Implementado
- **Health Check** de todos os serviços
- **Métricas JVM** (memória, CPU, uptime)
- **Status dos serviços** em tempo real
- **Dashboard consolidado** com visão geral
- **Actuator endpoints** para observabilidade
- **Prometheus metrics** para coleta de dados

### 🔄 Planejado
- Alertas por email/Slack
- Histórico de métricas
- Interface web para dashboard
- Logs centralizados

## 🚀 Como usar

### 1. Executar o Módulo
```bash
cd modules/monitoring-module
mvn spring-boot:run
```

### 2. Endpoints Disponíveis
- **Dashboard**: http://localhost:8084/api/monitoring/dashboard
- **Health**: http://localhost:8084/api/monitoring/health
- **Services**: http://localhost:8084/api/monitoring/services
- **Metrics**: http://localhost:8084/api/monitoring/metrics
- **Actuator**: http://localhost:8084/actuator

### 3. Exemplos de Uso

#### Dashboard Completo
```bash
curl http://localhost:8084/api/monitoring/dashboard
```

#### Status dos Serviços
```bash
curl http://localhost:8084/api/monitoring/services
```

#### Métricas do Sistema
```bash
curl http://localhost:8084/api/monitoring/metrics
```

## 📈 Métricas Monitoradas

### Sistema
- **Uptime** dos serviços
- **Uso de memória** (heap/non-heap)
- **CPU** disponível
- **Status** de cada microserviço

### Serviços Monitorados
- **Eureka Server** (8761)
- **API Gateway** (8080)
- **Auth Service** (8081)
- **RH Module** (8082)
- **Biometria Module** (8083)

## 🎯 Dashboard Data

```json
{
  "success": true,
  "data": {
    "health": {
      "status": "UP",
      "uptime": "120 seconds",
      "memoryUsed": "45 MB"
    },
    "services": {
      "eureka": {"status": "UP"},
      "api-gateway": {"status": "UP"},
      "auth-service": {"status": "UP"}
    },
    "summary": {
      "totalServices": 5,
      "upServices": 4,
      "healthPercentage": 80.0
    }
  }
}
```

## 🔧 Configuração

### Porta
- **Monitoring**: 8084

### Actuator
- Todos os endpoints expostos
- Health details sempre visíveis
- Prometheus metrics habilitado

## 📊 Próximos Passos

1. **Interface Web** para dashboard visual
2. **Alertas automáticos** quando serviços ficam down
3. **Histórico de métricas** com banco de dados
4. **Integração com Grafana** para visualizações avançadas
5. **Logs centralizados** com ELK Stack