# Docker Guide - MyERP

## 🐳 **Containerização Completa**

✅ **STATUS: 100% COMPLETO** - Todos os 9 módulos possuem Dockerfile

Todos os módulos do MyERP possuem **Dockerfile** para deploy independente e escalável.

## 📦 **Estrutura de Containers**

### **Infrastructure Services**
| Serviço | Porta | Dockerfile | Descrição |
|---------|-------|------------|-----------|
| **Eureka Server** | 8761 | `service-discovery/Dockerfile` | Service Discovery |
| **API Gateway** | 8080 | `api-gateway/Dockerfile` | Gateway Principal |
| **Auth Service** | 8081 | `auth-service/Dockerfile` | Autenticação JWT |
| **Config Server** | 8888 | `config-server/Dockerfile` | Configurações |

### **Business Modules**
| Módulo | Porta | Dockerfile | Descrição |
|--------|-------|------------|-----------|
| **RH Module** | 8082 | `rh-module/Dockerfile` | Recursos Humanos |
| **Biometria Module** | 8083 | `biometria-module/Dockerfile` | Controle Biométrico |
| **Monitoring Module** | 8084 | `monitoring-module/Dockerfile` | Dashboard |
| **Company Module** | 8085 | `company-module/Dockerfile` | Gestão Empresas |
| **Financial Module** | 8086 | `financial-module/Dockerfile` | Financeiro |

## 🔧 **Padrão dos Dockerfiles**

Todos seguem o mesmo padrão de segurança e otimização:

```dockerfile
FROM openjdk:17-jdk-slim
LABEL maintainer="MyERP Team"
LABEL service="service-name"
LABEL version="1.0.0"

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Criar usuário não-root
RUN groupadd -r myerp && useradd -r -g myerp myerp

# Configurar aplicação
WORKDIR /app
COPY target/service-name-*.jar app.jar
RUN chown -R myerp:myerp /app
USER myerp

# Expor porta e health check
EXPOSE 8XXX
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8XXX/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

## 🚀 **Build e Deploy**

### **Build Individual**
```bash
# Compilar JAR
cd infrastructure/auth-service
mvn clean package -DskipTests

# Build imagem
docker build -t myerp/auth-service:1.0.0 .

# Executar
docker run -p 8081:8081 myerp/auth-service:1.0.0
```

### **Build Todos os Módulos**
```bash
# Build completo
docker-compose build

# Deploy desenvolvimento
docker-compose up -d

# Deploy produção
docker-compose -f docker-compose.prod.yml up -d
```

### **Build com Maven**
```bash
# Build JAR + Docker em um comando
mvn clean package docker:build -DskipTests
```

## 🔍 **Health Checks**

Todos os containers têm health checks automáticos:

```bash
# Verificar status
docker ps --format "table {{.Names}}\t{{.Status}}"

# Logs de health check
docker inspect --format='{{json .State.Health}}' myerp-auth-service
```

## 📊 **Monitoramento**

### **Container Stats**
```bash
# Uso de recursos
docker stats

# Logs em tempo real
docker logs -f myerp-auth-service
```

### **Health Endpoints**
- Auth Service: http://localhost:8081/actuator/health
- RH Module: http://localhost:8082/actuator/health
- Monitoring: http://localhost:8084/actuator/health

## ☁️ **Deploy AWS ECS**

### **Task Definitions**
Cada Dockerfile gera uma **Task Definition** no ECS:

```json
{
  "family": "myerp-auth-service",
  "image": "myerp/auth-service:1.0.0",
  "portMappings": [{"containerPort": 8081}],
  "healthCheck": {
    "command": ["CMD-SHELL", "curl -f http://localhost:8081/actuator/health || exit 1"]
  }
}
```

### **Service Discovery**
- **Eureka** para desenvolvimento
- **AWS Service Discovery** para produção
- **Application Load Balancer** para roteamento

## 🔐 **Segurança**

### **Usuário Não-Root**
Todos os containers executam com usuário `myerp:myerp`

### **Imagem Base**
- `openjdk:17-jdk-slim` - Otimizada e segura
- Apenas dependências essenciais
- Atualizações automáticas de segurança

### **Secrets Management**
```bash
# Variáveis de ambiente
docker run -e JWT_SECRET=secret123 myerp/auth-service:1.0.0

# AWS Secrets Manager
docker run -e AWS_REGION=us-east-1 myerp/auth-service:1.0.0
```

## 🔄 **CI/CD Pipeline**

### **GitHub Actions**
```yaml
- name: Build Docker Image
  run: |
    mvn clean package -DskipTests
    docker build -t myerp/${{ matrix.service }}:${{ github.sha }} .
    
- name: Push to Registry
  run: |
    docker push myerp/${{ matrix.service }}:${{ github.sha }}
```

### **Deployment Strategy**
1. **Build** - Maven + Docker
2. **Test** - Container health checks
3. **Push** - Docker Registry (ECR)
4. **Deploy** - ECS Rolling Update
5. **Verify** - Health checks + Monitoring

## 📋 **Troubleshooting**

### **Container não inicia**
```bash
# Verificar logs
docker logs myerp-auth-service

# Executar interativo
docker run -it myerp/auth-service:1.0.0 /bin/bash
```

### **Health Check falha**
```bash
# Testar manualmente
docker exec myerp-auth-service curl http://localhost:8081/actuator/health

# Verificar configuração
docker inspect myerp-auth-service
```

### **Performance Issues**
```bash
# Limitar recursos
docker run --memory=512m --cpus=0.5 myerp/auth-service:1.0.0

# Monitorar uso
docker stats myerp-auth-service
```

## 🎯 **Próximos Passos**

1. **Multi-stage builds** para otimização
2. **Distroless images** para segurança máxima
3. **Kubernetes manifests** para orquestração
4. **Helm charts** para deploy simplificado
5. **Service mesh** (Istio) para comunicação

---

**MyERP - Containerização completa para deploy escalável! 🐳🚀**