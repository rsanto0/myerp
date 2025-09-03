package com.myerp.biometria.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "email.mock.enabled", havingValue = "true", matchIfMissing = true)
public class MockEmailService {
    
    public void enviarNotificacao(String destinatario, String assunto, String corpo) {
        System.out.println("📧 [EMAIL MOCK] ========================");
        System.out.println("📧 Para: " + destinatario);
        System.out.println("📧 Assunto: " + assunto);
        System.out.println("📧 Corpo: " + corpo);
        System.out.println("📧 ========================================");
        
        // Simula envio bem-sucedido
        System.out.println("✅ [EMAIL] Email enviado com sucesso (simulado)");
    }
    
    public void notificarInconsistencia(String funcionario, String motivo) {
        String assunto = "[BIOMETRIA] Inconsistência detectada - " + funcionario;
        String corpo = String.format(
            "Funcionário: %s\n" +
            "Problema: %s\n" +
            "Data/Hora: %s\n" +
            "Ação: Aguardando validação do RH",
            funcionario, motivo, java.time.LocalDateTime.now()
        );
        
        enviarNotificacao("rh@empresa.com", assunto, corpo);
    }
}