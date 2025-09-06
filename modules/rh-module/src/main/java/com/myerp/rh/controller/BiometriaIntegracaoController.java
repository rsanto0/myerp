package com.myerp.rh.controller;

import com.myerp.rh.client.BiometriaServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/biometria")
public class BiometriaIntegracaoController {
    
    private static final Logger logger = LoggerFactory.getLogger(BiometriaIntegracaoController.class);
    private final BiometriaServiceClient biometriaClient;
    
    public BiometriaIntegracaoController(BiometriaServiceClient biometriaClient) {
        this.biometriaClient = biometriaClient;
    }
    
    /**
     * Cadastra biometria para funcionário (integração RH → Biometria)
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarBiometriaFuncionario(
            @RequestHeader("X-User-Role") String userRole,
            @RequestBody CadastrarBiometriaIntegracaoRequest request) {
        
        logger.info("[RH_BIOMETRIA] Solicitação de cadastro de biometria - Usuário: {}, Tipo: {}", 
                   request.getNomeUsuario(), request.getTipo());
        
        // Validar role de admin
        if (!"ADMIN".equals(userRole)) {
            logger.warn("[RH_BIOMETRIA] Acesso negado - Role: {}", userRole);
            return ResponseEntity.status(403).body(Map.of(
                "success", false,
                "message", "Acesso negado. Apenas administradores podem cadastrar biometria."
            ));
        }
        
        try {
            // Criar request para o serviço de biometria
            BiometriaServiceClient.CadastrarBiometriaRequest biometriaRequest = 
                new BiometriaServiceClient.CadastrarBiometriaRequest(
                    request.getUsuarioId(),
                    request.getNomeUsuario(),
                    request.getTipo(),
                    request.getDadosBiometricos()
                );
            
            // Chamar serviço de biometria
            Map<String, Object> resultado = biometriaClient.cadastrarBiometria(biometriaRequest);
            
            logger.info("[RH_BIOMETRIA] Resultado do cadastro: {}", resultado);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("[RH_BIOMETRIA] Erro ao cadastrar biometria: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro interno ao cadastrar biometria"
            ));
        }
    }
    
    /**
     * Verifica se funcionário tem biometria cadastrada
     */
    @GetMapping("/verificar/{usuarioId}")
    public ResponseEntity<?> verificarBiometriaFuncionario(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable Long usuarioId) {
        
        logger.info("[RH_BIOMETRIA] Verificação de biometria - Usuário ID: {}", usuarioId);
        
        // Validar role de admin
        if (!"ADMIN".equals(userRole)) {
            return ResponseEntity.status(403).body(Map.of(
                "success", false,
                "message", "Acesso negado"
            ));
        }
        
        try {
            Map<String, Object> resultado = biometriaClient.verificarBiometria(usuarioId);
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("[RH_BIOMETRIA] Erro ao verificar biometria: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro ao verificar biometria"
            ));
        }
    }
    
    /**
     * Remove biometria do funcionário
     */
    @DeleteMapping("/remover/{usuarioId}")
    public ResponseEntity<?> removerBiometriaFuncionario(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable Long usuarioId) {
        
        logger.info("[RH_BIOMETRIA] Remoção de biometria - Usuário ID: {}", usuarioId);
        
        // Validar role de admin
        if (!"ADMIN".equals(userRole)) {
            return ResponseEntity.status(403).body(Map.of(
                "success", false,
                "message", "Acesso negado"
            ));
        }
        
        try {
            Map<String, Object> resultado = biometriaClient.removerBiometria(usuarioId);
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("[RH_BIOMETRIA] Erro ao remover biometria: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro ao remover biometria"
            ));
        }
    }
    
    /**
     * DTO para integração de cadastro de biometria
     */
    public static class CadastrarBiometriaIntegracaoRequest {
        private Long usuarioId;
        private String nomeUsuario;
        private String tipo; // FACIAL, DIGITAL, IRIS, VOZ
        private String dadosBiometricos; // Base64
        
        // Getters e Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        
        public String getNomeUsuario() { return nomeUsuario; }
        public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getDadosBiometricos() { return dadosBiometricos; }
        public void setDadosBiometricos(String dadosBiometricos) { this.dadosBiometricos = dadosBiometricos; }
    }
}