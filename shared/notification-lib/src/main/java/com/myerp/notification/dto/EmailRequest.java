package com.myerp.notification.dto;

import java.util.List;

public class EmailRequest {
    private String to;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String htmlContent;
    private String textContent;
    private List<String> attachments;
    
    // Construtores
    public EmailRequest() {}
    
    public EmailRequest(String to, String subject, String htmlContent) {
        this.to = to;
        this.subject = subject;
        this.htmlContent = htmlContent;
    }
    
    // Getters e Setters
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public List<String> getCc() { return cc; }
    public void setCc(List<String> cc) { this.cc = cc; }
    public List<String> getBcc() { return bcc; }
    public void setBcc(List<String> bcc) { this.bcc = bcc; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getHtmlContent() { return htmlContent; }
    public void setHtmlContent(String htmlContent) { this.htmlContent = htmlContent; }
    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }
    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }
}