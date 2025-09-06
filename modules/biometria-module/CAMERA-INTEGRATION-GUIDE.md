# 📷 GUIA DE INTEGRAÇÃO COM CÂMERA REAL

## 🎯 INFORMAÇÕES NECESSÁRIAS

### **HARDWARE QUE VOCÊ DEVE FORNECER:**

#### **1. Especificações da Câmera:**
- [ ] **Marca/Modelo**: _________________
- [ ] **Tipo**: USB / IP / Serial / Outro: _______
- [ ] **Resolução**: _________________
- [ ] **Driver/SDK**: Link ou arquivo
- [ ] **Sistema Operacional**: Windows / Linux / macOS

#### **2. Para Câmera USB/Webcam:**
- [ ] **VID/PID**: (Vendor ID / Product ID)
- [ ] **Porta USB**: Qual porta será usada
- [ ] **Drivers necessários**: Sim / Não

#### **3. Para Câmera IP:**
- [ ] **Endereço IP**: _________________
- [ ] **Porta**: _________________
- [ ] **Protocolo**: RTSP / HTTP / Outro: _______
- [ ] **Usuário/Senha**: _________________
- [ ] **URL do Stream**: _________________

#### **4. Para Sensor de Impressão Digital:**
- [ ] **Marca/Modelo**: _________________
- [ ] **SDK/API**: Link ou arquivo
- [ ] **Formato de saída**: Template / Imagem / Outro
- [ ] **Qualidade mínima**: _________________

## 🔧 DEPENDÊNCIAS A ADICIONAR

### **Para OpenCV (Recomendado):**
```xml
<dependency>
    <groupId>org.openpnp</groupId>
    <artifactId>opencv</artifactId>
    <version>4.9.0-0</version>
</dependency>
```

### **Para JavaCV (Alternativa):**
```xml
<dependency>
    <groupId>org.bytedeco</groupId>
    <artifactId>javacv-platform</artifactId>
    <version>1.5.10</version>
</dependency>
```

### **Para Câmeras IP:**
```xml
<dependency>
    <groupId>org.bytedeco</groupId>
    <artifactId>ffmpeg-platform</artifactId>
    <version>6.1.1-1.5.10</version>
</dependency>
```

## 🚀 IMPLEMENTAÇÃO PERSONALIZADA

### **OPÇÃO 1: Frontend (JavaScript)**
```javascript
// Captura via navegador
async function capturarBiometria() {
    const stream = await navigator.mediaDevices.getUserMedia({ 
        video: { 
            width: 1280, 
            height: 720,
            facingMode: 'user' 
        } 
    });
    
    const video = document.createElement('video');
    video.srcObject = stream;
    video.play();
    
    // Capturar frame após 3 segundos
    setTimeout(() => {
        const canvas = document.createElement('canvas');
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        
        const ctx = canvas.getContext('2d');
        ctx.drawImage(video, 0, 0);
        
        const base64 = canvas.toDataURL('image/jpeg', 0.8);
        
        // Enviar para backend
        enviarBiometria(base64);
        
        // Parar stream
        stream.getTracks().forEach(track => track.stop());
    }, 3000);
}
```

### **OPÇÃO 2: Backend (Java + OpenCV)**
```java
// Implementação com sua câmera específica
public String capturarComOpenCV() {
    VideoCapture camera = new VideoCapture(0); // Índice da câmera
    
    if (!camera.isOpened()) {
        throw new RuntimeException("Câmera não encontrada");
    }
    
    Mat frame = new Mat();
    camera.read(frame);
    
    // Converter para Base64
    MatOfByte matOfByte = new MatOfByte();
    Imgcodecs.imencode(".jpg", frame, matOfByte);
    byte[] byteArray = matOfByte.toArray();
    
    camera.release();
    
    return Base64.getEncoder().encodeToString(byteArray);
}
```

## 📱 ENDPOINTS CRIADOS

### **Testar Câmera:**
```bash
GET http://localhost:8083/api/biometria/camera/testar
```

### **Capturar Foto:**
```bash
POST http://localhost:8083/api/biometria/camera/capturar-foto
```

### **Captura com Detecção Facial:**
```bash
POST http://localhost:8083/api/biometria/camera/capturar-facial?segundos=5
```

## 🔧 CONFIGURAÇÃO ESPECÍFICA

### **Para sua câmera, você precisará:**

1. **Fornecer as informações acima**
2. **Instalar drivers/SDK** (se necessário)
3. **Testar conectividade** com a câmera
4. **Configurar parâmetros** específicos
5. **Implementar captura** personalizada

## 📞 PRÓXIMOS PASSOS

1. **Preencha as informações** desta lista
2. **Forneça drivers/SDK** da câmera
3. **Teste a conectividade** básica
4. **Implementaremos juntos** a integração específica

## 🎯 RESULTADO ESPERADO

Após a implementação, você terá:
- ✅ Captura real via câmera
- ✅ Detecção facial automática
- ✅ Qualidade de imagem otimizada
- ✅ Integração com cadastro de usuários
- ✅ Fallback para simulação (desenvolvimento)

---

**IMPORTANTE:** Forneça as informações marcadas com [ ] para implementação personalizada!