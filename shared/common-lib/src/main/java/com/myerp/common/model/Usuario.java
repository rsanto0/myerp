package com.myerp.common.model;

import com.myerp.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidade unificada para usuários do sistema MyERP
 * Substitui as antigas entidades User (auth-service) e Funcionario (rh-module)
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 2, max = 100)
    private String nome;
    
    @NotBlank
    @Size(min = 11, max = 11)
    private String cpf;
    
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true)
    private String login;
    
    @NotBlank
    @Size(min = 4)
    private String senha;
    
    @Enumerated(EnumType.STRING)
    private Role role = Role.FUNCIONARIO;
    
    // Construtores
    public Usuario() {}
    
    public Usuario(String nome, String cpf, String login, String senha, Role role) {
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.role = role;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', login='" + login + "', role=" + role + "}";
    }
}