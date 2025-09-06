package com.myerp.company.repository;

import com.myerp.company.entity.Cargo;
import com.myerp.company.enums.TipoCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
    
    List<Cargo> findByEmpresaIdAndAtivoTrue(Long empresaId);
    
    List<Cargo> findByEmpresaId(Long empresaId);
    
    Optional<Cargo> findByEmpresaIdAndNome(Long empresaId, String nome);
    
    List<Cargo> findByDepartamentoId(Long departamentoId);
    
    List<Cargo> findByTipoCargo(TipoCargo tipoCargo);
    
    @Query("SELECT c FROM Cargo c WHERE c.empresaId = :empresaId AND c.ativo = true ORDER BY c.nivelHierarquico ASC")
    List<Cargo> findByEmpresaIdOrderByHierarquia(@Param("empresaId") Long empresaId);
    
    @Query("SELECT c FROM Cargo c WHERE c.empresaId = :empresaId AND c.nivelHierarquico <= :nivel AND c.ativo = true")
    List<Cargo> findCargosGerenciais(@Param("empresaId") Long empresaId, @Param("nivel") int nivel);
}