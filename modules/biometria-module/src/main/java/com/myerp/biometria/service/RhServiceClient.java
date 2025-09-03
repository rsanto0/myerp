package com.myerp.biometria.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@FeignClient(name = "rh-service", url = "http://localhost:8082", fallback = RhServiceClientFallback.class)
public interface RhServiceClient {
    
    @PostMapping("/pontos/registrar-automatico")
    void registrarPonto(@RequestParam Long funcionarioId, 
                       @RequestParam LocalDateTime dataHora, 
                       @RequestParam String tipoMovimento);
}