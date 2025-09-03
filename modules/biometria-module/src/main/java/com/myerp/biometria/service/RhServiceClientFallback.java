package com.myerp.biometria.service;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class RhServiceClientFallback implements RhServiceClient {
    
    @Override
    public void registrarPonto(Long funcionarioId, LocalDateTime dataHora, String tipoMovimento) {
        System.err.println("⚠️ FALLBACK: Módulo RH indisponível. Ponto não registrado automaticamente.");
        System.err.println("Funcionário: " + funcionarioId + ", Data/Hora: " + dataHora + ", Tipo: " + tipoMovimento);
        
        // Em produção, você poderia:
        // 1. Salvar em uma fila para retry posterior
        // 2. Notificar administradores
        // 3. Registrar em log para auditoria
    }
}