package com.myerp.company.entity;

import com.myerp.company.enums.BancoBrasil;
import com.myerp.company.enums.TipoConta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "conta_bancaria")
public class ContaBancaria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "empresa_id", nullable = false)
    @NotNull(message = "Empresa é obrigatória")
    private Long empresaId;
    
    // Dados básicos da conta
    @Column(name = "nome_conta", nullable = false, length = 100)
    @NotBlank(message = "Nome da conta é obrigatório")
    private String nomeConta; // Ex: "Conta Principal", "Conta Boletos"
    
    @Enumerated(EnumType.STRING)
    @Column(name = "banco", nullable = false)
    @NotNull(message = "Banco é obrigatório")
    private BancoBrasil banco;
    
    @Column(name = "agencia", nullable = false, length = 10)
    @NotBlank(message = "Agência é obrigatória")
    private String agencia;
    
    @Column(name = "conta", nullable = false, length = 20)
    @NotBlank(message = "Conta é obrigatória")
    private String conta;
    
    @Column(name = "digito_verificador", length = 2)
    private String digitoVerificador;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", nullable = false)
    @NotNull(message = "Tipo de conta é obrigatório")
    private TipoConta tipoConta;
    
    // Dados do titular
    @Column(name = "titular", length = 255)
    private String titular;
    
    @Column(name = "cpf_cnpj_titular", length = 18)
    private String cpfCnpjTitular;
    
    // Saldos e limites
    @Column(name = "saldo_atual", precision = 15, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;
    
    @Column(name = "limite_credito", precision = 15, scale = 2)
    private BigDecimal limiteCredito = BigDecimal.ZERO;
    
    // Configurações de integração API
    @Column(name = "client_id", length = 255)
    private String clientId;
    
    @Column(name = "client_secret", columnDefinition = "TEXT")
    private String clientSecret; // Será criptografado
    
    @Column(name = "certificado_digital", columnDefinition = "TEXT")
    private String certificadoDigital; // Base64
    
    @Column(name = "chave_privada", columnDefinition = "TEXT")
    private String chavePrivada; // Base64 criptografado
    
    @Column(name = "ambiente", length = 20)
    private String ambiente = "SANDBOX"; // SANDBOX, PRODUCAO
    
    // Funcionalidades habilitadas
    @Column(name = "ativa_boletos", nullable = false)
    private Boolean ativaBoletos = false;
    
    @Column(name = "ativa_pix", nullable = false)
    private Boolean ativaPix = false;
    
    @Column(name = "ativa_ted_doc", nullable = false)
    private Boolean ativaTedDoc = false;
    
    @Column(name = "ativa_conciliacao", nullable = false)
    private Boolean ativaConciliacao = false;
    
    // Configurações específicas do banco
    @ElementCollection
    @CollectionTable(name = "conta_bancaria_configuracao", joinColumns = @JoinColumn(name = "conta_id"))
    @MapKeyColumn(name = "chave")
    @Column(name = "valor")
    private Map<String, String> configuracoesBanco = new HashMap<>();
    
    // Status e controle
    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;
    
    @Column(name = "conta_principal", nullable = false)
    private Boolean contaPrincipal = false;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    // Auditoria
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "ultima_sincronizacao")
    private LocalDateTime ultimaSincronizacao;
    
    // Construtores
    public ContaBancaria() {}
    
    public ContaBancaria(Long empresaId, String nomeConta, BancoBrasil banco, String agencia, String conta, TipoConta tipoConta) {
        this.empresaId = empresaId;
        this.nomeConta = nomeConta;
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
        this.tipoConta = tipoConta;
    }
    
    // Métodos de conveniência
    public String getContaCompleta() {
        return agencia + "/" + conta + (digitoVerificador != null ? "-" + digitoVerificador : "");
    }
    
    public String getBancoDescricao() {
        return banco.getCodigoNome();
    }
    
    public void adicionarConfiguracao(String chave, String valor) {
        this.configuracoesBanco.put(chave, valor);
    }
    
    public String getConfiguracao(String chave) {
        return this.configuracoesBanco.get(chave);
    }
    
    public boolean isIntegracaoAtiva() {
        return clientId != null && clientSecret != null && ativa;
    }
    
    public boolean isProducao() {
        return "PRODUCAO".equals(ambiente);
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    
    public String getNomeConta() { return nomeConta; }
    public void setNomeConta(String nomeConta) { this.nomeConta = nomeConta; }
    
    public BancoBrasil getBanco() { return banco; }
    public void setBanco(BancoBrasil banco) { this.banco = banco; }
    
    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }
    
    public String getConta() { return conta; }
    public void setConta(String conta) { this.conta = conta; }
    
    public String getDigitoVerificador() { return digitoVerificador; }
    public void setDigitoVerificador(String digitoVerificador) { this.digitoVerificador = digitoVerificador; }
    
    public TipoConta getTipoConta() { return tipoConta; }
    public void setTipoConta(TipoConta tipoConta) { this.tipoConta = tipoConta; }
    
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    
    public String getCpfCnpjTitular() { return cpfCnpjTitular; }
    public void setCpfCnpjTitular(String cpfCnpjTitular) { this.cpfCnpjTitular = cpfCnpjTitular; }
    
    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { this.saldoAtual = saldoAtual; }
    
    public BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(BigDecimal limiteCredito) { this.limiteCredito = limiteCredito; }
    
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    
    public String getCertificadoDigital() { return certificadoDigital; }
    public void setCertificadoDigital(String certificadoDigital) { this.certificadoDigital = certificadoDigital; }
    
    public String getChavePrivada() { return chavePrivada; }
    public void setChavePrivada(String chavePrivada) { this.chavePrivada = chavePrivada; }
    
    public String getAmbiente() { return ambiente; }
    public void setAmbiente(String ambiente) { this.ambiente = ambiente; }
    
    public Boolean getAtivaBoletos() { return ativaBoletos; }
    public void setAtivaBoletos(Boolean ativaBoletos) { this.ativaBoletos = ativaBoletos; }
    
    public Boolean getAtivaPix() { return ativaPix; }
    public void setAtivaPix(Boolean ativaPix) { this.ativaPix = ativaPix; }
    
    public Boolean getAtivaTedDoc() { return ativaTedDoc; }
    public void setAtivaTedDoc(Boolean ativaTedDoc) { this.ativaTedDoc = ativaTedDoc; }
    
    public Boolean getAtivaConciliacao() { return ativaConciliacao; }
    public void setAtivaConciliacao(Boolean ativaConciliacao) { this.ativaConciliacao = ativaConciliacao; }
    
    public Map<String, String> getConfiguracoesBanco() { return configuracoesBanco; }
    public void setConfiguracoesBanco(Map<String, String> configuracoesBanco) { this.configuracoesBanco = configuracoesBanco; }
    
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
    
    public Boolean getContaPrincipal() { return contaPrincipal; }
    public void setContaPrincipal(Boolean contaPrincipal) { this.contaPrincipal = contaPrincipal; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public LocalDateTime getUltimaSincronizacao() { return ultimaSincronizacao; }
    public void setUltimaSincronizacao(LocalDateTime ultimaSincronizacao) { this.ultimaSincronizacao = ultimaSincronizacao; }
}