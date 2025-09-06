package com.exemplo.ponto.dto;

import com.exemplo.ponto.model.TipoPonto;
import jakarta.validation.constraints.NotNull;

public class RegistrarPontoRequest {
    
    @NotNull(message = "Tipo do ponto é obrigatório")
    private TipoPonto tipo;
    
    public RegistrarPontoRequest() {}
    
    public RegistrarPontoRequest(TipoPonto tipo) {
        this.tipo = tipo;
    }
    
    public TipoPonto getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoPonto tipo) {
        this.tipo = tipo;
    }
}