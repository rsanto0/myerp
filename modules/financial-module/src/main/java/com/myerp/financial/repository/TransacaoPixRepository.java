package com.myerp.financial.repository;

import com.myerp.financial.entity.TransacaoPix;
import com.myerp.financial.enums.StatusPix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoPixRepository extends JpaRepository<TransacaoPix, Long> {
    
    List<TransacaoPix> findByEmpresaId(Long empresaId);
    
    List<TransacaoPix> findByEmpresaIdAndStatus(Long empresaId, StatusPix status);
    
    List<TransacaoPix> findByContaBancariaId(Long contaBancariaId);
    
    Optional<TransacaoPix> findByEndToEndId(String endToEndId);
    
    Optional<TransacaoPix> findByTxid(String txid);
    
    List<TransacaoPix> findByChavePix(String chavePix);
    
    @Query("SELECT t FROM TransacaoPix t WHERE t.empresaId = :empresaId AND t.dataCriacao BETWEEN :inicio AND :fim")
    List<TransacaoPix> findTransacoesPorPeriodo(@Param("empresaId") Long empresaId, 
                                                @Param("inicio") LocalDateTime inicio, 
                                                @Param("fim") LocalDateTime fim);
    
    @Query("SELECT COUNT(t) FROM TransacaoPix t WHERE t.empresaId = :empresaId AND t.status = :status")
    Long countByEmpresaIdAndStatus(@Param("empresaId") Long empresaId, @Param("status") StatusPix status);
    
    @Query("SELECT t FROM TransacaoPix t WHERE t.status = 'PENDENTE' AND t.dataCriacao < :limite")
    List<TransacaoPix> findTransacoesPendentesExpiradas(@Param("limite") LocalDateTime limite);
}