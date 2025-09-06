package com.myerp.financial.controller;

import com.myerp.financial.entity.Boleto;
import com.myerp.financial.enums.StatusBoleto;
import com.myerp.financial.service.BoletoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial/boletos")
public class BoletoController {
    
    private static final Logger logger = LoggerFactory.getLogger(BoletoController.class);
    
    @Autowired
    private BoletoService boletoService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarBoleto(@Valid @RequestBody Boleto boleto) {
        logger.info("[CONTROLLER_BOLETO] Criando boleto - Empresa: {}", boleto.getEmpresaId());
        
        try {
            Boleto boletoSalvo = boletoService.criarBoleto(boleto);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Boleto criado com sucesso",
                "boleto", boletoSalvo
            ));
        } catch (Exception e) {
            logger.error("[CONTROLLER_BOLETO] Erro ao criar boleto: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao criar boleto: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Map<String, Object>> listarBoletosPorEmpresa(@PathVariable Long empresaId) {
        logger.info("[CONTROLLER_BOLETO] Listando boletos da empresa: {}", empresaId);
        
        List<Boleto> boletos = boletoService.listarBoletosPorEmpresa(empresaId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "boletos", boletos,
            "total", boletos.size()
        ));
    }
    
    @GetMapping("/empresa/{empresaId}/status/{status}")
    public ResponseEntity<Map<String, Object>> listarBoletosPorStatus(
            @PathVariable Long empresaId, 
            @PathVariable StatusBoleto status) {
        
        logger.info("[CONTROLLER_BOLETO] Listando boletos - Empresa: {}, Status: {}", empresaId, status);
        
        List<Boleto> boletos = boletoService.listarBoletosPorStatus(empresaId, status);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "boletos", boletos,
            "status", status,
            "total", boletos.size()
        ));
    }
    
    @GetMapping("/linha-digitavel/{linhaDigitavel}")
    public ResponseEntity<Map<String, Object>> buscarPorLinhaDigitavel(@PathVariable String linhaDigitavel) {
        logger.info("[CONTROLLER_BOLETO] Buscando boleto por linha digitável");
        
        return boletoService.buscarPorLinhaDigitavel(linhaDigitavel)
                .map(boleto -> ResponseEntity.ok(Map.of(
                    "success", true,
                    "boleto", boleto
                )))
                .orElse(ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Boleto não encontrado"
                )));
    }
    
    @PostMapping("/{boletoId}/pagar")
    public ResponseEntity<Map<String, Object>> processarPagamento(
            @PathVariable Long boletoId,
            @RequestParam BigDecimal valorPago) {
        
        logger.info("[CONTROLLER_BOLETO] Processando pagamento - Boleto: {}, Valor: {}", boletoId, valorPago);
        
        try {
            Boleto boletoAtualizado = boletoService.processarPagamento(boletoId, valorPago);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Pagamento processado com sucesso",
                "boleto", boletoAtualizado
            ));
        } catch (Exception e) {
            logger.error("[CONTROLLER_BOLETO] Erro ao processar pagamento: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao processar pagamento: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}/vencidos")
    public ResponseEntity<Map<String, Object>> listarBoletosVencidos(@PathVariable Long empresaId) {
        logger.info("[CONTROLLER_BOLETO] Listando boletos vencidos da empresa: {}", empresaId);
        
        List<Boleto> boletosVencidos = boletoService.listarBoletosVencidos(empresaId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "boletosVencidos", boletosVencidos,
            "total", boletosVencidos.size()
        ));
    }
    
    @GetMapping("/empresa/{empresaId}/periodo")
    public ResponseEntity<Map<String, Object>> listarBoletosPorPeriodo(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        logger.info("[CONTROLLER_BOLETO] Listando boletos por período - Empresa: {}", empresaId);
        
        List<Boleto> boletos = boletoService.listarBoletosPorPeriodo(empresaId, inicio, fim);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "boletos", boletos,
            "periodo", Map.of("inicio", inicio, "fim", fim),
            "total", boletos.size()
        ));
    }
    
    @GetMapping("/empresa/{empresaId}/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas(@PathVariable Long empresaId) {
        logger.info("[CONTROLLER_BOLETO] Obtendo estatísticas de boletos da empresa: {}", empresaId);
        
        Map<String, Object> estatisticas = boletoService.obterEstatisticas(empresaId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "estatisticas", estatisticas
        ));
    }
}