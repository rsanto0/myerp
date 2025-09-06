package com.myerp.notification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    
    @Value("${notification.sms.twilio.account-sid:}")
    private String accountSid;
    
    @Value("${notification.sms.twilio.auth-token:}")
    private String authToken;
    
    @Value("${notification.sms.twilio.from-number:}")
    private String fromNumber;
    
    @Value("${notification.sms.enabled:false}")
    private boolean enabled;
    
    private boolean initialized = false;
    
    private void initTwilio() {
        if (!initialized && enabled && !accountSid.isEmpty()) {
            Twilio.init(accountSid, authToken);
            initialized = true;
        }
    }
    
    public void sendSms(String to, String message) {
        if (!enabled) {
            System.out.println("[SMS] SIMULADO - Para: " + to + " | Mensagem: " + message);
            return;
        }
        
        try {
            initTwilio();
            
            Message twilioMessage = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromNumber),
                message
            ).create();
            
            System.out.println("[SMS] Enviado para: " + to + " | SID: " + twilioMessage.getSid());
        } catch (Exception e) {
            System.err.println("[SMS] Erro ao enviar: " + e.getMessage());
        }
    }
}