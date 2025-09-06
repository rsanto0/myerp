package com.myerp.company.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "departamento")
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;
    
    @Column(name = "nome", nullable = false, length = 100)
    @NotBlank(message = "Nome do departamento é obrigatório")
    private String nome;
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @Column(name = "codigo", length = 10)
    private String codigo;
    
    @Column(name = "centro_custo", length = 20)
    private String centroCusto;
    
    @Column(name = "responsavel_id")
    private Long responsavelId;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    // Auditoria
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public Departamento() {}
    
    public Departamento(Long empresaId, String nome) {
        this.empresaId = empresaId;
        this.nome = nome;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getCentroCusto() { return centroCusto; }
    public void setCentroCusto(String centroCusto) { this.centroCusto = centroCusto; }
    
    public Long getResponsavelId() { return responsavelId; }
    public void setResponsavelId(Long responsavelId) { this.responsavelId = responsavelId; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}