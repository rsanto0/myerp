package com.myerp.biometria.controller;

import com.myerp.biometria.entity.BiometriaUsuario;
import com.myerp.biometria.entity.TipoBiometria;
import com.myerp.biometria.service.BiometriaUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/biometria/usuarios")
public class BiometriaUsuarioController {
    
    private static final Logger logger = LoggerFactory.getLogger(BiometriaUsuarioController.class);
    private final BiometriaUsuarioService biometriaService;
    
    public BiometriaUsuarioController(BiometriaUsuarioService biometriaService) {
        this.biometriaService = biometriaService;
    }
    
    /**
     * Cadastra biometria para usuário
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarBiometria(@RequestBody CadastrarBiometriaRequest request) {
        logger.info("[API_BIOMETRIA] Solicitação de cadastro - Usuário: {}, Tipo: {}", 
                   request.getNomeUsuario(), request.getTipo());
        
        try {
            BiometriaUsuario biometria = biometriaService.cadastrarBiometria(
                request.getUsuarioId(),
                request.getNomeUsuario(),
                request.getTipo(),
                request.getDadosBiometricos()
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Biometria cadastrada com sucesso",
                "biometriaId", biometria.getId(),
                "qualidade", biometria.getQualidadeCaptura() * 100 + "%"
            ));
            
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("[API_BIOMETRIA] Erro no cadastro: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    /**
     * Verifica se usuário tem biometria
     */
    @GetMapping("/verificar/{usuarioId}")
    public ResponseEntity<?> verificarBiometria(@PathVariable Long usuarioId) {
        boolean temBiometria = biometriaService.temBiometria(usuarioId);
        
        return ResponseEntity.ok(Map.of(
            "usuarioId", usuarioId,
            "temBiometria", temBiometria
        ));
    }
    
    /**
     * Busca dados da biometria do usuário (sem dados sensíveis)
     */
    @GetMapping("/dados/{usuarioId}")
    public ResponseEntity<?> dadosBiometria(@PathVariable Long usuarioId) {
        Optional<BiometriaUsuario> biometria = biometriaService.buscarPorUsuario(usuarioId);
        
        if (biometria.isPresent()) {
            BiometriaUsuario bio = biometria.get();
            return ResponseEntity.ok(Map.of(
                "usuarioId", bio.getUsuarioId(),
                "nomeUsuario", bio.getNomeUsuario(),
                "tipo", bio.getTipoBiometria(),
                "dataCadastro", bio.getDataCadastro(),
                "qualidade", bio.getQualidadeCaptura() * 100 + "%"
            ));
        }
        
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Remove biometria do usuário
     */
    @DeleteMapping("/remover/{usuarioId}")
    public ResponseEntity<?> removerBiometria(@PathVariable Long usuarioId) {
        logger.info("[API_BIOMETRIA] Solicitação de remoção - Usuário: {}", usuarioId);
        
        try {
            biometriaService.removerBiometria(usuarioId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Biometria removida com sucesso"
            ));
        } catch (Exception e) {
            logger.error("[API_BIOMETRIA] Erro na remoção: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro ao remover biometria"
            ));
        }
    }
    
    /**
     * Estatísticas de biometrias
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<?> estatisticas() {
        BiometriaUsuarioService.BiometriaStats stats = biometriaService.obterEstatisticas();
        
        return ResponseEntity.ok(Map.of(
            "total", stats.getTotal(),
            "facial", stats.getFacial(),
            "digital", stats.getDigital(),
            "iris", stats.getIris(),
            "voz", stats.getVoz()
        ));
    }
    
    /**
     * DTO para cadastro de biometria
     */
    public static class CadastrarBiometriaRequest {
        private Long usuarioId;
        private String nomeUsuario;
        private TipoBiometria tipo;
        private String dadosBiometricos; // Base64
        
        // Getters e Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        
        public String getNomeUsuario() { return nomeUsuario; }
        public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
        
        public TipoBiometria getTipo() { return tipo; }
        public void setTipo(TipoBiometria tipo) { this.tipo = tipo; }
        
        public String getDadosBiometricos() { return dadosBiometricos; }
        public void setDadosBiometricos(String dadosBiometricos) { this.dadosBiometricos = dadosBiometricos; }
    }
}