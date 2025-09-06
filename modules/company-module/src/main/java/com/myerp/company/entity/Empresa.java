package com.myerp.company.entity;

import com.myerp.company.enums.TipoPlano;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Entidade principal que representa uma empresa no sistema MyERP multi-tenant.
 * 
 * <p><b>PROCESSO DE NEGÓCIO:</b> Esta classe é o <b>core do sistema multi-tenant</b>,
 * controlando todas as empresas cadastradas e suas configurações específicas.
 * Trabalha em conjunto com <b>TODOS os módulos</b> do sistema.
 * 
 * <p><b>INTEGRAÇÕES PRINCIPAIS:</b>
 * <ul>
 *   <li><b>Financial Module (8086)</b>: Fornece dados bancários via {@code ContaBancaria}</li>
 *   <li><b>RH Module (8082)</b>: Limita número de funcionários baseado no {@code TipoPlano}</li>
 *   <li><b>Biometria Module (8083)</b>: Feature flags controlam funcionalidades avançadas</li>
 *   <li><b>Monitoring Module (8084)</b>: Agrupa empresas por {@code grupoManutenção}</li>
 *   <li><b>Auth Service (8081)</b>: Validação de usuários por empresa</li>
 * </ul>
 * 
 * <p><b>CARACTERÍSTICAS MULTI-TENANT:</b>
 * <ul>
 *   <li><b>Isolamento de dados</b>: Cada empresa tem seus próprios dados</li>
 *   <li><b>Feature flags dinâmicas</b>: Funcionalidades habilitadas por empresa</li>
 *   <li><b>Configurações personalizadas</b>: Parâmetros específicos por empresa</li>
 *   <li><b>Grupos de manutenção</b>: Deploy escalonado (A/B/C)</li>
 * </ul>
 * 
 * <p><b>FLUXO DE USO:</b>
 * <ol>
 *   <li>Empresa é criada com {@code TipoPlano} específico</li>
 *   <li>Feature flags são configuradas baseadas no plano</li>
 *   <li>Módulos consultam configurações via {@code empresaId}</li>
 *   <li>Limites e funcionalidades são aplicados dinamicamente</li>
 * </ol>
 * 
 * @author MyERP Team
 * @version 1.0
 * @since 1.0
 * @see TipoPlano
 * @see com.myerp.company.entity.ContaBancaria
 * @see com.myerp.financial.client.CompanyClient
 * @see com.myerp.rh.controller.AdminController
 */
@Entity
@Table(name = "empresa")
public class Empresa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Identificação
    @Column(name = "razao_social", nullable = false, length = 255)
    @NotBlank(message = "Razão social é obrigatória")
    private String razaoSocial;
    
    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;
    
    @Column(name = "cnpj", unique = true, length = 18)
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
    private String cnpj;
    
    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;
    
    @Column(name = "inscricao_municipal", length = 20)
    private String inscricaoMunicipal;
    
    // Endereço
    @Column(name = "endereco", length = 500)
    private String endereco;
    
    @Column(name = "cidade", length = 100)
    private String cidade;
    
    @Column(name = "estado", length = 2)
    private String estado;
    
    @Column(name = "cep", length = 10)
    private String cep;
    
    // Contato
    @Column(name = "telefone", length = 20)
    private String telefone;
    
    @Column(name = "email", length = 100)
    @Email(message = "Email deve ser válido")
    private String email;
    
    @Column(name = "website", length = 255)
    private String website;
    
    // Configurações
    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo; // Base64
    
    @Column(name = "timezone", length = 50)
    private String timezone = "America/Sao_Paulo";
    
    @Column(name = "moeda", length = 3)
    private String moeda = "BRL";
    
    // Plano e Multi-tenant
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_plano", nullable = false)
    private TipoPlano tipoPlano = TipoPlano.BASIC;
    
    @Column(name = "instancia_dedicada")
    private Boolean instanciaDedicada = false;
    
    @Column(name = "grupo_manutencao", length = 1)
    private String grupoManutenção = "C";
    
    @Column(name = "versao_sistema", length = 10)
    private String versaoSistema = "1.0.0";
    
    // Feature Flags e Configurações
    @ElementCollection
    @CollectionTable(name = "empresa_configuracao", joinColumns = @JoinColumn(name = "empresa_id"))
    @MapKeyColumn(name = "chave")
    @Column(name = "valor")
    private Map<String, String> configuracoes = new HashMap<>();
    
    @ElementCollection
    @CollectionTable(name = "empresa_feature_flag", joinColumns = @JoinColumn(name = "empresa_id"))
    @MapKeyColumn(name = "feature")
    @Column(name = "ativa")
    private Map<String, Boolean> featureFlags = new HashMap<>();
    
    // Status
    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;
    
    @Column(name = "data_fundacao")
    private LocalDateTime dataFundacao;
    
    @Column(name = "data_vencimento_plano")
    private LocalDateTime dataVencimentoPlano;
    
    // Auditoria
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public Empresa() {}
    
    public Empresa(String razaoSocial, String cnpj) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }
    
    // Métodos de conveniência
    public void adicionarConfiguracao(String chave, String valor) {
        this.configuracoes.put(chave, valor);
    }
    
    public String getConfiguracao(String chave) {
        return this.configuracoes.get(chave);
    }
    
    public void habilitarFeature(String feature) {
        this.featureFlags.put(feature, true);
    }
    
    public void desabilitarFeature(String feature) {
        this.featureFlags.put(feature, false);
    }
    
    public boolean isFeatureAtiva(String feature) {
        return this.featureFlags.getOrDefault(feature, false);
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getInscricaoEstadual() { return inscricaoEstadual; }
    public void setInscricaoEstadual(String inscricaoEstadual) { this.inscricaoEstadual = inscricaoEstadual; }
    
    public String getInscricaoMunicipal() { return inscricaoMunicipal; }
    public void setInscricaoMunicipal(String inscricaoMunicipal) { this.inscricaoMunicipal = inscricaoMunicipal; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public String getMoeda() { return moeda; }
    public void setMoeda(String moeda) { this.moeda = moeda; }
    
    public TipoPlano getTipoPlano() { return tipoPlano; }
    public void setTipoPlano(TipoPlano tipoPlano) { this.tipoPlano = tipoPlano; }
    
    public Boolean getInstanciaDedicada() { return instanciaDedicada; }
    public void setInstanciaDedicada(Boolean instanciaDedicada) { this.instanciaDedicada = instanciaDedicada; }
    
    public String getGrupoManutenção() { return grupoManutenção; }
    public void setGrupoManutenção(String grupoManutenção) { this.grupoManutenção = grupoManutenção; }
    
    public String getVersaoSistema() { return versaoSistema; }
    public void setVersaoSistema(String versaoSistema) { this.versaoSistema = versaoSistema; }
    
    public Map<String, String> getConfiguracoes() { return configuracoes; }
    public void setConfiguracoes(Map<String, String> configuracoes) { this.configuracoes = configuracoes; }
    
    public Map<String, Boolean> getFeatureFlags() { return featureFlags; }
    public void setFeatureFlags(Map<String, Boolean> featureFlags) { this.featureFlags = featureFlags; }
    
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
    
    public LocalDateTime getDataFundacao() { return dataFundacao; }
    public void setDataFundacao(LocalDateTime dataFundacao) { this.dataFundacao = dataFundacao; }
    
    public LocalDateTime getDataVencimentoPlano() { return dataVencimentoPlano; }
    public void setDataVencimentoPlano(LocalDateTime dataVencimentoPlano) { this.dataVencimentoPlano = dataVencimentoPlano; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}