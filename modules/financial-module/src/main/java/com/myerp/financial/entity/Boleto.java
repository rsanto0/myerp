package com.myerp.financial.entity;

import com.myerp.financial.enums.StatusBoleto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa um boleto bancário no sistema financeiro.
 * 
 * <p><b>PROCESSO DE NEGÓCIO:</b> Esta classe faz parte do processo de <b>Gestão Financeira</b>
 * e trabalha em conjunto com o <b>Company Module (8085)</b> para obter dados bancários
 * das empresas e processar pagamentos via boleto.
 * 
 * <p><b>INTEGRAÇÕES:</b>
 * <ul>
 *   <li><b>Company Module (8085)</b>: Obtém dados da {@code ContaBancaria} via {@code CompanyClient}</li>
 *   <li><b>API Gateway (8080)</b>: Endpoints acessíveis via roteamento</li>
 *   <li><b>Auth Service (8081)</b>: Validação de permissões para operações financeiras</li>
 *   <li><b>Monitoring Module (8084)</b>: Métricas de boletos processados</li>
 * </ul>
 * 
 * <p><b>FUNCIONALIDADES:</b>
 * <ul>
 *   <li><b>Geração automática</b>: Código de barras e linha digitável</li>
 *   <li><b>Controle de status</b>: PENDENTE, PAGO, VENCIDO, CANCELADO</li>
 *   <li><b>Validação bancária</b>: Integração com contas do Company Module</li>
 *   <li><b>Processamento de pagamentos</b>: Atualização automática de status</li>
 * </ul>
 * 
 * <p><b>FLUXO DE USO:</b>
 * <ol>
 *   <li>Boleto é criado via {@code BoletoController}</li>
 *   <li>Sistema valida conta bancária no Company Module</li>
 *   <li>Códigos são gerados automaticamente</li>
 *   <li>Cliente efetua pagamento</li>
 *   <li>Status é atualizado via {@code BoletoService}</li>
 * </ol>
 * 
 * @author MyERP Team
 * @version 1.0
 * @since 1.0
 * @see StatusBoleto
 * @see com.myerp.financial.service.BoletoService
 * @see com.myerp.financial.controller.BoletoController
 * @see com.myerp.financial.client.CompanyClient
 */
@Entity
@Table(name = "boletos")
public class Boleto {
    
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
    @Size(max = 100)
    @Column(name = "numero_documento")
    private String numeroDocumento;
    
    @NotBlank
    @Size(max = 47)
    @Column(name = "linha_digitavel", unique = true)
    private String linhaDigitavel;
    
    @NotBlank
    @Size(max = 44)
    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;
    
    @NotNull
    @DecimalMin("0.01")
    @Column(name = "valor", precision = 15, scale = 2)
    private BigDecimal valor;
    
    @NotNull
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
    
    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "sacado_nome")
    private String sacadoNome;
    
    @NotBlank
    @Size(max = 18)
    @Column(name = "sacado_documento")
    private String sacadoDocumento;
    
    @Size(max = 500)
    @Column(name = "instrucoes")
    private String instrucoes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusBoleto status = StatusBoleto.PENDENTE;
    
    @Column(name = "nosso_numero")
    private String nossoNumero;
    
    @Column(name = "valor_pago", precision = 15, scale = 2)
    private BigDecimal valorPago;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    // Constructors
    public Boleto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public Long getContaBancariaId() { return contaBancariaId; }
    public void setContaBancariaId(Long contaBancariaId) { this.contaBancariaId = contaBancariaId; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getLinhaDigitavel() { return linhaDigitavel; }
    public void setLinhaDigitavel(String linhaDigitavel) { this.linhaDigitavel = linhaDigitavel; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public String getSacadoNome() { return sacadoNome; }
    public void setSacadoNome(String sacadoNome) { this.sacadoNome = sacadoNome; }

    public String getSacadoDocumento() { return sacadoDocumento; }
    public void setSacadoDocumento(String sacadoDocumento) { this.sacadoDocumento = sacadoDocumento; }

    public String getInstrucoes() { return instrucoes; }
    public void setInstrucoes(String instrucoes) { this.instrucoes = instrucoes; }

    public StatusBoleto getStatus() { return status; }
    public void setStatus(StatusBoleto status) { this.status = status; }

    public String getNossoNumero() { return nossoNumero; }
    public void setNossoNumero(String nossoNumero) { this.nossoNumero = nossoNumero; }

    public BigDecimal getValorPago() { return valorPago; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}