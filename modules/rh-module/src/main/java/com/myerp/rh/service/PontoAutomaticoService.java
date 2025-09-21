package com.myerp.rh.service;

import com.myerp.common.events.FuncionarioDetectadoEvent;
import com.myerp.rh.model.RegistroPonto;
import com.myerp.rh.model.TipoPonto;
import com.myerp.rh.repository.RegistroPontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Serviço responsável por processar eventos biométricos e registrar pontos automaticamente
 */
@Service
public class PontoAutomaticoService {
    
    private static final Logger logger = LoggerFactory.getLogger(PontoAutomaticoService.class);
    
    @Autowired
    private RegistroPontoRepository registroPontoRepository;
    
    @Autowired
    private NotificacaoRhService notificacaoRhService;
    
    /**
     * Processa evento de detecção biométrica
     */
    public void processarEventoDeteccao(FuncionarioDetectadoEvent evento) {
        try {
            LocalDateTime timestamp = evento.getTimestamp();
            
            // Determina tipo de registro baseado no horário
            TipoPonto tipoPonto = determinarTipoPonto(timestamp);
            
            // Verifica se deve registrar automaticamente
            if (dentroToleranciaHorario(timestamp, tipoPonto)) {
                registrarPontoAutomatico(evento.getFuncionarioId(), timestamp, tipoPonto, evento.getDeviceId());
            } else {
                criarSugestaoValidacao(evento.getFuncionarioId(), timestamp, tipoPonto, evento.getDeviceId());
            }
            
        } catch (Exception e) {
            logger.error("Erro ao processar evento de detecção: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Determina o tipo de ponto baseado no horário
     */
    private TipoPonto determinarTipoPonto(LocalDateTime timestamp) {
        LocalTime hora = timestamp.toLocalTime();
        
        if (hora.isBefore(LocalTime.of(10, 0))) {
            return TipoPonto.ENTRADA;
        } else if (hora.isBefore(LocalTime.of(13, 0))) {
            return TipoPonto.SAIDA_ALMOCO;
        } else if (hora.isBefore(LocalTime.of(15, 0))) {
            return TipoPonto.RETORNO;
        } else {
            return TipoPonto.SAIDA;
        }
    }
    
    /**
     * Verifica se o horário está dentro da tolerância para registro automático
     */
    private boolean dentroToleranciaHorario(LocalDateTime timestamp, TipoPonto tipo) {
        LocalTime hora = timestamp.toLocalTime();
        
        switch (tipo) {
            case ENTRADA:
                return !hora.isBefore(LocalTime.of(7, 45)) && !hora.isAfter(LocalTime.of(8, 15));
            case SAIDA_ALMOCO:
                return !hora.isBefore(LocalTime.of(11, 45)) && !hora.isAfter(LocalTime.of(12, 15));
            case RETORNO:
                return !hora.isBefore(LocalTime.of(12, 45)) && !hora.isAfter(LocalTime.of(13, 15));
            case SAIDA:
                return !hora.isBefore(LocalTime.of(16, 45)) && !hora.isAfter(LocalTime.of(17, 15));
            default:
                return false;
        }
    }
    
    /**
     * Registra ponto automaticamente
     */
    private void registrarPontoAutomatico(String funcionarioId, LocalDateTime timestamp, 
                                        TipoPonto tipo, String deviceId) {
        try {
            RegistroPonto registro = new RegistroPonto();
            registro.setFuncionarioId(Long.valueOf(funcionarioId));
            registro.setDataHora(timestamp);
            registro.setTipo(tipo);
            registro.setObservacoes("Registro automático via biometria - Device: " + deviceId);
            
            registroPontoRepository.save(registro);
            
            logger.info("Ponto registrado automaticamente: funcionário {} - {} às {}", 
                       funcionarioId, tipo, timestamp);
            
            // Notifica funcionário sobre ponto automático
            notificacaoRhService.notificarPontoAutomatico(funcionarioId, "Funcionário " + funcionarioId, timestamp, tipo.toString());
            
        } catch (Exception e) {
            logger.error("Erro ao registrar ponto automático: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Cria sugestão para validação manual
     */
    private void criarSugestaoValidacao(String funcionarioId, LocalDateTime timestamp, 
                                      TipoPonto tipo, String deviceId) {
        logger.info("Sugestão de ponto criada para validação: funcionário {} - {} às {} (fora da tolerância)", 
                   funcionarioId, tipo, timestamp);
        
        // Notifica RH sobre sugestão de validação
        notificacaoRhService.notificarSugestaoValidacao(funcionarioId, "Funcionário " + funcionarioId, timestamp, tipo.toString());
        
        // Futuramente pode criar uma entidade SugestaoPonto para o RH validar manualmente
    }
}