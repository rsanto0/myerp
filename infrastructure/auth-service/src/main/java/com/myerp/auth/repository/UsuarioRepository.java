package com.myerp.auth.repository;

import com.myerp.common.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Busca usuário pelo login único
     * @param login nome de usuário
     * @return usuário encontrado ou null
     */
    Usuario findByLogin(String login);
}