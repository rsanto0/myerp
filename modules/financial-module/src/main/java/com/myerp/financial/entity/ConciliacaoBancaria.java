package com.myerp.financial.entity;

import com.myerp.financial.enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "conciliacao_bancaria")
public class ConciliacaoBancaria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "empresa_id")
    private Long empresaId;
    
    @NotNull
    @Column(name = "conta_bancaria_id")
    private Long contaBancariaId;
    
    @NotNull
    @Column(name = "data_transacao")
    private LocalDate dataTransacao;
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "descricao")
    private String descricao;
    
    @NotNull
    @DecimalMin("0.01")
    @Column(name = "valor", precision = 15, scale = 2)
    private BigDecimal valor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao")
    private TipoTransacao tipoTransacao;
    
    @Size(max = 100)
    @Column(name = "documento_referencia")
    private String documentoReferencia;
    
    @Column(name = "conciliado")
    private Boolean conciliado = false;
    
    @Column(name = "data_conciliacao")
    private LocalDateTime dataConciliacao;
    
    @Size(max = 500)
    @Column(name = "observacoes")
    private String observacoes;
    
    @Column(name = "saldo_anterior", precision = 15, scale = 2)
    private BigDecimal saldoAnterior;
    
    @Column(name = "saldo_atual", precision = 15, scale = 2)
    private BigDecimal saldoAtual;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    // Constructors
    public ConciliacaoBancaria() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public Long getContaBancariaId() { return contaBancariaId; }
    public void setContaBancariaId(Long contaBancariaId) { this.contaBancariaId = contaBancariaId; }

    public LocalDate getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDate dataTransacao) { this.dataTransacao = dataTransacao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public TipoTransacao getTipoTransacao() { return tipoTransacao; }
    public void setTipoTransacao(TipoTransacao tipoTransacao) { this.tipoTransacao = tipoTransacao; }

    public String getDocumentoReferencia() { return documentoReferencia; }
    public void setDocumentoReferencia(String documentoReferencia) { this.documentoReferencia = documentoReferencia; }

    public Boolean getConciliado() { return conciliado; }
    public void setConciliado(Boolean conciliado) { this.conciliado = conciliado; }

    public LocalDateTime getDataConciliacao() { return dataConciliacao; }
    public void setDataConciliacao(LocalDateTime dataConciliacao) { this.dataConciliacao = dataConciliacao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public BigDecimal getSaldoAnterior() { return saldoAnterior; }
    public void setSaldoAnterior(BigDecimal saldoAnterior) { this.saldoAnterior = saldoAnterior; }

    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}