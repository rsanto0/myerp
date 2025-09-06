package com.myerp.financial.enums;

public enum StatusPix {
    PENDENTE("Pendente"),
    APROVADO("Aprovado"),
    REJEITADO("Rejeitado"),
    CANCELADO("Cancelado"),
    DEVOLVIDO("Devolvido");

    private final String descricao;

    StatusPix(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}