package com.myerp.company.entity;

import com.myerp.company.enums.TipoCargo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cargo")
public class Cargo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;
    
    @Column(name = "nome", nullable = false, length = 100)
    @NotBlank(message = "Nome do cargo é obrigatório")
    private String nome;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cargo")
    private TipoCargo tipoCargo;
    
    @Column(name = "descricao", length = 1000)
    private String descricao;
    
    @Column(name = "nivel_hierarquico")
    private Integer nivelHierarquico;
    
    @Column(name = "salario_base", precision = 10, scale = 2)
    private BigDecimal salarioBase;
    
    @Column(name = "carga_horaria_semanal")
    private Integer cargaHorariaSemanal = 40;
    
    @Column(name = "departamento_id")
    private Long departamentoId;
    
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
    public Cargo() {}
    
    public Cargo(Long empresaId, String nome, TipoCargo tipoCargo) {
        this.empresaId = empresaId;
        this.nome = nome;
        this.tipoCargo = tipoCargo;
        this.nivelHierarquico = tipoCargo.getHierarquia();
    }
    
    // Métodos de conveniência
    public boolean isGerencial() {
        return tipoCargo != null && tipoCargo.isGerencial();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public TipoCargo getTipoCargo() { return tipoCargo; }
    public void setTipoCargo(TipoCargo tipoCargo) { 
        this.tipoCargo = tipoCargo;
        if (tipoCargo != null) {
            this.nivelHierarquico = tipoCargo.getHierarquia();
        }
    }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Integer getNivelHierarquico() { return nivelHierarquico; }
    public void setNivelHierarquico(Integer nivelHierarquico) { this.nivelHierarquico = nivelHierarquico; }
    
    public BigDecimal getSalarioBase() { return salarioBase; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }
    
    public Integer getCargaHorariaSemanal() { return cargaHorariaSemanal; }
    public void setCargaHorariaSemanal(Integer cargaHorariaSemanal) { this.cargaHorariaSemanal = cargaHorariaSemanal; }
    
    public Long getDepartamentoId() { return departamentoId; }
    public void setDepartamentoId(Long departamentoId) { this.departamentoId = departamentoId; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}