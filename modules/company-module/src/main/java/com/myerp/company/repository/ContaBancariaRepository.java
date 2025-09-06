package com.myerp.company.repository;

import com.myerp.company.entity.ContaBancaria;
import com.myerp.company.enums.BancoBrasil;
import com.myerp.company.enums.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    
    List<ContaBancaria> findByEmpresaIdAndAtivaTrue(Long empresaId);
    
    List<ContaBancaria> findByEmpresaId(Long empresaId);
    
    Optional<ContaBancaria> findByEmpresaIdAndContaPrincipalTrue(Long empresaId);
    
    List<ContaBancaria> findByEmpresaIdAndBanco(Long empresaId, BancoBrasil banco);
    
    List<ContaBancaria> findByEmpresaIdAndTipoConta(Long empresaId, TipoConta tipoConta);
    
    List<ContaBancaria> findByEmpresaIdAndAtivaBoletosTrue(Long empresaId);
    
    List<ContaBancaria> findByEmpresaIdAndAtivaPixTrue(Long empresaId);
    
    @Query("SELECT c FROM ContaBancaria c WHERE c.empresaId = :empresaId AND c.agencia = :agencia AND c.conta = :conta")
    Optional<ContaBancaria> findByEmpresaIdAndAgenciaAndConta(
        @Param("empresaId") Long empresaId, 
        @Param("agencia") String agencia, 
        @Param("conta") String conta
    );
    
    @Query("SELECT c FROM ContaBancaria c WHERE c.empresaId = :empresaId AND c.clientId IS NOT NULL AND c.clientSecret IS NOT NULL AND c.ativa = true")
    List<ContaBancaria> findContasComIntegracao(@Param("empresaId") Long empresaId);
    
    @Query("SELECT c FROM ContaBancaria c WHERE c.empresaId = :empresaId AND c.ambiente = 'PRODUCAO' AND c.ativa = true")
    List<ContaBancaria> findContasProducao(@Param("empresaId") Long empresaId);
    
    long countByEmpresaIdAndAtivaTrue(Long empresaId);
    
    boolean existsByEmpresaIdAndContaPrincipalTrue(Long empresaId);
}