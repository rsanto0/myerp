package com.myerp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Login é obrigatório")
    @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres")
    private String login;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 4, message = "Senha deve ter pelo menos 4 caracteres")
    private String senha;
    
    /** @return nome de usuário para autenticação */
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    /** @return senha em texto plano */
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}