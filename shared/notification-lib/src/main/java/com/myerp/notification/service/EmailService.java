package com.myerp.notification.service;

import com.myerp.notification.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${notification.email.from.email:noreply@myerp.com}")
    private String fromEmail;
    
    @Value("${notification.email.from.name:MyERP Sistema}")
    private String fromName;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            System.out.println("[EMAIL] Enviado para: " + to + " | Assunto: " + subject);
        } catch (Exception e) {
            System.err.println("[EMAIL] Erro ao enviar: " + e.getMessage());
        }
    }
    
    public void sendHtmlEmail(EmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail, fromName);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            
            if (request.getHtmlContent() != null) {
                helper.setText(request.getTextContent(), request.getHtmlContent());
            } else {
                helper.setText(request.getTextContent());
            }
            
            mailSender.send(message);
            System.out.println("[EMAIL] HTML enviado para: " + request.getTo());
        } catch (Exception e) {
            System.err.println("[EMAIL] Erro ao enviar HTML: " + e.getMessage());
        }
    }
}