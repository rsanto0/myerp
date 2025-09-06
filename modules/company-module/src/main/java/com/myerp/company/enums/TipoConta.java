package com.myerp.company.enums;

public enum TipoConta {
    CORRENTE("Conta Corrente", "CC"),
    POUPANCA("Conta Poupança", "CP"),
    PAGAMENTO("Conta de Pagamento", "PG"),
    SALARIO("Conta Salário", "CS"),
    INVESTIMENTO("Conta Investimento", "CI");
    
    private final String descricao;
    private final String sigla;
    
    TipoConta(String descricao, String sigla) {
        this.descricao = descricao;
        this.sigla = sigla;
    }
    
    public String getDescricao() { return descricao; }
    public String getSigla() { return sigla; }
}