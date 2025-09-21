package com.myerp.rh.service;

import com.myerp.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Serviço responsável por notificações relacionadas ao RH
 */
@Service
public class NotificacaoRhService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificacaoRhService.class);
    
    @Autowired
    private EmailService emailService;
    
    /**
     * Notifica sobre ponto registrado automaticamente
     */
    public void notificarPontoAutomatico(String funcionarioId, String nomeFuncionario, 
                                       LocalDateTime timestamp, String tipo) {
        try {
            String assunto = "Ponto Registrado Automaticamente - " + nomeFuncionario;
            String mensagem = String.format(
                "Olá %s,\n\n" +
                "Seu ponto foi registrado automaticamente:\n\n" +
                "Tipo: %s\n" +
                "Horário: %s\n" +
                "Data: %s\n\n" +
                "Registro realizado via detecção biométrica.\n\n" +
                "Atenciosamente,\n" +
                "Sistema MyERP",
                nomeFuncionario,
                tipo,
                timestamp.toLocalTime(),
                timestamp.toLocalDate()
            );
            
            String emailFuncionario = gerarEmailFuncionario(nomeFuncionario);
            emailService.sendSimpleEmail(emailFuncionario, assunto, mensagem);
            
            logger.info("Notificação de ponto automático enviada para funcionário {}", funcionarioId);
            
        } catch (Exception e) {
            logger.error("Erro ao notificar ponto automático: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Notifica RH sobre sugestão de ponto para validação
     */
    public void notificarSugestaoValidacao(String funcionarioId, String nomeFuncionario,
                                         LocalDateTime timestamp, String tipo) {
        try {
            String assunto = "Sugestão de Ponto para Validação - " + nomeFuncionario;
            String mensagem = String.format(
                "Uma detecção biométrica requer validação:\n\n" +
                "Funcionário: %s (ID: %s)\n" +
                "Tipo: %s\n" +
                "Horário: %s\n" +
                "Data: %s\n\n" +
                "Motivo: Horário fora da tolerância padrão.\n" +
                "Acesse o sistema para validar este registro.\n\n" +
                "Sistema MyERP - Módulo RH",
                nomeFuncionario,
                funcionarioId,
                tipo,
                timestamp.toLocalTime(),
                timestamp.toLocalDate()
            );
            
            emailService.sendSimpleEmail("rh@empresa.com", assunto, mensagem);
            
            logger.info("Sugestão de validação enviada para RH - funcionário {}", funcionarioId);
            
        } catch (Exception e) {
            logger.error("Erro ao notificar sugestão para RH: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Gera email do funcionário baseado no nome (temporário)
     */
    private String gerarEmailFuncionario(String nomeFuncionario) {
        return nomeFuncionario.toLowerCase()
                             .replace(" ", ".")
                             .replaceAll("[^a-z.]", "") + "@empresa.com";
    }
}