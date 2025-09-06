package com.myerp.financial.repository;

import com.myerp.financial.entity.ConciliacaoBancaria;
import com.myerp.financial.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConciliacaoBancariaRepository extends JpaRepository<ConciliacaoBancaria, Long> {
    
    List<ConciliacaoBancaria> findByEmpresaId(Long empresaId);
    
    List<ConciliacaoBancaria> findByContaBancariaId(Long contaBancariaId);
    
    List<ConciliacaoBancaria> findByEmpresaIdAndConciliado(Long empresaId, Boolean conciliado);
    
    List<ConciliacaoBancaria> findByDataTransacaoBetween(LocalDate inicio, LocalDate fim);
    
    List<ConciliacaoBancaria> findByTipoTransacao(TipoTransacao tipoTransacao);
    
    @Query("SELECT c FROM ConciliacaoBancaria c WHERE c.empresaId = :empresaId AND c.dataTransacao BETWEEN :inicio AND :fim ORDER BY c.dataTransacao DESC")
    List<ConciliacaoBancaria> findExtratoPorPeriodo(@Param("empresaId") Long empresaId, 
                                                    @Param("inicio") LocalDate inicio, 
                                                    @Param("fim") LocalDate fim);
    
    @Query("SELECT c FROM ConciliacaoBancaria c WHERE c.contaBancariaId = :contaId AND c.conciliado = false ORDER BY c.dataTransacao ASC")
    List<ConciliacaoBancaria> findTransacoesPendenteConciliacao(@Param("contaId") Long contaId);
    
    @Query("SELECT SUM(c.valor) FROM ConciliacaoBancaria c WHERE c.contaBancariaId = :contaId AND c.conciliado = true")
    Double calcularSaldoConciliado(@Param("contaId") Long contaId);
    
    @Query("SELECT COUNT(c) FROM ConciliacaoBancaria c WHERE c.empresaId = :empresaId AND c.conciliado = :conciliado")
    Long countByEmpresaIdAndConciliado(@Param("empresaId") Long empresaId, @Param("conciliado") Boolean conciliado);
}