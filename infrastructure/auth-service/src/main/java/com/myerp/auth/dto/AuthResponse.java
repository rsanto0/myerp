package com.myerp.auth.dto;

public class AuthResponse {
    private String token;
    private String login;
    private String role;
    private Long userId;
    
    /**
     * Construtor com todos os dados de resposta
     */
    public AuthResponse(String token, String login, String role, Long userId) {
        this.token = token;
        this.login = login;
        this.role = role;
        this.userId = userId;
    }
    
    /** @return token JWT gerado */
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    /** @return login do usuário autenticado */
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    /** @return papel do usuário (ADMIN/FUNCIONARIO) */
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    /** @return ID único do usuário */
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}