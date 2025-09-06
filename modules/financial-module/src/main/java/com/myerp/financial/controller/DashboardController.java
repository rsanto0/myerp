package com.myerp.financial.controller;

import com.myerp.financial.service.BoletoService;
import com.myerp.financial.service.PixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/financial/dashboard")
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @Autowired
    private BoletoService boletoService;
    
    @Autowired
    private PixService pixService;
    
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Map<String, Object>> getDashboardCompleto(@PathVariable Long empresaId) {
        logger.info("[DASHBOARD_FINANCEIRO] Obtendo dashboard completo da empresa: {}", empresaId);
        
        try {
            Map<String, Object> estatisticasBoletos = boletoService.obterEstatisticas(empresaId);
            Map<String, Object> estatisticasPix = pixService.obterEstatisticas(empresaId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "empresa", Map.of("id", empresaId),
                "boletos", estatisticasBoletos,
                "pix", estatisticasPix,
                "resumo", Map.of(
                    "totalBoletos", estatisticasBoletos.get("total"),
                    "totalPix", estatisticasPix.get("total"),
                    "boletosPendentes", estatisticasBoletos.get("pendentes"),
                    "pixPendentes", estatisticasPix.get("pendentes")
                )
            ));
        } catch (Exception e) {
            logger.error("[DASHBOARD_FINANCEIRO] Erro ao obter dashboard: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao obter dashboard: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}/resumo")
    public ResponseEntity<Map<String, Object>> getResumoFinanceiro(@PathVariable Long empresaId) {
        logger.info("[RESUMO_FINANCEIRO] Obtendo resumo financeiro da empresa: {}", empresaId);
        
        try {
            Map<String, Object> estatisticasBoletos = boletoService.obterEstatisticas(empresaId);
            Map<String, Object> estatisticasPix = pixService.obterEstatisticas(empresaId);
            
            Long totalTransacoes = (Long) estatisticasBoletos.get("total") + (Long) estatisticasPix.get("total");
            Long totalPendentes = (Long) estatisticasBoletos.get("pendentes") + (Long) estatisticasPix.get("pendentes");
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "resumo", Map.of(
                    "totalTransacoes", totalTransacoes,
                    "totalPendentes", totalPendentes,
                    "boletosPagos", estatisticasBoletos.get("pagos"),
                    "pixAprovados", estatisticasPix.get("aprovadas"),
                    "boletosVencidos", estatisticasBoletos.get("vencidos"),
                    "pixRejeitados", estatisticasPix.get("rejeitadas")
                )
            ));
        } catch (Exception e) {
            logger.error("[RESUMO_FINANCEIRO] Erro ao obter resumo: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao obter resumo: " + e.getMessage()
            ));
        }
    }
}