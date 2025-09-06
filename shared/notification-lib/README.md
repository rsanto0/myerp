# Notification Library - MyERP

## 📧 Funcionalidades

### ✅ Implementado
- **Email SMTP** com templates HTML
- **SMS via Twilio** com fallback simulado
- **WhatsApp via Twilio** com fallback simulado
- **Templates dinâmicos** com placeholders
- **Configuração centralizada** via Config Server
- **Múltiplos providers** (Email, SMS, WhatsApp)

## 🚀 Como Usar

### 1. Adicionar Dependência
```xml
<dependency>
    <groupId>com.myerp</groupId>
    <artifactId>notification-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Uso Simples
```java
@Autowired
private NotificationService notificationService;

// Email simples
notificationService.send(EMAIL, "user@email.com", "Assunto", "Mensagem");

// SMS
notificationService.send(SMS, "+5511999999999", null, "Mensagem SMS");

// WhatsApp
notificationService.send(WHATSAPP, "+5511999999999", null, "Mensagem WhatsApp");
```

### 3. Uso Avançado com Templates
```java
// Email de boas-vindas
notificationService.sendWelcomeEmail(
    "user@email.com", 
    "João Silva", 
    "joao", 
    "http://myerp.com"
);

// Notificação de ponto
notificationService.sendPontoNotification(
    "user@email.com", 
    "João Silva", 
    "ENTRADA", 
    "08:00"
);
```

### 4. Request Customizado
```java
NotificationRequest request = new NotificationRequest();
request.setType(EMAIL);
request.setTo("user@email.com");
request.setSubject("Assunto Personalizado");
request.setTemplate("custom-template");
request.setVariables(Map.of(
    "nome", "João",
    "data", LocalDate.now().toString()
));

notificationService.send(request);
```

## ⚙️ Configuração

### Config Server (notification-service.yml)
```yaml
notification:
  email:
    enabled: true
    smtp:
      host: smtp.gmail.com
      port: 587
      username: ${SMTP_USERNAME}
      password: ${SMTP_PASSWORD}
  
  sms:
    enabled: true
    twilio:
      account-sid: ${TWILIO_ACCOUNT_SID}
      auth-token: ${TWILIO_AUTH_TOKEN}
      from-number: ${TWILIO_FROM_NUMBER}
  
  whatsapp:
    enabled: false
    twilio:
      account-sid: ${TWILIO_ACCOUNT_SID}
      auth-token: ${TWILIO_AUTH_TOKEN}
      from-number: ${TWILIO_WHATSAPP_FROM}
```

### Variáveis de Ambiente
```bash
# Email
SMTP_USERNAME=myerp@gmail.com
SMTP_PASSWORD=app-password

# Twilio
TWILIO_ACCOUNT_SID=your-account-sid
TWILIO_AUTH_TOKEN=your-auth-token
TWILIO_FROM_NUMBER=+1234567890
TWILIO_WHATSAPP_FROM=whatsapp:+14155238886
```

## 📋 Templates Disponíveis

### Email Templates
- **welcome** - Email de boas-vindas
- **ponto-registro** - Confirmação de ponto
- **password-reset** - Reset de senha (futuro)

### Placeholders
- `{{nome}}` - Nome do usuário
- `{{login}}` - Login do usuário
- `{{url}}` - URL do sistema
- `{{tipo}}` - Tipo de ponto (ENTRADA/SAIDA)
- `{{dataHora}}` - Data e hora formatada

## 🔧 Providers

### Email (SMTP)
- **Provider**: JavaMailSender (Spring)
- **Suporte**: HTML, texto, anexos
- **Configuração**: Via application.yml

### SMS (Twilio)
- **Provider**: Twilio REST API
- **Fallback**: Simulação quando não configurado
- **Formato**: Números internacionais (+55...)

### WhatsApp (Twilio)
- **Provider**: Twilio WhatsApp API
- **Fallback**: Simulação quando não configurado
- **Formato**: whatsapp:+55... ou +55...

## 🎯 Estrutura

```
notification-lib/
├── service/
│   ├── NotificationService.java    # Facade principal
│   ├── EmailService.java          # SMTP
│   ├── SmsService.java            # Twilio SMS
│   ├── WhatsAppService.java       # Twilio WhatsApp
│   └── TemplateService.java       # Templates
├── dto/
│   ├── NotificationRequest.java   # Request principal
│   └── EmailRequest.java          # Email avançado
├── enums/
│   └── NotificationType.java      # EMAIL, SMS, WHATSAPP
└── config/
    └── NotificationConfig.java    # Spring config
```

## 🧪 Testes

### Simulação Local
Quando providers não estão configurados, a biblioteca simula os envios:
```
[EMAIL] SIMULADO - Para: user@email.com | Assunto: Teste
[SMS] SIMULADO - Para: +5511999999999 | Mensagem: Teste SMS
[WHATSAPP] SIMULADO - Para: +5511999999999 | Mensagem: Teste WhatsApp
```

### Logs de Sucesso
```
[EMAIL] Enviado para: user@email.com | Assunto: Bem-vindo
[SMS] Enviado para: +5511999999999 | SID: SM1234567890
[WHATSAPP] Enviado para: +5511999999999 | SID: SM0987654321
```

## 🚀 Próximos Passos

1. **Templates externos** (arquivos .html)
2. **Mais providers** (SendGrid, Amazon SES)
3. **Filas assíncronas** (RabbitMQ, Kafka)
4. **Retry automático** para falhas
5. **Métricas de entrega** e analytics

## 📞 Suporte

- **Configuração**: Ver `config-server/notification-service.yml`
- **Exemplos**: Ver módulos que usam (biometria-module)
- **Logs**: Nível DEBUG habilitado por padrão

**Comunicação unificada para todo o MyERP! 📧📱💬**