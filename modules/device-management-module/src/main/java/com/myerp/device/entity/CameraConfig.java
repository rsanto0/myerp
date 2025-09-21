package com.myerp.device.entity;

public class CameraConfig {
    
    private String nomeConfiguracao;
    private TipoCamera tipoCamera;
    private String enderecoIp;
    private Integer porta;
    private String protocolo;
    private String usuario;
    private String senha;
    private String urlStream;
    
    public enum TipoCamera {
        IP, USB, RTSP
    }
    
    // Constructors
    public CameraConfig() {}
    
    // Getters and Setters
    public String getNomeConfiguracao() { return nomeConfiguracao; }
    public void setNomeConfiguracao(String nomeConfiguracao) { this.nomeConfiguracao = nomeConfiguracao; }
    
    public TipoCamera getTipoCamera() { return tipoCamera; }
    public void setTipoCamera(TipoCamera tipoCamera) { this.tipoCamera = tipoCamera; }
    
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
}