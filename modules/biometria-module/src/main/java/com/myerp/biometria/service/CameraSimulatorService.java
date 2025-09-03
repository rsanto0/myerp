package com.myerp.biometria.service;

import com.myerp.biometria.entity.EventoDeteccao;
import com.myerp.biometria.entity.TipoMovimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CameraSimulatorService {
    
    @Autowired
    private DeteccaoService deteccaoService;
    
    private final Random random = new Random();
    private final List<String> funcionariosSimulados = Arrays.asList(
        "João Silva", "Maria Santos", "Pedro Oliveira", "Ana Costa", "Carlos Lima"
    );
    
    // Simula detecções a cada 30 segundos para testes
    @Scheduled(fixedRate = 30000)
    public void simularDeteccaoFacial() {
        if (shouldSimulateDetection()) {
            String funcionario = funcionariosSimulados.get(random.nextInt(funcionariosSimulados.size()));
            Long funcionarioId = (long) (funcionariosSimulados.indexOf(funcionario) + 1);
            
            TipoMovimento movimento = determinarTipoMovimento();
            double confianca = 0.85 + (random.nextDouble() * 0.14); // 85-99%
            
            EventoDeteccao evento = new EventoDeteccao(
                funcionarioId, 
                funcionario, 
                LocalDateTime.now(), 
                movimento, 
                confianca
            );
            
            deteccaoService.processarDeteccao(evento);
            System.out.println("🎥 SIMULAÇÃO: " + funcionario + " - " + movimento + " (" + String.format("%.1f", confianca * 100) + "%)");
        }
    }
    
    private boolean shouldSimulateDetection() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        
        // Simula mais detecções nos horários de pico
        if (hour >= 7 && hour <= 9) return random.nextDouble() < 0.7; // Entrada
        if (hour >= 11 && hour <= 13) return random.nextDouble() < 0.5; // Almoço
        if (hour >= 17 && hour <= 19) return random.nextDouble() < 0.6; // Saída
        
        return random.nextDouble() < 0.1; // Outras horas
    }
    
    private TipoMovimento determinarTipoMovimento() {
        int hour = LocalDateTime.now().getHour();
        
        if (hour >= 7 && hour <= 9) return TipoMovimento.ENTRADA;
        if (hour >= 11 && hour <= 12) return TipoMovimento.SAIDA_ALMOCO;
        if (hour >= 13 && hour <= 14) return TipoMovimento.RETORNO_ALMOCO;
        if (hour >= 17 && hour <= 19) return TipoMovimento.SAIDA;
        
        // Horário aleatório
        TipoMovimento[] tipos = TipoMovimento.values();
        return tipos[random.nextInt(tipos.length)];
    }
    
    // Método para simular detecção manual (para testes via API)
    public void simularDeteccaoManual(String nomeFuncionario, TipoMovimento movimento) {
        Long funcionarioId = (long) (funcionariosSimulados.indexOf(nomeFuncionario) + 1);
        
        EventoDeteccao evento = new EventoDeteccao(
            funcionarioId,
            nomeFuncionario,
            LocalDateTime.now(),
            movimento,
            0.95 // Alta confiança para simulação manual
        );
        
        deteccaoService.processarDeteccao(evento);
    }
    
    // Método para simular detecção com horário específico
    public void simularDeteccaoComHorario(String nomeFuncionario, TipoMovimento movimento, String horario) {
        Long funcionarioId = (long) (funcionariosSimulados.indexOf(nomeFuncionario) + 1);
        
        // Parse do horário (formato HH:mm)
        String[] parts = horario.split(":");
        int hora = Integer.parseInt(parts[0]);
        int minuto = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        
        LocalDateTime dataHora = LocalDateTime.now()
            .withHour(hora)
            .withMinute(minuto)
            .withSecond(0)
            .withNano(0);
        
        EventoDeteccao evento = new EventoDeteccao(
            funcionarioId,
            nomeFuncionario,
            dataHora,
            movimento,
            0.95
        );
        
        deteccaoService.processarDeteccao(evento);
    }
}