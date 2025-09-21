package com.myerp.biometria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myerp.biometria.entity.EventoDeteccao;
import com.myerp.biometria.entity.StatusProcessamento;

@Service
public class DeteccaoService {
    
    @Autowired
    private EventoDeteccaoRepository eventoRepository;
    
    @Autowired
    private EventPublisherService eventPublisherService;
    
    public void processarDeteccao(EventoDeteccao evento) {
        // Salva o evento de detecção
        eventoRepository.save(evento);
        
        // Publica evento para o RH Module processar
        eventPublisherService.publicarFuncionarioDetectado(
            String.valueOf(evento.getFuncionarioId()),
            evento.getDataHoraDeteccao(),
            evento.getDeviceId()
        );
        
        // Atualiza status do evento
        evento.setStatusProcessamento(StatusProcessamento.EVENTO_PUBLICADO);
        evento.setObservacoes("Evento de detecção enviado para processamento no RH Module");
        eventoRepository.save(evento);
        
        System.out.println("[BIOMETRIA] 📡 Evento publicado: " + evento.getNomeFuncionario() + 
                          " detectado às " + evento.getDataHoraDeteccao());
    }
    

}