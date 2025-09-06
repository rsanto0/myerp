package com.myerp.biometria.repository;

import com.myerp.biometria.entity.BiometriaUsuario;
import com.myerp.biometria.entity.TipoBiometria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BiometriaUsuarioRepository extends JpaRepository<BiometriaUsuario, Long> {
    
    /**
     * Busca biometria por ID do usuário
     */
    Optional<BiometriaUsuario> findByUsuarioIdAndAtivoTrue(Long usuarioId);
    
    /**
     * Busca biometria por tipo específico
     */
    List<BiometriaUsuario> findByTipoBiometriaAndAtivoTrue(TipoBiometria tipo);
    
    /**
     * Verifica se usuário já tem biometria cadastrada
     */
    boolean existsByUsuarioIdAndAtivoTrue(Long usuarioId);
    
    /**
     * Busca por hash para comparação rápida
     */
    Optional<BiometriaUsuario> findByHashBiometriaAndAtivoTrue(String hash);
    
    /**
     * Lista todas as biometrias ativas
     */
    @Query("SELECT b FROM BiometriaUsuario b WHERE b.ativo = true ORDER BY b.dataCadastro DESC")
    List<BiometriaUsuario> findAllAtivas();
    
    /**
     * Conta biometrias por tipo
     */
    @Query("SELECT COUNT(b) FROM BiometriaUsuario b WHERE b.tipoBiometria = :tipo AND b.ativo = true")
    Long countByTipo(@Param("tipo") TipoBiometria tipo);
}