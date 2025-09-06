package com.myerp.biometria.controller;

import com.myerp.biometria.entity.ConfiguracaoCamera;
import com.myerp.biometria.repository.ConfiguracaoCameraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/biometria/camera/manage")
public class ConfiguracaoCameraManagementController {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracaoCameraManagementController.class);
    private final ConfiguracaoCameraRepository repository;
    
    public ConfiguracaoCameraManagementController(ConfiguracaoCameraRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Atualizar configuração existente
     */
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Map<String, Object>> atualizarConfiguracao(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> dados) {
        
        logger.info("[CONFIG_CAMERA] Atualizando configuração ID: {}", id);
        
        try {
            ConfiguracaoCamera config = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuração não encontrada"));
            
            // Atualizar campos básicos
            if (dados.containsKey("nomeConfiguracao")) {
                config.setNomeConfiguracao((String) dados.get("nomeConfiguracao"));
            }
            
            if (dados.containsKey("alias")) {
                String novoAlias = (String) dados.get("alias");
                // Verificar se alias já existe em outra configuração
                Optional<ConfiguracaoCamera> existente = repository.findByAliasIgnoreCase(novoAlias);
                if (existente.isPresent() && !existente.get().getId().equals(id)) {
                    return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Alias já existe: " + novoAlias
                    ));
                }
                config.setAlias(novoAlias);
            }
            
            if (dados.containsKey("marcaModelo")) {
                config.setMarcaModelo((String) dados.get("marcaModelo"));
            }
            
            if (dados.containsKey("resolucao")) {
                config.setResolucao((String) dados.get("resolucao"));
            }
            
            // Campos específicos para IP
            if (dados.containsKey("enderecoIp")) {
                config.setEnderecoIp((String) dados.get("enderecoIp"));
            }
            if (dados.containsKey("porta")) {
                config.setPorta((Integer) dados.get("porta"));
            }
            if (dados.containsKey("usuario")) {
                config.setUsuario((String) dados.get("usuario"));
            }
            if (dados.containsKey("senha")) {
                config.setSenha((String) dados.get("senha"));
            }
            if (dados.containsKey("urlStream")) {
                config.setUrlStream((String) dados.get("urlStream"));
            }
            
            // Campos específicos para USB
            if (dados.containsKey("indiceUsb")) {
                config.setIndiceUsb((Integer) dados.get("indiceUsb"));
            }
            if (dados.containsKey("vidPid")) {
                config.setVidPid((String) dados.get("vidPid"));
            }
            
            // Configurações opcionais
            if (dados.containsKey("fps")) {
                config.setFps((Integer) dados.get("fps"));
            }
            if (dados.containsKey("qualidadeJpeg")) {
                config.setQualidadeJpeg((Integer) dados.get("qualidadeJpeg"));
            }
            if (dados.containsKey("timeoutConexao")) {
                config.setTimeoutConexao((Integer) dados.get("timeoutConexao"));
            }
            
            config.setDataAtualizacao(LocalDateTime.now());
            ConfiguracaoCamera atualizada = repository.save(config);
            
            logger.info("[CONFIG_CAMERA] ✅ Configuração atualizada: {} ({})", 
                       atualizada.getNomeConfiguracao(), atualizada.getAlias());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Configuração atualizada com sucesso",
                "config", Map.of(
                    "id", atualizada.getId(),
                    "nome", atualizada.getNomeConfiguracao(),
                    "alias", atualizada.getAlias(),
                    "tipo", atualizada.getTipoCamera().name(),
                    "dataAtualizacao", atualizada.getDataAtualizacao()
                )
            ));
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] ❌ Erro ao atualizar configuração: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro ao atualizar configuração: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Remover configuração
     */
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Map<String, Object>> removerConfiguracao(@PathVariable Long id) {
        logger.info("[CONFIG_CAMERA] Removendo configuração ID: {}", id);
        
        try {
            ConfiguracaoCamera config = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuração não encontrada"));
            
            // Não permitir remover configuração ativa
            if (config.getAtiva()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Não é possível remover a configuração ativa"
                ));
            }
            
            String nome = config.getNomeConfiguracao();
            String alias = config.getAlias();
            
            repository.deleteById(id);
            
            logger.info("[CONFIG_CAMERA] ✅ Configuração removida: {} ({})", nome, alias);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Configuração removida com sucesso",
                "removedId", id,
                "removedName", nome,
                "removedAlias", alias
            ));
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] ❌ Erro ao remover configuração: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro ao remover configuração: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Buscar configuração por alias
     */
    @GetMapping("/buscar-alias/{alias}")
    public ResponseEntity<Map<String, Object>> buscarPorAlias(@PathVariable String alias) {
        logger.info("[CONFIG_CAMERA] Buscando configuração por alias: {}", alias);
        
        try {
            Optional<ConfiguracaoCamera> config = repository.findByAliasIgnoreCase(alias);
            
            if (config.isPresent()) {
                ConfiguracaoCamera c = config.get();
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "config", Map.of(
                        "id", c.getId(),
                        "nomeConfiguracao", c.getNomeConfiguracao(),
                        "alias", c.getAlias(),
                        "marcaModelo", c.getMarcaModelo() != null ? c.getMarcaModelo() : "",
                        "tipoCamera", c.getTipoCamera().name(),
                        "resolucao", c.getResolucao() != null ? c.getResolucao() : "",
                        "ativa", c.getAtiva(),
                        "ultimoTeste", c.getUltimoTeste(),
                        "statusUltimoTeste", c.getStatusUltimoTeste() != null ? c.getStatusUltimoTeste() : "NUNCA_TESTADO"
                    )
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Configuração não encontrada para alias: " + alias
                ));
            }
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] ❌ Erro ao buscar configuração: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro ao buscar configuração: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Listar configurações por tipo
     */
    @GetMapping("/listar-tipo/{tipo}")
    public ResponseEntity<Map<String, Object>> listarPorTipo(@PathVariable String tipo) {
        try {
            ConfiguracaoCamera.TipoCamera tipoCamera = ConfiguracaoCamera.TipoCamera.valueOf(tipo.toUpperCase());
            var configs = repository.findByTipoCameraOrderByDataCriacaoDesc(tipoCamera);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "tipo", tipo,
                "total", configs.size(),
                "configuracoes", configs.stream().map(c -> Map.of(
                    "id", c.getId(),
                    "nome", c.getNomeConfiguracao(),
                    "alias", c.getAlias(),
                    "ativa", c.getAtiva(),
                    "ultimoTeste", c.getUltimoTeste()
                )).toList()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro ao listar configurações: " + e.getMessage()
            ));
        }
    }
}