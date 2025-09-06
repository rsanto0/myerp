package com.myerp.biometria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometria_usuario")
public class BiometriaUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false, unique = true)
    private Long usuarioId;
    
    @Column(name = "nome_usuario", nullable = false)
    private String nomeUsuario;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_biometria", nullable = false)
    private TipoBiometria tipoBiometria;
    
    @Lob
    @Column(name = "dados_biometricos", nullable = false)
    private String dadosBiometricos; // Base64 da imagem/template
    
    @Column(name = "hash_biometria", nullable = false)
    private String hashBiometria; // Hash para comparação rápida
    
    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "qualidade_captura")
    private Double qualidadeCaptura; // 0.0 a 1.0
    
    // Construtores
    public BiometriaUsuario() {
        this.dataCadastro = LocalDateTime.now();
    }
    
    public BiometriaUsuario(Long usuarioId, String nomeUsuario, TipoBiometria tipo) {
        this();
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.tipoBiometria = tipo;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    
    public TipoBiometria getTipoBiometria() { return tipoBiometria; }
    public void setTipoBiometria(TipoBiometria tipoBiometria) { this.tipoBiometria = tipoBiometria; }
    
    public String getDadosBiometricos() { return dadosBiometricos; }
    public void setDadosBiometricos(String dadosBiometricos) { this.dadosBiometricos = dadosBiometricos; }
    
    public String getHashBiometria() { return hashBiometria; }
    public void setHashBiometria(String hashBiometria) { this.hashBiometria = hashBiometria; }
    
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public Double getQualidadeCaptura() { return qualidadeCaptura; }
    public void setQualidadeCaptura(Double qualidadeCaptura) { this.qualidadeCaptura = qualidadeCaptura; }
}