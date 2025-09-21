package com.myerp.biometria.controller;

import com.myerp.biometria.entity.EventoDeteccao;
import com.myerp.biometria.entity.TipoMovimento;
import com.myerp.biometria.entity.StatusProcessamento;
import com.myerp.biometria.service.CameraService;
import com.myerp.biometria.service.EventoDeteccaoRepository;
import com.myerp.common.dto.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/biometria")
@CrossOrigin(origins = "*")
public class BiometriaController {
    
    @Autowired
    private EventoDeteccaoRepository eventoRepository;
    
    @Autowired
    private CameraService cameraService;
    
    @GetMapping("/eventos")
    public BaseResponse<Page<EventoDeteccao>> listarEventos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<EventoDeteccao> eventos = eventoRepository.findAllByOrderByDataHoraDeteccaoDesc(
            PageRequest.of(page, size)
        );
        
        return BaseResponse.success(eventos);
    }
    
    @GetMapping("/eventos/pendentes")
    public BaseResponse<List<EventoDeteccao>> eventosPendentes() {
        List<EventoDeteccao> pendentes = eventoRepository.findByStatusProcessamento(
            StatusProcessamento.AGUARDANDO_VALIDACAO
        );
        
        return BaseResponse.success(pendentes);
    }
    
    @PostMapping("/simular-deteccao")
    public BaseResponse<String> simularDeteccao(
            @RequestParam String nomeFuncionario,
            @RequestParam TipoMovimento movimento,
            @RequestParam(required = false) String horario) {
        
        try {
            // Método removido - usar câmera real
            return BaseResponse.error("Simulação removida - use câmera real");
        } catch (Exception e) {
            return BaseResponse.error("Erro ao simular detecção: " + e.getMessage());
        }
    }
    
    @PutMapping("/eventos/{id}/validar")
    public BaseResponse<String> validarEvento(
            @PathVariable Long id,
            @RequestParam boolean aprovado,
            @RequestParam(required = false) String observacao) {
        
        EventoDeteccao evento = eventoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        
        if (aprovado) {
            evento.setStatusProcessamento(StatusProcessamento.VALIDADO_RH);
            evento.setObservacoes("Validado pelo RH: " + (observacao != null ? observacao : ""));
        } else {
            evento.setStatusProcessamento(StatusProcessamento.REJEITADO);
            evento.setObservacoes("Rejeitado pelo RH: " + (observacao != null ? observacao : ""));
        }
        
        eventoRepository.save(evento);
        
        return BaseResponse.success(aprovado ? "Evento validado" : "Evento rejeitado");
    }
    
    @GetMapping("/dashboard")
    public BaseResponse<DashboardData> dashboard() {
        long totalEventos = eventoRepository.count();
        long eventosPendentes = eventoRepository.countByStatusProcessamento(
            StatusProcessamento.AGUARDANDO_VALIDACAO
        );
        long eventosAutomaticos = eventoRepository.countByStatusProcessamento(
            StatusProcessamento.PROCESSADO_AUTOMATICO
        );
        
        DashboardData data = new DashboardData(totalEventos, eventosPendentes, eventosAutomaticos);
        return BaseResponse.success(data);
    }
    
    public static class DashboardData {
        public long totalEventos;
        public long eventosPendentes;
        public long eventosAutomaticos;
        
        public DashboardData(long totalEventos, long eventosPendentes, long eventosAutomaticos) {
            this.totalEventos = totalEventos;
            this.eventosPendentes = eventosPendentes;
            this.eventosAutomaticos = eventosAutomaticos;
        }
    }
}