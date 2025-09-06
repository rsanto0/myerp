package com.myerp.biometria.entity;

public enum TipoBiometria {
    FACIAL("Reconhecimento Facial"),
    DIGITAL("Impressão Digital"),
    IRIS("Reconhecimento de Íris"),
    VOZ("Reconhecimento de Voz");
    
    private final String descricao;
    
    TipoBiometria(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}