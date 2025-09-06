package com.myerp.company.enums;

public enum BancoBrasil {
    BANCO_DO_BRASIL("001", "Banco do Brasil S.A.", "https://api.bb.com.br"),
    ITAU("341", "Itaú Unibanco S.A.", "https://api.itau.com.br"),
    BRADESCO("237", "Banco Bradesco S.A.", "https://api.bradesco.com.br"),
    CAIXA("104", "Caixa Econômica Federal", "https://api.caixa.gov.br"),
    SANTANDER("033", "Banco Santander Brasil S.A.", "https://api.santander.com.br"),
    SICOOB("756", "Banco Cooperativo Sicoob S.A.", "https://api.sicoob.com.br"),
    INTER("077", "Banco Inter S.A.", "https://api.bancointer.com.br"),
    NUBANK("260", "Nu Pagamentos S.A.", "https://api.nubank.com.br"),
    C6_BANK("336", "Banco C6 S.A.", "https://api.c6bank.com.br"),
    STONE("197", "Stone Pagamentos S.A.", "https://api.stone.com.br"),
    ORIGINAL("212", "Banco Original S.A.", "https://api.original.com.br"),
    SAFRA("422", "Banco Safra S.A.", "https://api.safra.com.br"),
    BTG_PACTUAL("208", "Banco BTG Pactual S.A.", "https://api.btgpactual.com"),
    VOTORANTIM("655", "Banco Votorantim S.A.", "https://api.bv.com.br"),
    BANRISUL("041", "Banco do Estado do RS S.A.", "https://api.banrisul.com.br");
    
    private final String codigo;
    private final String nomeCompleto;
    private final String apiBaseUrl;
    
    BancoBrasil(String codigo, String nomeCompleto, String apiBaseUrl) {
        this.codigo = codigo;
        this.nomeCompleto = nomeCompleto;
        this.apiBaseUrl = apiBaseUrl;
    }
    
    public String getCodigo() { return codigo; }
    public String getNomeCompleto() { return nomeCompleto; }
    public String getApiBaseUrl() { return apiBaseUrl; }
    
    public String getCodigoNome() {
        return codigo + " - " + nomeCompleto;
    }
    
    public static BancoBrasil findByCodigo(String codigo) {
        for (BancoBrasil banco : values()) {
            if (banco.getCodigo().equals(codigo)) {
                return banco;
            }
        }
        throw new IllegalArgumentException("Banco não encontrado para código: " + codigo);
    }
}