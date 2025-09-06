package com.exemplo.ponto.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
public class Administrador extends Funcionario {
    
    public Administrador() {
        super();
        setRole(Role.ADMIN);
    }
}