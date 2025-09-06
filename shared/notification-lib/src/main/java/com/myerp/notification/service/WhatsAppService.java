package com.myerp.notification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {
    
    @Value("${notification.whatsapp.twilio.account-sid:}")
    private String accountSid;
    
    @Value("${notification.whatsapp.twilio.auth-token:}")
    private String authToken;
    
    @Value("${notification.whatsapp.twilio.from-number:}")
    private String fromNumber;
    
    @Value("${notification.whatsapp.enabled:false}")
    private boolean enabled;
    
    private boolean initialized = false;
    
    private void initTwilio() {
        if (!initialized && enabled && !accountSid.isEmpty()) {
            Twilio.init(accountSid, authToken);
            initialized = true;
        }
    }
    
    public void sendWhatsApp(String to, String message) {
        if (!enabled) {
            System.out.println("[WHATSAPP] SIMULADO - Para: " + to + " | Mensagem: " + message);
            return;
        }
        
        try {
            initTwilio();
            
            // WhatsApp numbers need 'whatsapp:' prefix
            String whatsappTo = to.startsWith("whatsapp:") ? to : "whatsapp:" + to;
            
            Message twilioMessage = Message.creator(
                new PhoneNumber(whatsappTo),
                new PhoneNumber(fromNumber),
                message
            ).create();
            
            System.out.println("[WHATSAPP] Enviado para: " + to + " | SID: " + twilioMessage.getSid());
        } catch (Exception e) {
            System.err.println("[WHATSAPP] Erro ao enviar: " + e.getMessage());
        }
    }
}