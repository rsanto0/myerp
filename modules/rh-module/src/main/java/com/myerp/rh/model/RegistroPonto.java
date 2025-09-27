package com.myerp.rh.model;

import com.myerp.common.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class RegistroPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Usuario funcionario;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoPonto tipo;
    
    private String observacoes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Usuario getFuncionario() { return funcionario; }
    public void setFuncionario(Usuario funcionario) { this.funcionario = funcionario; }

    public TipoPonto getTipo() { return tipo; }
    public void setTipo(TipoPonto tipo) { this.tipo = tipo; }
    
    public Long getFuncionarioId() { 
        return funcionario != null ? funcionario.getId() : null; 
    }
    
    public void setFuncionarioId(Long funcionarioId) { 
        if (funcionarioId != null) {
            Usuario user = new Usuario();
            user.setId(funcionarioId);
            this.funcionario = user;
        } else {
            this.funcionario = null;
        }
    }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    // Compatibility methods
    public void setTipoRegistro(TipoPonto tipo) { this.tipo = tipo; }
    public TipoPonto getTipoRegistro() { return this.tipo; }
}
