package com.myerp.financial.entity;

import com.myerp.financial.enums.StatusPix;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes_pix")
public class TransacaoPix {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "empresa_id")
    private Long empresaId;
    
    @NotNull
    @Column(name = "conta_bancaria_id")
    private Long contaBancariaId;
    
    @NotBlank
    @Size(max = 32)
    @Column(name = "end_to_end_id", unique = true)
    private String endToEndId;
    
    @NotBlank
    @Size(max = 35)
    @Column(name = "txid")
    private String txid;
    
    @NotNull
    @DecimalMin("0.01")
    @Column(name = "valor", precision = 15, scale = 2)
    private BigDecimal valor;
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "chave_pix")
    private String chavePix;
    
    @Size(max = 140)
    @Column(name = "descricao")
    private String descricao;
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "pagador_nome")
    private String pagadorNome;
    
    @NotBlank
    @Size(max = 18)
    @Column(name = "pagador_documento")
    private String pagadorDocumento;
    
    @Size(max = 200)
    @Column(name = "recebedor_nome")
    private String recebedorNome;
    
    @Size(max = 18)
    @Column(name = "recebedor_documento")
    private String recebedorDocumento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPix status = StatusPix.PENDENTE;
    
    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;
    
    @Column(name = "payload_pix", columnDefinition = "TEXT")
    private String payloadPix;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    // Constructors
    public TransacaoPix() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public Long getContaBancariaId() { return contaBancariaId; }
    public void setContaBancariaId(Long contaBancariaId) { this.contaBancariaId = contaBancariaId; }

    public String getEndToEndId() { return endToEndId; }
    public void setEndToEndId(String endToEndId) { this.endToEndId = endToEndId; }

    public String getTxid() { return txid; }
    public void setTxid(String txid) { this.txid = txid; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getChavePix() { return chavePix; }
    public void setChavePix(String chavePix) { this.chavePix = chavePix; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPagadorNome() { return pagadorNome; }
    public void setPagadorNome(String pagadorNome) { this.pagadorNome = pagadorNome; }

    public String getPagadorDocumento() { return pagadorDocumento; }
    public void setPagadorDocumento(String pagadorDocumento) { this.pagadorDocumento = pagadorDocumento; }

    public String getRecebedorNome() { return recebedorNome; }
    public void setRecebedorNome(String recebedorNome) { this.recebedorNome = recebedorNome; }

    public String getRecebedorDocumento() { return recebedorDocumento; }
    public void setRecebedorDocumento(String recebedorDocumento) { this.recebedorDocumento = recebedorDocumento; }

    public StatusPix getStatus() { return status; }
    public void setStatus(StatusPix status) { this.status = status; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public String getPayloadPix() { return payloadPix; }
    public void setPayloadPix(String payloadPix) { this.payloadPix = payloadPix; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataProcessamento() { return dataProcessamento; }
    public void setDataProcessamento(LocalDateTime dataProcessamento) { this.dataProcessamento = dataProcessamento; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}