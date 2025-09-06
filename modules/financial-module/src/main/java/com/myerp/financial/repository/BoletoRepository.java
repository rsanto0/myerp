package com.myerp.financial.repository;

import com.myerp.financial.entity.Boleto;
import com.myerp.financial.enums.StatusBoleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Long> {
    
    List<Boleto> findByEmpresaId(Long empresaId);
    
    List<Boleto> findByEmpresaIdAndStatus(Long empresaId, StatusBoleto status);
    
    List<Boleto> findByContaBancariaId(Long contaBancariaId);
    
    Optional<Boleto> findByLinhaDigitavel(String linhaDigitavel);
    
    Optional<Boleto> findByCodigoBarras(String codigoBarras);
    
    List<Boleto> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);
    
    @Query("SELECT b FROM Boleto b WHERE b.empresaId = :empresaId AND b.dataVencimento < :data AND b.status = 'PENDENTE'")
    List<Boleto> findBoletosVencidos(@Param("empresaId") Long empresaId, @Param("data") LocalDate data);
    
    @Query("SELECT b FROM Boleto b WHERE b.empresaId = :empresaId AND b.dataVencimento BETWEEN :inicio AND :fim")
    List<Boleto> findBoletosPorPeriodo(@Param("empresaId") Long empresaId, 
                                       @Param("inicio") LocalDate inicio, 
                                       @Param("fim") LocalDate fim);
    
    @Query("SELECT COUNT(b) FROM Boleto b WHERE b.empresaId = :empresaId AND b.status = :status")
    Long countByEmpresaIdAndStatus(@Param("empresaId") Long empresaId, @Param("status") StatusBoleto status);
}