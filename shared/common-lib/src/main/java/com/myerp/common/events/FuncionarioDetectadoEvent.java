package com.myerp.common.events;

import java.time.LocalDateTime;

/**
 * Evento emitido quando um funcionário é detectado pelo sistema biométrico
 */
public class FuncionarioDetectadoEvent {
    
    private String funcionarioId;
    private LocalDateTime timestamp;
    private String deviceId;
    private String localizacao;
    private Double confiabilidade;
    
    public FuncionarioDetectadoEvent() {}
    
    public FuncionarioDetectadoEvent(String funcionarioId, LocalDateTime timestamp, String deviceId) {
        this.funcionarioId = funcionarioId;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }
    
    // Getters e Setters
    public String getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(String funcionarioId) { this.funcionarioId = funcionarioId; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    
    public Double getConfiabilidade() { return confiabilidade; }
    public void setConfiabilidade(Double confiabilidade) { this.confiabilidade = confiabilidade; }
    
    @Override
    public String toString() {
        return String.format("FuncionarioDetectadoEvent{funcionarioId='%s', timestamp=%s, deviceId='%s'}", 
                           funcionarioId, timestamp, deviceId);
    }
}