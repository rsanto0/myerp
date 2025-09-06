package com.myerp.company.enums;

public enum TipoCargo {
    // Presidência
    PRESIDENTE("Presidente", "PRESIDENCIA", 1),
    VICE_PRESIDENTE("Vice-Presidente", "PRESIDENCIA", 2),
    
    // Diretoria
    DIRETOR_EXECUTIVO("Diretor Executivo", "DIRETORIA", 3),
    DIRETOR_FINANCEIRO("Diretor Financeiro", "DIRETORIA", 4),
    DIRETOR_RH("Diretor de RH", "DIRETORIA", 5),
    DIRETOR_TI("Diretor de TI", "DIRETORIA", 6),
    DIRETOR_COMERCIAL("Diretor Comercial", "DIRETORIA", 7),
    
    // Gerência
    GERENTE_VENDAS("Gerente de Vendas", "GERENCIA", 8),
    GERENTE_PRODUCAO("Gerente de Produção", "GERENCIA", 9),
    GERENTE_QUALIDADE("Gerente de Qualidade", "GERENCIA", 10),
    GERENTE_FINANCEIRO("Gerente Financeiro", "GERENCIA", 11),
    GERENTE_RH("Gerente de RH", "GERENCIA", 12),
    
    // Coordenação
    COORDENADOR_PROJETOS("Coordenador de Projetos", "COORDENACAO", 13),
    COORDENADOR_EQUIPE("Coordenador de Equipe", "COORDENACAO", 14),
    COORDENADOR_VENDAS("Coordenador de Vendas", "COORDENACAO", 15),
    
    // Supervisão
    SUPERVISOR_TURNO("Supervisor de Turno", "SUPERVISAO", 16),
    SUPERVISOR_AREA("Supervisor de Área", "SUPERVISAO", 17),
    SUPERVISOR_QUALIDADE("Supervisor de Qualidade", "SUPERVISAO", 18),
    
    // Operacional
    ANALISTA_SENIOR("Analista Sênior", "OPERACIONAL", 19),
    ANALISTA_PLENO("Analista Pleno", "OPERACIONAL", 20),
    ANALISTA_JUNIOR("Analista Júnior", "OPERACIONAL", 21),
    ASSISTENTE("Assistente", "OPERACIONAL", 22),
    TECNICO("Técnico", "OPERACIONAL", 23),
    OPERADOR("Operador", "OPERACIONAL", 24),
    ESTAGIARIO("Estagiário", "OPERACIONAL", 25),
    FUNCIONARIO("Funcionário", "OPERACIONAL", 26);
    
    private final String descricao;
    private final String nivel;
    private final int hierarquia;
    
    TipoCargo(String descricao, String nivel, int hierarquia) {
        this.descricao = descricao;
        this.nivel = nivel;
        this.hierarquia = hierarquia;
    }
    
    public String getDescricao() { return descricao; }
    public String getNivel() { return nivel; }
    public int getHierarquia() { return hierarquia; }
    
    public boolean isGerencial() {
        return nivel.equals("PRESIDENCIA") || nivel.equals("DIRETORIA") || nivel.equals("GERENCIA");
    }
}