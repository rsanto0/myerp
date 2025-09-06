# 📷 EXEMPLOS DE CONFIGURAÇÃO - CÂMERAS USB E IP

## 🎯 CONFIGURAÇÃO VIA API

### **1. CÂMERA USB/WEBCAM**

#### **Exemplo Genérico (Webcam Padrão):**
```bash
curl -X POST http://localhost:8083/api/biometria/camera/config/criar \
-H "Content-Type: application/json" \
-d '{
  "nomeConfiguracao": "Webcam USB Padrão",
  "marcaModelo": "Webcam Genérica",
  "tipoCamera": "USB",
  "resolucao": "1280x720",
  "sistemaOperacional": "Windows",
  "indiceUsb": 0,
  "fps": 30,
  "qualidadeJpeg": 80
}'
```

#### **Exemplo Logitech C920:**
```bash
curl -X POST http://localhost:8083/api/biometria/camera/config/criar \
-H "Content-Type: application/json" \
-d '{
  "nomeConfiguracao": "Logitech C920 HD",
  "marcaModelo": "Logitech C920 HD Pro Webcam",
  "tipoCamera": "USB",
  "resolucao": "1920x1080",
  "sistemaOperacional": "Windows 11",
  "indiceUsb": 0,
  "vidPid": "046d:085b",
  "fps": 30,
  "qualidadeJpeg": 85,
  "timeoutConexao": 3000
}'
```

### **2. CÂMERA IP**

#### **Exemplo Hikvision:**
```bash
curl -X POST http://localhost:8083/api/biometria/camera/config/criar \
-H "Content-Type: application/json" \
-d '{
  "nomeConfiguracao": "Hikvision IP Camera",
  "marcaModelo": "Hikvision DS-2CD2043G0-I",
  "tipoCamera": "IP",
  "resolucao": "2688x1520",
  "sistemaOperacional": "Linux",
  "enderecoIp": "192.168.1.100",
  "porta": 554,
  "protocolo": "RTSP",
  "usuario": "admin",
  "senha": "12345",
  "urlStream": "rtsp://admin:12345@192.168.1.100:554/stream1",
  "fps": 25,
  "qualidadeJpeg": 90,
  "timeoutConexao": 5000
}'
```

#### **Exemplo Dahua:**
```bash
curl -X POST http://localhost:8083/api/biometria/camera/config/criar \
-H "Content-Type: application/json" \
-d '{
  "nomeConfiguracao": "Dahua IP Camera",
  "marcaModelo": "Dahua IPC-HFW4431R-Z",
  "tipoCamera": "IP",
  "resolucao": "2688x1520",
  "enderecoIp": "192.168.1.101",
  "porta": 554,
  "protocolo": "RTSP",
  "usuario": "admin",
  "senha": "admin123",
  "urlStream": "rtsp://admin:admin123@192.168.1.101:554/cam/realmonitor?channel=1&subtype=0"
}'
```

## 🔧 COMANDOS DE TESTE

### **Listar Configurações:**
```bash
curl http://localhost:8083/api/biometria/camera/config/listar
```

### **Testar Configuração Específica:**
```bash
# Testar configuração ID 1
curl -X POST http://localhost:8083/api/biometria/camera/config/testar/1
```

### **Ativar Configuração:**
```bash
# Ativar configuração ID 1
curl -X POST http://localhost:8083/api/biometria/camera/config/ativar/1
```

### **Ver Configuração Ativa:**
```bash
curl http://localhost:8083/api/biometria/camera/config/ativa
```

## 📋 INFORMAÇÕES QUE PRECISO DAS SUAS CÂMERAS

### **CÂMERA USB:**
- **Marca/Modelo**: _________________ 
- **Está funcionando no Windows?**: Sim/Não
- **Aparece no Gerenciador de Dispositivos?**: Sim/Não
- **Funciona em aplicativos como Skype/Teams?**: Sim/Não

### **CÂMERA IP:**
- **Marca/Modelo**: _________________
- **IP atual**: _________________
- **Usuário/Senha**: _________________
- **Porta RTSP**: _________________ (geralmente 554)
- **Consegue acessar via navegador?**: Sim/Não
- **URL de acesso web**: _________________

## 🚀 FLUXO DE CONFIGURAÇÃO

### **PASSO 1: Configurar Câmera USB**
```bash
# 1. Criar configuração USB
curl -X POST http://localhost:8083/api/biometria/camera/config/criar \
-H "Content-Type: application/json" \
-d '{
  "nomeConfiguracao": "Minha Webcam USB",
  "tipoCamera": "USB",
  "indiceUsb": 0,
  "resolucao": "1280x720"
}'

# 2. Testar (substitua ID pelo retornado)
curl -X POST http://localhost:8083/api/biometria/camera/config/testar/1

# 3. Se funcionou, ativar
curl -X POST http://localhost:8083/api/biometria/camera/config/ativar/1
```

### **PASSO 2: Configurar Câmera IP**
```bash
# 1. Criar configuração IP (ajuste os dados)
curl -X POST http://localhost:8083/api/biometria/camera/config/criar \
-H "Content-Type: application/json" \
-d '{
  "nomeConfiguracao": "Minha Camera IP",
  "tipoCamera": "IP",
  "enderecoIp": "SEU_IP_AQUI",
  "porta": 554,
  "protocolo": "RTSP",
  "usuario": "SEU_USUARIO",
  "senha": "SUA_SENHA",
  "urlStream": "rtsp://usuario:senha@IP:554/stream"
}'

# 2. Testar
curl -X POST http://localhost:8083/api/biometria/camera/config/testar/2

# 3. Se funcionou, pode alternar entre USB e IP conforme necessário
```

## 🎯 PRÓXIMOS PASSOS

1. **Me informe os dados** das suas câmeras
2. **Testamos a configuração** USB primeiro
3. **Configuramos a IP** em seguida  
4. **Implementamos a captura real** baseada no que funcionar
5. **Integramos com o cadastro** de usuários

## 💡 DICAS

- **USB é mais simples** - geralmente funciona com índice 0
- **IP precisa de rede** - teste ping primeiro
- **Pode alternar** entre as duas conforme necessário
- **Sistema salva** múltiplas configurações
- **Teste individual** antes de ativar

---

**Me passe os dados das suas câmeras e configuramos juntos! 📷✨**