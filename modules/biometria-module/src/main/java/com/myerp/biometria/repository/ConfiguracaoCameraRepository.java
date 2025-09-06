package com.myerp.biometria.repository;

import com.myerp.biometria.entity.ConfiguracaoCamera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfiguracaoCameraRepository extends JpaRepository<ConfiguracaoCamera, Long> {
    
    /**
     * Busca configuração ativa
     */
    Optional<ConfiguracaoCamera> findByAtivaTrue();
    
    /**
     * Lista todas as configurações por tipo
     */
    List<ConfiguracaoCamera> findByTipoCameraOrderByDataCriacaoDesc(ConfiguracaoCamera.TipoCamera tipo);
    
    /**
     * Busca por nome da configuração
     */
    Optional<ConfiguracaoCamera> findByNomeConfiguracaoIgnoreCase(String nome);
    
    /**
     * Busca por alias
     */
    Optional<ConfiguracaoCamera> findByAliasIgnoreCase(String alias);
    
    /**
     * Lista configurações testadas recentemente
     */
    @Query("SELECT c FROM ConfiguracaoCamera c WHERE c.ultimoTeste IS NOT NULL ORDER BY c.ultimoTeste DESC")
    List<ConfiguracaoCamera> findRecentlyTested();
    
    /**
     * Conta configurações por tipo
     */
    long countByTipoCamera(ConfiguracaoCamera.TipoCamera tipo);
}