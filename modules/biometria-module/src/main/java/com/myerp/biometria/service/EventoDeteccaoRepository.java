package com.myerp.biometria.service;

import com.myerp.biometria.entity.EventoDeteccao;
import com.myerp.biometria.entity.StatusProcessamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoDeteccaoRepository extends JpaRepository<EventoDeteccao, Long> {
    
    Page<EventoDeteccao> findAllByOrderByDataHoraDeteccaoDesc(Pageable pageable);
    
    List<EventoDeteccao> findByStatusProcessamento(StatusProcessamento status);
    
    long countByStatusProcessamento(StatusProcessamento status);
    
    List<EventoDeteccao> findByFuncionarioIdOrderByDataHoraDeteccaoDesc(Long funcionarioId);
}