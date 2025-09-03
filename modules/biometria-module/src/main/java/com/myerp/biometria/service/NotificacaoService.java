package com.myerp.biometria.service;

import com.myerp.biometria.entity.EventoDeteccao;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {
    
    public void notificarRhSugestaoPonto(EventoDeteccao evento) {
        String assunto = "Sugestão de Ponto - " + evento.getNomeFuncionario();
        String mensagem = String.format(
            "Detectamos movimento do funcionário %s em %s.\n\n" +
            "Tipo: %s\n" +
            "Horário: %s\n" +
            "Confiança: %.1f%%\n\n" +
            "O horário está fora da tolerância padrão. " +
            "Acesse o sistema para validar este registro.\n\n" +
            "Sistema MyERP - Módulo Biometria",
            evento.getNomeFuncionario(),
            evento.getDataHoraDeteccao().toLocalDate(),
            evento.getTipoMovimento(),
            evento.getDataHoraDeteccao().toLocalTime(),
            evento.getConfiancaDeteccao() * 100
        );
        
        enviarEmail("rh@empresa.com", assunto, mensagem);
        System.out.println("📧 Email enviado para RH: Sugestão de ponto - " + evento.getNomeFuncionario());
    }
    
    public void notificarInconsistencia(EventoDeteccao evento) {
        // Email para RH
        String assuntoRh = "Inconsistência Detectada - " + evento.getNomeFuncionario();
        String mensagemRh = String.format(
            "Detectamos uma possível inconsistência:\n\n" +
            "Funcionário: %s\n" +
            "Detecção: %s às %s\n" +
            "Tipo: %s\n\n" +
            "Verifique se há registro manual próximo a este horário.\n\n" +
            "Sistema MyERP - Módulo Biometria",
            evento.getNomeFuncionario(),
            evento.getDataHoraDeteccao().toLocalDate(),
            evento.getDataHoraDeteccao().toLocalTime(),
            evento.getTipoMovimento()
        );
        
        enviarEmail("rh@empresa.com", assuntoRh, mensagemRh);
        
        // Email para funcionário
        String assuntoFunc = "Verificação de Ponto Necessária";
        String mensagemFunc = String.format(
            "Olá %s,\n\n" +
            "Detectamos sua presença na empresa em %s às %s, " +
            "mas não encontramos registro de ponto correspondente.\n\n" +
            "Por favor, verifique se você registrou o ponto corretamente. " +
            "Em caso de dúvidas, entre em contato com o RH.\n\n" +
            "Atenciosamente,\n" +
            "Sistema MyERP",
            evento.getNomeFuncionario(),
            evento.getDataHoraDeteccao().toLocalDate(),
            evento.getDataHoraDeteccao().toLocalTime()
        );
        
        // Simula email do funcionário (em produção, buscar do cadastro)
        String emailFuncionario = evento.getNomeFuncionario().toLowerCase()
                                       .replace(" ", ".") + "@empresa.com";
        
        enviarEmail(emailFuncionario, assuntoFunc, mensagemFunc);
        
        System.out.println("⚠️ Inconsistência notificada: " + evento.getNomeFuncionario());
    }
    
    private void enviarEmail(String destinatario, String assunto, String mensagem) {
        // Email simulado - apenas logs
        System.out.println("📧 [EMAIL SIMULADO] ========================");
        System.out.println("📧 Para: " + destinatario);
        System.out.println("📧 Assunto: " + assunto);
        System.out.println("📧 Mensagem: " + mensagem);
        System.out.println("📧 ========================================");
        System.out.println("✅ [EMAIL] Email enviado com sucesso (simulado)");
    }
}