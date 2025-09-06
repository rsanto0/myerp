package com.myerp.company.repository;

import com.myerp.company.entity.Empresa;
import com.myerp.company.enums.TipoPlano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    Optional<Empresa> findByCnpj(String cnpj);
    
    List<Empresa> findByAtivaTrue();
    
    List<Empresa> findByTipoPlano(TipoPlano tipoPlano);
    
    List<Empresa> findByGrupoManutenção(String grupo);
    
    @Query("SELECT e FROM Empresa e WHERE e.ativa = true AND e.tipoPlano IN :planos")
    List<Empresa> findByPlanosAtivos(@Param("planos") List<TipoPlano> planos);
    
    @Query("SELECT COUNT(e) FROM Empresa e WHERE e.ativa = true")
    long countEmpresasAtivas();
    
    @Query("SELECT e FROM Empresa e WHERE e.featureFlags[:feature] = true")
    List<Empresa> findByFeatureAtiva(@Param("feature") String feature);
}