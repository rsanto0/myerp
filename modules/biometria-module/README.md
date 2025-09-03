# Módulo Biometria - MyERP

## Funcionalidades

### ✅ Implementado
- **Simulador de câmera** para testes
- **Detecção automática** com tolerância de ±15min
- **Registro automático** de ponto dentro da tolerância
- **Sugestões para RH** quando fora da tolerância
- **Notificações por email** para inconsistências
- **Dashboard** com estatísticas
- **API REST** para gerenciamento

### 🔄 Em Desenvolvimento
- Integração com câmera real
- Reconhecimento facial com OpenCV
- Interface web para RH
- Notificações WhatsApp

## Como Testar

### 1. Executar o Módulo
```bash
cd modules/biometria-module
mvn spring-boot:run
```

### 2. Acessar Interfaces
- **API**: http://localhost:8083/api/biometria
- **H2 Console**: http://localhost:8083/h2-console
- **Dashboard**: http://localhost:8083/api/biometria/dashboard

### 3. Simular Detecções
```bash
# Simular entrada
curl -X POST "http://localhost:8083/api/biometria/simular-deteccao?nomeFuncionario=João Silva&movimento=ENTRADA"

# Simular saída
curl -X POST "http://localhost:8083/api/biometria/simular-deteccao?nomeFuncionario=João Silva&movimento=SAIDA"
```

### 4. Ver Eventos
```bash
# Listar todos os eventos
curl http://localhost:8083/api/biometria/eventos

# Ver eventos pendentes de validação
curl http://localhost:8083/api/biometria/eventos/pendentes
```

## Configurações

### Tolerâncias de Horário
- **Entrada**: 07:45 - 08:15 (±15min do 08:00)
- **Saída Almoço**: 11:45 - 12:15
- **Retorno Almoço**: 12:45 - 13:15  
- **Saída**: 16:45 - 17:15

### Comportamentos
- **Dentro da tolerância**: Registra ponto automaticamente
- **Fora da tolerância**: Cria sugestão para RH validar
- **Inconsistências**: Notifica funcionário e RH por email

## Logs do Sistema

O sistema exibe logs coloridos no console:
- 🎥 **Detecções simuladas**
- ✅ **Pontos registrados automaticamente**  
- ⏰ **Sugestões criadas para RH**
- 📧 **Emails enviados**
- ⚠️ **Inconsistências detectadas**

## Próximos Passos

1. **Integrar câmera real** (substituir simulador)
2. **Implementar reconhecimento facial** com OpenCV
3. **Criar interface web** para RH
4. **Configurar email real** (atualmente simulado)
5. **Adicionar notificações WhatsApp**