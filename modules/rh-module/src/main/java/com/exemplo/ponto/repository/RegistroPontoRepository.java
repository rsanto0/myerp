package com.exemplo.ponto.repository;

import com.exemplo.ponto.model.RegistroPonto;
import com.exemplo.ponto.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {
    List<RegistroPonto> findByFuncionario(Funcionario funcionario);
}
