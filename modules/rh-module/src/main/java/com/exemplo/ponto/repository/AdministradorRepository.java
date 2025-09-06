package com.exemplo.ponto.repository;

import com.exemplo.ponto.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Administrador findByLogin(String login);
}