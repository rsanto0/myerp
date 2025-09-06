package com.myerp.rh.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BiometriaServiceClientFallback implements BiometriaServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(BiometriaServiceClientFallback.class);
    
    @Override
    public Map<String, Object> cadastrarBiometria(CadastrarBiometriaRequest request) {
        logger.warn("[BIOMETRIA_FALLBACK] Serviço de biometria indisponível - cadastro de biometria falhou");
        return Map.of(
            "success", false,
            "message", "Serviço de biometria temporariamente indisponível"
        );
    }
    
    @Override
    public Map<String, Object> verificarBiometria(Long usuarioId) {
        logger.warn("[BIOMETRIA_FALLBACK] Serviço de biometria indisponível - verificação falhou");
        return Map.of(
            "usuarioId", usuarioId,
            "temBiometria", false,
            "erro", "Serviço indisponível"
        );
    }
    
    @Override
    public Map<String, Object> removerBiometria(Long usuarioId) {
        logger.warn("[BIOMETRIA_FALLBACK] Serviço de biometria indisponível - remoção falhou");
        return Map.of(
            "success", false,
            "message", "Serviço de biometria temporariamente indisponível"
        );
    }
}