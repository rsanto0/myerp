package com.myerp.notification.service;

import com.myerp.notification.dto.EmailRequest;
import com.myerp.notification.dto.NotificationRequest;
import com.myerp.notification.enums.NotificationType;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class NotificationService {
    
    private final EmailService emailService;
    private final SmsService smsService;
    private final WhatsAppService whatsAppService;
    private final TemplateService templateService;
    
    public NotificationService(EmailService emailService, SmsService smsService, 
                             WhatsAppService whatsAppService, TemplateService templateService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.whatsAppService = whatsAppService;
        this.templateService = templateService;
    }
    
    // Método simples (compatibilidade)
    public void send(NotificationType type, String to, String subject, String message) {
        switch (type) {
            case EMAIL -> emailService.sendSimpleEmail(to, subject, message);
            case SMS -> smsService.sendSms(to, message);
            case WHATSAPP -> whatsAppService.sendWhatsApp(to, message);
        }
    }
    
    // Método avançado com templates
    public void send(NotificationRequest request) {
        String processedMessage = request.getMessage();
        
        // Processar template se especificado
        if (request.getTemplate() != null && request.getVariables() != null) {
            if (request.getType() == NotificationType.EMAIL) {
                String template = templateService.getEmailTemplate(request.getTemplate());
                processedMessage = templateService.processTemplate(template, request.getVariables());
            } else {
                processedMessage = templateService.processTemplate(request.getMessage(), request.getVariables());
            }
        }
        
        switch (request.getType()) {
            case EMAIL -> {
                if (request.getTemplate() != null) {
                    EmailRequest emailRequest = new EmailRequest(request.getTo(), request.getSubject(), processedMessage);
                    emailService.sendHtmlEmail(emailRequest);
                } else {
                    emailService.sendSimpleEmail(request.getTo(), request.getSubject(), processedMessage);
                }
            }
            case SMS -> smsService.sendSms(request.getTo(), processedMessage);
            case WHATSAPP -> whatsAppService.sendWhatsApp(request.getTo(), processedMessage);
        }
    }
    
    // Métodos de conveniência
    public void sendWelcomeEmail(String to, String nome, String login, String url) {
        NotificationRequest request = new NotificationRequest();
        request.setType(NotificationType.EMAIL);
        request.setTo(to);
        request.setSubject("Bem-vindo ao MyERP!");
        request.setTemplate("welcome");
        request.setVariables(Map.of(
            "nome", nome,
            "login", login,
            "url", url
        ));
        send(request);
    }
    
    public void sendPontoNotification(String to, String nome, String tipo, String dataHora) {
        NotificationRequest request = new NotificationRequest();
        request.setType(NotificationType.EMAIL);
        request.setTo(to);
        request.setSubject("Ponto Registrado - MyERP");
        request.setTemplate("ponto-registro");
        request.setVariables(Map.of(
            "nome", nome,
            "tipo", tipo,
            "dataHora", dataHora
        ));
        send(request);
    }
}