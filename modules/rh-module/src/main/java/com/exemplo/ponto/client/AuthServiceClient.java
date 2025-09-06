package com.exemplo.ponto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8080", 
             configuration = AuthServiceClient.FeignConfig.class)
public interface AuthServiceClient {
    
    @PostMapping("/auth/users")
    UserResponse criarUsuario(@RequestBody CreateUserRequest request);
    
    // DTOs internos
    class CreateUserRequest {
        private String nome;
        private String cpf;
        private String login;
        private String senha;
        private String role;
        
        public CreateUserRequest() {}
        
        public CreateUserRequest(String nome, String cpf, String login, String senha, String role) {
            this.nome = nome;
            this.cpf = cpf;
            this.login = login;
            this.senha = senha;
            this.role = role;
        }
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
    
    class UserResponse {
        private Long id;
        private String login;
        private String nome;
        private String cpf;
        private String role;
        
        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
    
    // Configuração de timeout
    @org.springframework.context.annotation.Configuration
    static class FeignConfig {
        
        @org.springframework.context.annotation.Bean
        public feign.Request.Options options() {
            return new feign.Request.Options(
                5000,  // connectTimeout: 5 segundos
                10000  // readTimeout: 10 segundos
            );
        }
    }
}