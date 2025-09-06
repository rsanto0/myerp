# Scripts de Desenvolvimento - MyERP

## 🚀 **Scripts Principais**

### **Sistema Completo**
```bash
start-myerp.bat          # Iniciar sistema completo
stop-myerp.bat           # Parar sistema completo
```

### **Módulos Específicos**
```bash
start-monitoring.bat     # Só módulo de monitoramento
stop-monitoring.bat      # Parar monitoramento
start-postgres.bat       # Só PostgreSQL
```

### **Testes e Validação**
```bash
test-profiles.bat        # Testar configurações de profiles
```

## 📋 **Ordem de Execução**

### **Desenvolvimento Normal**
1. `start-postgres.bat` (se não estiver rodando)
2. `start-myerp.bat`
3. Desenvolvimento...
4. `stop-myerp.bat`

### **Só Monitoramento**
1. Certifique-se que outros serviços estão rodando
2. `start-monitoring.bat`
3. Acesse: http://localhost:8084

## ⚙️ **Configurações**

Todos os scripts usam **profile local** por padrão:
- Database: localhost:5432
- Services: localhost:808X
- Logs: DEBUG level

## 🔧 **Personalização**

Para usar profiles diferentes, edite os scripts:
```bash
# Exemplo: usar profile docker
mvn spring-boot:run -Dspring.profiles.active=docker
```

## 📊 **Monitoramento**

Após executar os scripts, verifique:
- **Eureka**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Monitoring**: http://localhost:8084