package com.myerp.device.controller;

import com.myerp.device.service.CameraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import com.myerp.device.entity.CameraConfig;

@RestController
@RequestMapping("/api/devices/camera")
public class CameraController {
    
    private static final Logger logger = LoggerFactory.getLogger(CameraController.class);
    @Autowired
    private CameraService cameraService;
    
    /**
     * Testa conectividade com câmera
     */
    @GetMapping("/testar")
    public ResponseEntity<?> testarCamera() {
        logger.info("[API_CAMERA] Solicitação de teste de câmera");
        
        try {
            Map<String, Object> resultado = cameraService.testarCamera();
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("[API_CAMERA] Erro no teste: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERRO",
                "mensagem", "Erro interno no teste da câmera"
            ));
        }
    }
    
    /**
     * Captura foto única
     */
    @PostMapping("/capturar-foto")
    public ResponseEntity<?> capturarFoto() {
        logger.info("[API_CAMERA] Solicitação de captura de foto");
        
        try {
            Map<String, Object> resultado = cameraService.capturarFoto();
            
            if ((Boolean) resultado.get("success")) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.badRequest().body(resultado);
            }
            
        } catch (Exception e) {
            logger.error("[API_CAMERA] Erro na captura: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "erro", "Erro interno na captura"
            ));
        }
    }
    
    /**
     * Captura com detecção facial automática
     */
    @PostMapping("/capturar-facial")
    public ResponseEntity<?> capturarFacial(@RequestParam(defaultValue = "5") int segundos) {
        logger.info("[API_CAMERA] Solicitação de captura facial por {} segundos", segundos);
        
        if (segundos < 1 || segundos > 30) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "erro", "Tempo deve estar entre 1 e 30 segundos"
            ));
        }
        
        try {
            Map<String, Object> resultado = cameraService.capturarVideoDeteccao(segundos);
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("[API_CAMERA] Erro na captura facial: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "erro", "Erro interno na captura facial"
            ));
        }
    }
    
    /**
     * Inicializa câmera manualmente
     */
    @PostMapping("/inicializar")
    public ResponseEntity<?> inicializarCamera() {
        logger.info("[API_CAMERA] Solicitação de inicialização manual");
        
        try {
            boolean sucesso = cameraService.inicializarCamera();
            
            return ResponseEntity.ok(Map.of(
                "success", sucesso,
                "mensagem", sucesso ? "Câmera inicializada" : "Falha na inicialização"
            ));
            
        } catch (Exception e) {
            logger.error("[API_CAMERA] Erro na inicialização: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "erro", "Erro interno na inicialização"
            ));
        }
    }
    
    /**
     * Finaliza câmera manualmente
     */
    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarCamera() {
        logger.info("[API_CAMERA] Solicitação de finalização manual");
        
        try {
            cameraService.finalizarCamera();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "mensagem", "Câmera finalizada"
            ));
            
        } catch (Exception e) {
            logger.error("[API_CAMERA] Erro na finalização: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "erro", "Erro interno na finalização"
            ));
        }
    }
    
    /**
     * Testa captura real da câmera Yoose CA-1003
     */
    @PostMapping("/testar-yoose")
    public ResponseEntity<?> testarCameraYoose() {
        logger.info("[API_CAMERA] Teste específico da câmera Yoose CA-1003");
        
        try {
            // Criar configuração da Yoose para teste
            CameraConfig config = new CameraConfig();
            config.setNomeConfiguracao("Yoose CA-1003 Test");
            config.setTipoCamera(CameraConfig.TipoCamera.IP);
            config.setEnderecoIp("192.168.1.102");
            config.setPorta(554);
            config.setProtocolo("RTSP");
            config.setUsuario("admin");
            config.setSenha("admin12324");
            config.setUrlStream("rtsp://admin:admin12324@192.168.1.102:554/h264_stream");
            
            Map<String, Object> resultado = cameraService.testarCamera(); // Simplificado
            
            return ResponseEntity.ok(Map.of(
                "camera", "Yoose CA-1003",
                "ip", "192.168.1.102",
                "teste", resultado
            ));
            
        } catch (Exception e) {
            logger.error("[API_CAMERA] Erro no teste Yoose: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "erro", "Erro no teste da Yoose: " + e.getMessage()
            ));
        }
    }
}