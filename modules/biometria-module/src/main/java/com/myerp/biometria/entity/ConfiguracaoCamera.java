package com.myerp.biometria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracao_camera")
public class ConfiguracaoCamera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_configuracao", nullable = false)
    private String nomeConfiguracao;
    
    @Column(name = "alias", nullable = true, unique = true)
    private String alias; // Ex: "camera_entrada", "camera_saida", "camera1"
    
    @Column(name = "marca_modelo")
    private String marcaModelo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_camera", nullable = false)
    private TipoCamera tipoCamera;
    
    @Column(name = "resolucao")
    private String resolucao;
    
    @Column(name = "sistema_operacional")
    private String sistemaOperacional;
    
    // Para câmera USB
    @Column(name = "indice_usb")
    private Integer indiceUsb;
    
    @Column(name = "vid_pid")
    private String vidPid;
    
    // Para câmera IP
    @Column(name = "endereco_ip")
    private String enderecoIp;
    
    @Column(name = "porta")
    private Integer porta;
    
    @Column(name = "protocolo")
    private String protocolo;
    
    @Column(name = "usuario")
    private String usuario;
    
    @Column(name = "senha")
    private String senha;
    
    @Column(name = "url_stream")
    private String urlStream;
    
    // Configurações gerais
    @Column(name = "fps")
    private Integer fps = 30;
    
    @Column(name = "qualidade_jpeg")
    private Integer qualidadeJpeg = 80;
    
    @Column(name = "timeout_conexao")
    private Integer timeoutConexao = 5000;
    
    @Column(name = "ativa", nullable = false)
    private Boolean ativa = false;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "ultimo_teste")
    private LocalDateTime ultimoTeste;
    
    @Column(name = "status_ultimo_teste")
    private String statusUltimoTeste;
    
    // Construtores
    public ConfiguracaoCamera() {
        this.dataCriacao = LocalDateTime.now();
    }
    
    @PrePersist
    @PreUpdate
    private void validarDados() {
        if (this.alias == null || this.alias.trim().isEmpty()) {
            this.alias = "camera_" + System.currentTimeMillis();
        }
        if (this.dataAtualizacao == null) {
            this.dataAtualizacao = LocalDateTime.now();
        }
    }
    
    public ConfiguracaoCamera(String nome, TipoCamera tipo) {
        this();
        this.nomeConfiguracao = nome;
        this.tipoCamera = tipo;
        // Gerar alias padrão se não fornecido
        if (this.alias == null || this.alias.trim().isEmpty()) {
            this.alias = "camera_" + System.currentTimeMillis();
        }
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNomeConfiguracao() { return nomeConfiguracao; }
    public void setNomeConfiguracao(String nomeConfiguracao) { this.nomeConfiguracao = nomeConfiguracao; }
    
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    
    public String getMarcaModelo() { return marcaModelo; }
    public void setMarcaModelo(String marcaModelo) { this.marcaModelo = marcaModelo; }
    
    public TipoCamera getTipoCamera() { return tipoCamera; }
    public void setTipoCamera(TipoCamera tipoCamera) { this.tipoCamera = tipoCamera; }
    
    public String getResolucao() { return resolucao; }
    public void setResolucao(String resolucao) { this.resolucao = resolucao; }
    
    public String getSistemaOperacional() { return sistemaOperacional; }
    public void setSistemaOperacional(String sistemaOperacional) { this.sistemaOperacional = sistemaOperacional; }
    
    public Integer getIndiceUsb() { return indiceUsb; }
    public void setIndiceUsb(Integer indiceUsb) { this.indiceUsb = indiceUsb; }
    
    public String getVidPid() { return vidPid; }
    public void setVidPid(String vidPid) { this.vidPid = vidPid; }
    
    public String getEnderecoIp() { return enderecoIp; }
    public void setEnderecoIp(String enderecoIp) { this.enderecoIp = enderecoIp; }
    
    public Integer getPorta() { return porta; }
    public void setPorta(Integer porta) { this.porta = porta; }
    
    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
    
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getUrlStream() { return urlStream; }
    public void setUrlStream(String urlStream) { this.urlStream = urlStream; }
    
    public Integer getFps() { return fps; }
    public void setFps(Integer fps) { this.fps = fps; }
    
    public Integer getQualidadeJpeg() { return qualidadeJpeg; }
    public void setQualidadeJpeg(Integer qualidadeJpeg) { this.qualidadeJpeg = qualidadeJpeg; }
    
    public Integer getTimeoutConexao() { return timeoutConexao; }
    public void setTimeoutConexao(Integer timeoutConexao) { this.timeoutConexao = timeoutConexao; }
    
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public LocalDateTime getUltimoTeste() { return ultimoTeste; }
    public void setUltimoTeste(LocalDateTime ultimoTeste) { this.ultimoTeste = ultimoTeste; }
    
    public String getStatusUltimoTeste() { return statusUltimoTeste; }
    public void setStatusUltimoTeste(String statusUltimoTeste) { this.statusUltimoTeste = statusUltimoTeste; }
    
    // Enum para tipos de câmera
    public enum TipoCamera {
        USB("Câmera USB/Webcam"),
        IP("Câmera IP"),
        SERIAL("Câmera Serial"),
        SDK("SDK Específico");
        
        private final String descricao;
        
        TipoCamera(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
}