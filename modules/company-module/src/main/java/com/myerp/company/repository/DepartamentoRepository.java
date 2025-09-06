package com.myerp.company.repository;

import com.myerp.company.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    
    List<Departamento> findByEmpresaIdAndAtivoTrue(Long empresaId);
    
    List<Departamento> findByEmpresaId(Long empresaId);
    
    Optional<Departamento> findByEmpresaIdAndNome(Long empresaId, String nome);
    
    List<Departamento> findByResponsavelId(Long responsavelId);
    
    long countByEmpresaIdAndAtivoTrue(Long empresaId);
}