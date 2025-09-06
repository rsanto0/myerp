package com.myerp.financial.enums;

public enum TipoTransacao {
    BOLETO("Boleto Bancário"),
    PIX("PIX"),
    TED("TED"),
    DOC("DOC"),
    TRANSFERENCIA("Transferência"),
    DEPOSITO("Depósito"),
    SAQUE("Saque"),
    TARIFA("Tarifa Bancária");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}