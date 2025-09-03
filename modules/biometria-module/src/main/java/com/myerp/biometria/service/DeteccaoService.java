package com.myerp.biometria.service;

import com.myerp.biometria.entity.EventoDeteccao;
import com.myerp.biometria.entity.StatusProcessamento;
import com.myerp.biometria.entity.TipoMovimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DeteccaoService {
    
    @Autowired
    private EventoDeteccaoRepository eventoRepository;
    
    @Autowired
    private NotificacaoService notificacaoService;
    
    @Autowired
    private RhServiceClient rhServiceClient;
    
    public void processarDeteccao(EventoDeteccao evento) {
        // Salva o evento
        eventoRepository.save(evento);
        
        // Analisa se deve registrar automaticamente
        if (dentroToleranciaHorario(evento)) {
            registrarPontoAutomatico(evento);
        } else {
            criarSugestaoParaRh(evento);
        }
        
        // Verifica inconsistências
        verificarInconsistencias(evento);
    }
    
    private boolean dentroToleranciaHorario(EventoDeteccao evento) {
        LocalTime horaDeteccao = evento.getDataHoraDeteccao().toLocalTime();
        TipoMovimento tipo = evento.getTipoMovimento();
        
        System.out.println("🕰️ Verificando tolerância: " + tipo + " às " + horaDeteccao);
        
        boolean dentroTolerancia = false;
        
        // Horários padrão com tolerância de ±15 minutos
        switch (tipo) {
            case ENTRADA:
                dentroTolerancia = !horaDeteccao.isBefore(LocalTime.of(7, 45)) && 
                                  !horaDeteccao.isAfter(LocalTime.of(8, 15));
                System.out.println("🕰️ ENTRADA: 07:45-08:15, Atual: " + horaDeteccao + ", Dentro: " + dentroTolerancia);
                break;
            case SAIDA_ALMOCO:
                dentroTolerancia = !horaDeteccao.isBefore(LocalTime.of(11, 45)) && 
                                  !horaDeteccao.isAfter(LocalTime.of(12, 15));
                System.out.println("🕰️ SAIDA_ALMOCO: 11:45-12:15, Atual: " + horaDeteccao + ", Dentro: " + dentroTolerancia);
                break;
            case RETORNO_ALMOCO:
                dentroTolerancia = !horaDeteccao.isBefore(LocalTime.of(12, 45)) && 
                                  !horaDeteccao.isAfter(LocalTime.of(13, 15));
                System.out.println("🕰️ RETORNO_ALMOCO: 12:45-13:15, Atual: " + horaDeteccao + ", Dentro: " + dentroTolerancia);
                break;
            case SAIDA:
                dentroTolerancia = !horaDeteccao.isBefore(LocalTime.of(16, 45)) && 
                                  !horaDeteccao.isAfter(LocalTime.of(17, 15));
                System.out.println("🕰️ SAIDA: 16:45-17:15, Atual: " + horaDeteccao + ", Dentro: " + dentroTolerancia);
                break;
            default:
                dentroTolerancia = false;
        }
        
        return dentroTolerancia;
    }
    
    private void registrarPontoAutomatico(EventoDeteccao evento) {
        System.out.println("🔄 Tentando registrar ponto automático: " + evento.getNomeFuncionario() + " - " + evento.getTipoMovimento());
        
        try {
            // Chama o módulo RH para registrar o ponto
            rhServiceClient.registrarPonto(evento.getFuncionarioId(), 
                                         evento.getDataHoraDeteccao(), 
                                         evento.getTipoMovimento().name());
            
            evento.setPontoRegistradoAutomaticamente(true);
            evento.setStatusProcessamento(StatusProcessamento.PROCESSADO_AUTOMATICO);
            evento.setObservacoes("Ponto registrado automaticamente - dentro da tolerância");
            
            eventoRepository.save(evento);
            
            System.out.println("✅ Ponto registrado automaticamente: " + evento.getNomeFuncionario());
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar ponto automático: " + e.getMessage());
            e.printStackTrace();
            criarSugestaoParaRh(evento);
        }
    }
    
    private void criarSugestaoParaRh(EventoDeteccao evento) {
        evento.setStatusProcessamento(StatusProcessamento.AGUARDANDO_VALIDACAO);
        evento.setObservacoes("Fora da tolerância de horário - aguardando validação do RH");
        eventoRepository.save(evento);
        
        // Notifica RH
        notificacaoService.notificarRhSugestaoPonto(evento);
        
        System.out.println("⏰ Sugestão criada para RH: " + evento.getNomeFuncionario() + 
                          " - " + evento.getTipoMovimento());
    }
    
    private void verificarInconsistencias(EventoDeteccao evento) {
        // Verifica se há ponto manual registrado próximo ao horário
        LocalDateTime inicio = evento.getDataHoraDeteccao().minusMinutes(30);
        LocalDateTime fim = evento.getDataHoraDeteccao().plusMinutes(30);
        
        // Aqui você faria a consulta ao módulo RH para verificar pontos manuais
        // Por enquanto, simula uma inconsistência ocasional
        if (Math.random() < 0.2) { // 20% de chance de inconsistência
            notificacaoService.notificarInconsistencia(evento);
        }
    }
}