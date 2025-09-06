package com.exemplo.ponto.repository;

import com.exemplo.ponto.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Funcionario findByLogin(String login);
}
