package com.myerp.notification.dto;

import com.myerp.notification.enums.NotificationType;
import java.util.Map;

public class NotificationRequest {
    private NotificationType type;
    private String to;
    private String subject;
    private String message;
    private String template;
    private Map<String, Object> variables;
    
    // Construtores
    public NotificationRequest() {}
    
    public NotificationRequest(NotificationType type, String to, String subject, String message) {
        this.type = type;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
    
    // Getters e Setters
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
    public Map<String, Object> getVariables() { return variables; }
    public void setVariables(Map<String, Object> variables) { this.variables = variables; }
}