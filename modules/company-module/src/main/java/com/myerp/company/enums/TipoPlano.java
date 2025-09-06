package com.myerp.company.enums;

/**
 * Enum que define os tipos de planos disponíveis no sistema MyERP.
 * 
 * <p><b>PROCESSO DE NEGÓCIO:</b> Esta classe faz parte do processo de <b>Gestão Multi-tenant</b>
 * e trabalha em conjunto com o <b>Company Module (8085)</b> para definir as características
 * e limitações de cada empresa no sistema.
 * 
 * <p><b>INTEGRAÇÕES:</b>
 * <ul>
 *   <li><b>Company Module</b>: Usado pela entidade {@code Empresa} para definir o plano contratado</li>
 *   <li><b>Financial Module</b>: Consultado para validar limites de transações financeiras</li>
 *   <li><b>RH Module</b>: Usado para limitar número de funcionários por empresa</li>
 *   <li><b>Biometria Module</b>: Feature flags controlam acesso a funcionalidades avançadas</li>
 * </ul>
 * 
 * <p>Cada plano possui características específicas que determinam:
 * <ul>
 *   <li>Limite máximo de usuários</li>
 *   <li>Acesso a feature flags avançadas</li>
 *   <li>Grupo de manutenção para deploy (A/B/C)</li>
 * </ul>
 * 
 * <h3>Grupos de Manutenção:</h3>
 * <ul>
 *   <li><b>Grupo A</b>: Clientes premium - máximo 33% de downtime</li>
 *   <li><b>Grupo B</b>: Clientes padrão - deploy intermediário</li>
 *   <li><b>Grupo C</b>: Clientes básicos - deploy final</li>
 * </ul>
 * 
 * <h3>Fluxo de Uso:</h3>
 * <ol>
 *   <li>Empresa é criada no Company Module com um TipoPlano</li>
 *   <li>Financial Module valida limites baseado no plano</li>
 *   <li>RH Module verifica limite de funcionários</li>
 *   <li>Feature flags controlam funcionalidades disponíveis</li>
 * </ol>
 * 
 * @author MyERP Team
 * @version 1.0
 * @since 1.0
 * @see com.myerp.company.entity.Empresa
 * @see com.myerp.financial.service.BoletoService
 * @see com.myerp.rh.controller.AdminController
 */
public enum TipoPlano {
    /** Plano básico - 10 usuários, sem features avançadas, grupo C */
    BASIC("Básico", 10, false, "C"),
    
    /** Plano padrão - 50 usuários, com features, grupo B */
    STANDARD("Padrão", 50, true, "B"), 
    
    /** Plano premium - 200 usuários, com features, grupo A */
    PREMIUM("Premium", 200, true, "A"),
    
    /** Plano corporativo - 1000 usuários, com features, grupo A */
    ENTERPRISE("Corporativo", 1000, true, "A");
    
    /** Descrição amigável do plano */
    private final String descricao;
    
    /** Número máximo de usuários permitidos no plano */
    private final int maxUsuarios;
    
    /** Indica se o plano tem acesso a feature flags avançadas */
    private final boolean featureFlags;
    
    /** Grupo de manutenção para estratégia de deploy (A/B/C) */
    private final String grupoManutenção;
    
    /**
     * Construtor do enum TipoPlano.
     * 
     * @param descricao descrição amigável do plano
     * @param maxUsuarios número máximo de usuários permitidos
     * @param featureFlags se tem acesso a features avançadas
     * @param grupoManutenção grupo para estratégia de deploy (A/B/C)
     */
    TipoPlano(String descricao, int maxUsuarios, boolean featureFlags, String grupoManutenção) {
        this.descricao = descricao;
        this.maxUsuarios = maxUsuarios;
        this.featureFlags = featureFlags;
        this.grupoManutenção = grupoManutenção;
    }
    
    /**
     * Obtém a descrição amigável do plano.
     * 
     * @return descrição do plano
     */
    public String getDescricao() { return descricao; }
    
    /**
     * Obtém o número máximo de usuários permitidos no plano.
     * 
     * @return limite máximo de usuários
     */
    public int getMaxUsuarios() { return maxUsuarios; }
    
    /**
     * Verifica se o plano tem acesso a feature flags avançadas.
     * 
     * @return true se tem acesso a features avançadas, false caso contrário
     */
    public boolean hasFeatureFlags() { return featureFlags; }
    
    /**
     * Obtém o grupo de manutenção para estratégia de deploy.
     * 
     * <p>Grupos disponíveis:
     * <ul>
     *   <li><b>A</b>: Clientes premium (deploy prioritário)</li>
     *   <li><b>B</b>: Clientes padrão (deploy intermediário)</li>
     *   <li><b>C</b>: Clientes básicos (deploy final)</li>
     * </ul>
     * 
     * @return grupo de manutenção (A, B ou C)
     */
    public String getGrupoManutenção() { return grupoManutenção; }
}