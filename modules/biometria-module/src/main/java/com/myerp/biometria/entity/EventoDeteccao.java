package com.myerp.biometria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_deteccao")
public class EventoDeteccao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "funcionario_id")
    private Long funcionarioId;
    
    @Column(name = "nome_funcionario")
    private String nomeFuncionario;
    
    @Column(name = "data_hora_deteccao")
    private LocalDateTime dataHoraDeteccao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimento")
    private TipoMovimento tipoMovimento;
    
    @Column(name = "confianca_deteccao")
    private Double confiancaDeteccao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_processamento")
    private StatusProcessamento statusProcessamento;
    
    @Column(name = "ponto_registrado_automaticamente")
    private Boolean pontoRegistradoAutomaticamente = false;
    
    @Column(name = "observacoes")
    private String observacoes;
    
    @Column(name = "device_id")
    private String deviceId;
    
    // Construtores
    public EventoDeteccao() {}
    
    public EventoDeteccao(Long funcionarioId, String nomeFuncionario, 
                         LocalDateTime dataHoraDeteccao, TipoMovimento tipoMovimento, 
                         Double confiancaDeteccao) {
        this.funcionarioId = funcionarioId;
        this.nomeFuncionario = nomeFuncionario;
        this.dataHoraDeteccao = dataHoraDeteccao;
        this.tipoMovimento = tipoMovimento;
        this.confiancaDeteccao = confiancaDeteccao;
        this.statusProcessamento = StatusProcessamento.PENDENTE;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }
    
    public String getNomeFuncionario() { return nomeFuncionario; }
    public void setNomeFuncionario(String nomeFuncionario) { this.nomeFuncionario = nomeFuncionario; }
    
    public LocalDateTime getDataHoraDeteccao() { return dataHoraDeteccao; }
    public void setDataHoraDeteccao(LocalDateTime dataHoraDeteccao) { this.dataHoraDeteccao = dataHoraDeteccao; }
    
    public TipoMovimento getTipoMovimento() { return tipoMovimento; }
    public void setTipoMovimento(TipoMovimento tipoMovimento) { this.tipoMovimento = tipoMovimento; }
    
    public Double getConfiancaDeteccao() { return confiancaDeteccao; }
    public void setConfiancaDeteccao(Double confiancaDeteccao) { this.confiancaDeteccao = confiancaDeteccao; }
    
    public StatusProcessamento getStatusProcessamento() { return statusProcessamento; }
    public void setStatusProcessamento(StatusProcessamento statusProcessamento) { this.statusProcessamento = statusProcessamento; }
    
    public Boolean getPontoRegistradoAutomaticamente() { return pontoRegistradoAutomaticamente; }
    public void setPontoRegistradoAutomaticamente(Boolean pontoRegistradoAutomaticamente) { this.pontoRegistradoAutomaticamente = pontoRegistradoAutomaticamente; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
}

