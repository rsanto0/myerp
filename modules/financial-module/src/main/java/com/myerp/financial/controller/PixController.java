package com.myerp.financial.controller;

import com.myerp.financial.entity.TransacaoPix;
import com.myerp.financial.enums.StatusPix;
import com.myerp.financial.service.PixService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial/pix")
public class PixController {
    
    private static final Logger logger = LoggerFactory.getLogger(PixController.class);
    
    @Autowired
    private PixService pixService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarTransacaoPix(@Valid @RequestBody TransacaoPix transacao) {
        logger.info("[CONTROLLER_PIX] Criando transação PIX - Empresa: {}", transacao.getEmpresaId());
        
        try {
            TransacaoPix transacaoSalva = pixService.criarTransacaoPix(transacao);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Transação PIX criada com sucesso",
                "transacao", transacaoSalva
            ));
        } catch (Exception e) {
            logger.error("[CONTROLLER_PIX] Erro ao criar transação PIX: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao criar transação PIX: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Map<String, Object>> listarTransacoesPorEmpresa(@PathVariable Long empresaId) {
        logger.info("[CONTROLLER_PIX] Listando transações PIX da empresa: {}", empresaId);
        
        List<TransacaoPix> transacoes = pixService.listarTransacoesPorEmpresa(empresaId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "transacoes", transacoes,
            "total", transacoes.size()
        ));
    }
    
    @GetMapping("/empresa/{empresaId}/status/{status}")
    public ResponseEntity<Map<String, Object>> listarTransacoesPorStatus(
            @PathVariable Long empresaId, 
            @PathVariable StatusPix status) {
        
        logger.info("[CONTROLLER_PIX] Listando transações PIX - Empresa: {}, Status: {}", empresaId, status);
        
        List<TransacaoPix> transacoes = pixService.listarTransacoesPorStatus(empresaId, status);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "transacoes", transacoes,
            "status", status,
            "total", transacoes.size()
        ));
    }
    
    @GetMapping("/txid/{txid}")
    public ResponseEntity<Map<String, Object>> buscarPorTxid(@PathVariable String txid) {
        logger.info("[CONTROLLER_PIX] Buscando transação PIX por TxID: {}", txid);
        
        return pixService.buscarPorTxid(txid)
                .map(transacao -> ResponseEntity.ok(Map.of(
                    "success", true,
                    "transacao", transacao
                )))
                .orElse(ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Transação PIX não encontrada"
                )));
    }
    
    @GetMapping("/end-to-end/{endToEndId}")
    public ResponseEntity<Map<String, Object>> buscarPorEndToEndId(@PathVariable String endToEndId) {
        logger.info("[CONTROLLER_PIX] Buscando transação PIX por EndToEndId: {}", endToEndId);
        
        return pixService.buscarPorEndToEndId(endToEndId)
                .map(transacao -> ResponseEntity.ok(Map.of(
                    "success", true,
                    "transacao", transacao
                )))
                .orElse(ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Transação PIX não encontrada"
                )));
    }
    
    @PostMapping("/{transacaoId}/processar")
    public ResponseEntity<Map<String, Object>> processarPagamentoPix(@PathVariable Long transacaoId) {
        logger.info("[CONTROLLER_PIX] Processando pagamento PIX - ID: {}", transacaoId);
        
        try {
            TransacaoPix transacaoAtualizada = pixService.processarPagamentoPix(transacaoId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Pagamento PIX processado com sucesso",
                "transacao", transacaoAtualizada
            ));
        } catch (Exception e) {
            logger.error("[CONTROLLER_PIX] Erro ao processar pagamento PIX: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao processar pagamento PIX: " + e.getMessage()
            ));
        }
    }
    
    @PostMapping("/{transacaoId}/cancelar")
    public ResponseEntity<Map<String, Object>> cancelarTransacaoPix(
            @PathVariable Long transacaoId,
            @RequestParam String motivo) {
        
        logger.info("[CONTROLLER_PIX] Cancelando transação PIX - ID: {}", transacaoId);
        
        try {
            TransacaoPix transacaoAtualizada = pixService.cancelarTransacaoPix(transacaoId, motivo);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Transação PIX cancelada com sucesso",
                "transacao", transacaoAtualizada
            ));
        } catch (Exception e) {
            logger.error("[CONTROLLER_PIX] Erro ao cancelar transação PIX: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao cancelar transação PIX: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}/periodo")
    public ResponseEntity<Map<String, Object>> listarTransacoesPorPeriodo(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        
        logger.info("[CONTROLLER_PIX] Listando transações PIX por período - Empresa: {}", empresaId);
        
        List<TransacaoPix> transacoes = pixService.listarTransacoesPorPeriodo(empresaId, inicio, fim);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "transacoes", transacoes,
            "periodo", Map.of("inicio", inicio, "fim", fim),
            "total", transacoes.size()
        ));
    }
    
    @GetMapping("/chave/{chavePix}")
    public ResponseEntity<Map<String, Object>> listarTransacoesPorChave(@PathVariable String chavePix) {
        logger.info("[CONTROLLER_PIX] Listando transações PIX por chave: {}", chavePix);
        
        List<TransacaoPix> transacoes = pixService.listarTransacoesPorChave(chavePix);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "transacoes", transacoes,
            "chavePix", chavePix,
            "total", transacoes.size()
        ));
    }
    
    @GetMapping("/empresa/{empresaId}/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas(@PathVariable Long empresaId) {
        logger.info("[CONTROLLER_PIX] Obtendo estatísticas PIX da empresa: {}", empresaId);
        
        Map<String, Object> estatisticas = pixService.obterEstatisticas(empresaId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "estatisticas", estatisticas
        ));
    }
}