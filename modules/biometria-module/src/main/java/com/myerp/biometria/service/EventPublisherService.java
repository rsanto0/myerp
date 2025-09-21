package com.myerp.biometria.service;

import com.myerp.common.events.FuncionarioDetectadoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Serviço responsável por publicar eventos de detecção biométrica
 */
@Service
public class EventPublisherService {
    
    private static final Logger logger = LoggerFactory.getLogger(EventPublisherService.class);
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * Publica evento de funcionário detectado
     */
    public void publicarFuncionarioDetectado(String funcionarioId, LocalDateTime timestamp, String deviceId) {
        try {
            // Evento local (para processamento interno se necessário)
            FuncionarioDetectadoEvent evento = new FuncionarioDetectadoEvent(funcionarioId, timestamp, deviceId);
            eventPublisher.publishEvent(evento);
            
            // Notificar RH Module via REST
            notificarRHModule(evento);
            
            logger.info("Evento publicado: funcionário {} detectado às {} no device {}", 
                       funcionarioId, timestamp, deviceId);
            
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de detecção: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Notifica o RH Module sobre a detecção
     */
    private void notificarRHModule(FuncionarioDetectadoEvent evento) {
        try {
            String rhModuleUrl = "http://localhost:8082/api/ponto/evento-biometrico";
            restTemplate.postForObject(rhModuleUrl, evento, String.class);
            
            logger.debug("RH Module notificado sobre detecção do funcionário {}", evento.getFuncionarioId());
            
        } catch (Exception e) {
            logger.warn("Falha ao notificar RH Module: {}", e.getMessage());
            // Não propaga erro - sistema continua funcionando
        }
    }
}