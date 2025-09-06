package com.myerp.financial.enums;

public enum StatusBoleto {
    PENDENTE("Pendente de Pagamento"),
    PAGO("Pago"),
    VENCIDO("Vencido"),
    CANCELADO("Cancelado"),
    PROCESSANDO("Processando");

    private final String descricao;

    StatusBoleto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}