package com.myerp.rh.controller;

import com.myerp.common.events.FuncionarioDetectadoEvent;
import com.myerp.rh.service.PontoAutomaticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller para receber eventos biométricos do módulo de biometria
 */
@RestController
@RequestMapping("/api/ponto")
public class EventoBiometricoController {
    
    private static final Logger logger = LoggerFactory.getLogger(EventoBiometricoController.class);
    
    @Autowired
    private PontoAutomaticoService pontoAutomaticoService;
    
    /**
     * Recebe evento de detecção biométrica e processa registro de ponto
     */
    @PostMapping("/evento-biometrico")
    public ResponseEntity<String> receberEventoBiometrico(@RequestBody FuncionarioDetectadoEvent evento) {
        try {
            logger.info("Evento biométrico recebido: funcionário {} detectado às {} no device {}", 
                       evento.getFuncionarioId(), evento.getTimestamp(), evento.getDeviceId());
            
            // Processa o evento e decide se registra ponto automaticamente
            pontoAutomaticoService.processarEventoDeteccao(evento);
            
            return ResponseEntity.ok("Evento processado com sucesso");
            
        } catch (Exception e) {
            logger.error("Erro ao processar evento biométrico: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao processar evento: " + e.getMessage());
        }
    }
}