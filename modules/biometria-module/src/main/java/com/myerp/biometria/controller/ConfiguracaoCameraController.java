package com.myerp.biometria.controller;

import com.myerp.biometria.entity.ConfiguracaoCamera;
import com.myerp.biometria.repository.ConfiguracaoCameraRepository;
import com.myerp.biometria.service.CameraRealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/biometria/camera/config")
public class ConfiguracaoCameraController {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfiguracaoCameraController.class);
    private final ConfiguracaoCameraRepository configRepository;
    private final CameraRealService cameraService;
    
    public ConfiguracaoCameraController(ConfiguracaoCameraRepository configRepository, 
                                      CameraRealService cameraService) {
        this.configRepository = configRepository;
        this.cameraService = cameraService;
    }
    
    /**
     * Criar nova configuração de câmera
     */
    @PostMapping("/criar")
    public ResponseEntity<?> criarConfiguracao(@RequestBody ConfiguracaoCameraRequest request) {
        logger.info("[CONFIG_CAMERA] Criando configuração: {} - Tipo: {}", 
                   request.getNomeConfiguracao(), request.getTipoCamera());
        
        try {
            // Verificar se nome ou alias já existem
            Optional<ConfiguracaoCamera> existenteNome = configRepository
                .findByNomeConfiguracaoIgnoreCase(request.getNomeConfiguracao());
            Optional<ConfiguracaoCamera> existenteAlias = configRepository
                .findByAliasIgnoreCase(request.getAlias());
            
            if (existenteNome.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Já existe uma configuração com este nome"
                ));
            }
            
            if (existenteAlias.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Já existe uma configuração com este alias"
                ));
            }
            
            // Criar nova configuração
            ConfiguracaoCamera config = new ConfiguracaoCamera();
            config.setNomeConfiguracao(request.getNomeConfiguracao());
            config.setAlias(request.getAlias());
            config.setMarcaModelo(request.getMarcaModelo());
            config.setTipoCamera(ConfiguracaoCamera.TipoCamera.valueOf(request.getTipoCamera()));
            config.setResolucao(request.getResolucao());
            config.setSistemaOperacional(request.getSistemaOperacional());
            
            // Configurações específicas por tipo
            if ("USB".equals(request.getTipoCamera())) {
                config.setIndiceUsb(request.getIndiceUsb());
                config.setVidPid(request.getVidPid());
            } else if ("IP".equals(request.getTipoCamera())) {
                config.setEnderecoIp(request.getEnderecoIp());
                config.setPorta(request.getPorta());
                config.setProtocolo(request.getProtocolo());
                config.setUsuario(request.getUsuario());
                config.setSenha(request.getSenha());
                config.setUrlStream(request.getUrlStream());
            }
            
            // Configurações opcionais
            if (request.getFps() != null) config.setFps(request.getFps());
            if (request.getQualidadeJpeg() != null) config.setQualidadeJpeg(request.getQualidadeJpeg());
            if (request.getTimeoutConexao() != null) config.setTimeoutConexao(request.getTimeoutConexao());
            
            ConfiguracaoCamera salva = configRepository.save(config);
            
            logger.info("[CONFIG_CAMERA] ✅ Configuração criada - ID: {}", salva.getId());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Configuração criada com sucesso",
                "configId", salva.getId()
            ));
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] ❌ Erro ao criar configuração: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro interno ao criar configuração"
            ));
        }
    }
    
    /**
     * Listar todas as configurações
     */
    @GetMapping("/listar")
    public ResponseEntity<?> listarConfiguracoes() {
        try {
            List<ConfiguracaoCamera> configs = configRepository.findAll();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "configuracoes", configs.stream().map(this::mapearConfiguracao).toList()
            ));
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] Erro ao listar configurações: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro ao listar configurações"
            ));
        }
    }
    
    /**
     * Ativar configuração específica
     */
    @PostMapping("/ativar/{id}")
    public ResponseEntity<?> ativarConfiguracao(@PathVariable Long id) {
        logger.info("[CONFIG_CAMERA] Ativando configuração ID: {}", id);
        
        try {
            Optional<ConfiguracaoCamera> configOpt = configRepository.findById(id);
            
            if (configOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Desativar todas as outras
            List<ConfiguracaoCamera> todas = configRepository.findAll();
            todas.forEach(c -> c.setAtiva(false));
            configRepository.saveAll(todas);
            
            // Ativar a selecionada
            ConfiguracaoCamera config = configOpt.get();
            config.setAtiva(true);
            config.setDataAtualizacao(LocalDateTime.now());
            configRepository.save(config);
            
            logger.info("[CONFIG_CAMERA] ✅ Configuração ativada: {}", config.getNomeConfiguracao());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Configuração ativada: " + config.getNomeConfiguracao()
            ));
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] Erro ao ativar configuração: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro ao ativar configuração"
            ));
        }
    }
    
    /**
     * Testar configuração específica
     */
    @PostMapping("/testar/{id}")
    public ResponseEntity<?> testarConfiguracao(@PathVariable Long id) {
        logger.info("[CONFIG_CAMERA] Testando configuração ID: {}", id);
        
        try {
            Optional<ConfiguracaoCamera> configOpt = configRepository.findById(id);
            
            if (configOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            ConfiguracaoCamera config = configOpt.get();
            
            // Aplicar configuração temporariamente e testar
            Map<String, Object> resultado = cameraService.testarComConfiguracao(config);
            
            // Salvar resultado do teste
            config.setUltimoTeste(LocalDateTime.now());
            config.setStatusUltimoTeste((Boolean) resultado.get("conectada") ? "SUCESSO" : "FALHA");
            configRepository.save(config);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] Erro ao testar configuração: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro ao testar configuração"
            ));
        }
    }
    
    /**
     * Obter configuração ativa
     */
    @GetMapping("/ativa")
    public ResponseEntity<?> obterConfiguracaoAtiva() {
        try {
            Optional<ConfiguracaoCamera> ativa = configRepository.findByAtivaTrue();
            
            if (ativa.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "configuracao", mapearConfiguracao(ativa.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Nenhuma configuração ativa"
                ));
            }
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] Erro ao obter configuração ativa: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro interno"
            ));
        }
    }
    
    /**
     * Deletar configuração
     */
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarConfiguracao(@PathVariable Long id) {
        logger.info("[CONFIG_CAMERA] Deletando configuração ID: {}", id);
        
        try {
            Optional<ConfiguracaoCamera> configOpt = configRepository.findById(id);
            
            if (configOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            ConfiguracaoCamera config = configOpt.get();
            
            if (config.getAtiva()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Não é possível deletar a configuração ativa"
                ));
            }
            
            configRepository.delete(config);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Configuração deletada com sucesso"
            ));
            
        } catch (Exception e) {
            logger.error("[CONFIG_CAMERA] Erro ao deletar configuração: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Erro ao deletar configuração"
            ));
        }
    }
    
    // Método auxiliar para mapear configuração (sem senha)
    private Map<String, Object> mapearConfiguracao(ConfiguracaoCamera config) {
        return Map.of(
            "id", config.getId(),
            "nomeConfiguracao", config.getNomeConfiguracao(),
            "alias", config.getAlias() != null ? config.getAlias() : "",
            "marcaModelo", config.getMarcaModelo() != null ? config.getMarcaModelo() : "",
            "tipoCamera", config.getTipoCamera().name(),
            "resolucao", config.getResolucao() != null ? config.getResolucao() : "",
            "ativa", config.getAtiva(),
            "ultimoTeste", config.getUltimoTeste(),
            "statusUltimoTeste", config.getStatusUltimoTeste() != null ? config.getStatusUltimoTeste() : "NUNCA_TESTADO"
        );
    }
    
    // DTO para request
    public static class ConfiguracaoCameraRequest {
        private String nomeConfiguracao;
        private String alias;
        private String marcaModelo;
        private String tipoCamera; // USB, IP, SERIAL, SDK
        private String resolucao;
        private String sistemaOperacional;
        
        // USB
        private Integer indiceUsb;
        private String vidPid;
        
        // IP
        private String enderecoIp;
        private Integer porta;
        private String protocolo;
        private String usuario;
        private String senha;
        private String urlStream;
        
        // Opcionais
        private Integer fps;
        private Integer qualidadeJpeg;
        private Integer timeoutConexao;
        
        // Getters e Setters
        public String getNomeConfiguracao() { return nomeConfiguracao; }
        public void setNomeConfiguracao(String nomeConfiguracao) { this.nomeConfiguracao = nomeConfiguracao; }
        
        public String getAlias() { return alias; }
        public void setAlias(String alias) { this.alias = alias; }
        
        public String getMarcaModelo() { return marcaModelo; }
        public void setMarcaModelo(String marcaModelo) { this.marcaModelo = marcaModelo; }
        
        public String getTipoCamera() { return tipoCamera; }
        public void setTipoCamera(String tipoCamera) { this.tipoCamera = tipoCamera; }
        
        public String getResolucao() { return resolucao; }
        public void setResolucao(String resolucao) { this.resolucao = resolucao; }
        
        public String getSistemaOperacional() { return sistemaOperacional; }
        public void setSistemaOperacional(String sistemaOperacional) { this.sistemaOperacional = sistemaOperacional; }
        
        public Integer getIndiceUsb() { return indiceUsb; }
        public void setIndiceUsb(Integer indiceUsb) { this.indiceUsb = indiceUsb; }
        
        public String getVidPid() { return vidPid; }
        public void setVidPid(String vidPid) { this.vidPid = vidPid; }
        
        public String getEnderecoIp() { return enderecoIp; }
        public void setEnderecoIp(String enderecoIp) { this.enderecoIp = enderecoIp; }
        
        public Integer getPorta() { return porta; }
        public void setPorta(Integer porta) { this.porta = porta; }
        
        public String getProtocolo() { return protocolo; }
        public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
        
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        
        public String getUrlStream() { return urlStream; }
        public void setUrlStream(String urlStream) { this.urlStream = urlStream; }
        
        public Integer getFps() { return fps; }
        public void setFps(Integer fps) { this.fps = fps; }
        
        public Integer getQualidadeJpeg() { return qualidadeJpeg; }
        public void setQualidadeJpeg(Integer qualidadeJpeg) { this.qualidadeJpeg = qualidadeJpeg; }
        
        public Integer getTimeoutConexao() { return timeoutConexao; }
        public void setTimeoutConexao(Integer timeoutConexao) { this.timeoutConexao = timeoutConexao; }
    }
}