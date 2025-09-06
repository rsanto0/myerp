package com.myerp.rh.repository;

import com.myerp.rh.model.RegistroPonto;
import com.myerp.common.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {
    List<RegistroPonto> findByFuncionario(Usuario funcionario);
}
