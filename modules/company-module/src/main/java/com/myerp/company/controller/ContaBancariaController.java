package com.myerp.company.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myerp.company.entity.ContaBancaria;
import com.myerp.company.enums.BancoBrasil;
import com.myerp.company.enums.TipoConta;
import com.myerp.company.service.ContaBancariaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/company/contas-bancarias")
public class ContaBancariaController {
    
    private static final Logger logger = LoggerFactory.getLogger(ContaBancariaController.class);
    private final ContaBancariaService contaBancariaService;
    
    public ContaBancariaController(ContaBancariaService contaBancariaService) {
        this.contaBancariaService = contaBancariaService;
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarContaBancaria(@Valid @RequestBody ContaBancaria conta) {
        logger.info("[CONTA_API] Criando conta bancária: {} - Empresa: {}", 
                   conta.getNomeConta(), conta.getEmpresaId());
        
        try {
            ContaBancaria criada = contaBancariaService.criarContaBancaria(conta);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Conta bancária criada com sucesso",
                "conta", mapearConta(criada)
            ));
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao criar conta: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao criar conta: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Map<String, Object>> listarContasPorEmpresa(@PathVariable Long empresaId) {
        try {
            List<ContaBancaria> contas = contaBancariaService.listarContasPorEmpresa(empresaId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "total", contas.size(),
                "contas", contas.stream().map(this::mapearConta).toList()
            ));
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao listar contas: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarConta(@PathVariable Long id) {
        try {
            Optional<ContaBancaria> conta = contaBancariaService.buscarPorId(id);
            
            if (conta.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "conta", mapearContaCompleta(conta.get())
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao buscar conta: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}/principal")
    public ResponseEntity<Map<String, Object>> buscarContaPrincipal(@PathVariable Long empresaId) {
        try {
            Optional<ContaBancaria> conta = contaBancariaService.buscarContaPrincipal(empresaId);
            
            if (conta.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "conta", mapearConta(conta.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Nenhuma conta principal definida"
                ));
            }
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao buscar conta principal: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}/banco/{banco}")
    public ResponseEntity<Map<String, Object>> listarContasPorBanco(
            @PathVariable Long empresaId, 
            @PathVariable String banco) {
        
        try {
            BancoBrasil bancoBrasil = BancoBrasil.valueOf(banco.toUpperCase());
            List<ContaBancaria> contas = contaBancariaService.listarContasPorBanco(empresaId, bancoBrasil);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "banco", bancoBrasil.getCodigoNome(),
                "total", contas.size(),
                "contas", contas.stream().map(this::mapearConta).toList()
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Banco inválido: " + banco
            ));
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao listar por banco: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}/boletos")
    public ResponseEntity<Map<String, Object>> listarContasComBoletos(@PathVariable Long empresaId) {
        try {
            List<ContaBancaria> contas = contaBancariaService.listarContasComBoletos(empresaId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "total", contas.size(),
                "contas", contas.stream().map(this::mapearConta).toList()
            ));
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao listar contas com boletos: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/empresa/{empresaId}/pix")
    public ResponseEntity<Map<String, Object>> listarContasComPix(@PathVariable Long empresaId) {
        try {
            List<ContaBancaria> contas = contaBancariaService.listarContasComPix(empresaId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "total", contas.size(),
                "contas", contas.stream().map(this::mapearConta).toList()
            ));
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao listar contas com PIX: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarConta(
            @PathVariable Long id, 
            @RequestBody ContaBancaria dadosAtualizacao) {
        
        try {
            ContaBancaria atualizada = contaBancariaService.atualizarConta(id, dadosAtualizacao);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Conta atualizada com sucesso",
                "conta", mapearConta(atualizada)
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao atualizar conta: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PostMapping("/{id}/ativar")
    public ResponseEntity<Map<String, Object>> ativarConta(@PathVariable Long id) {
        try {
            contaBancariaService.ativarConta(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Conta ativada com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao ativar conta: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PostMapping("/{id}/desativar")
    public ResponseEntity<Map<String, Object>> desativarConta(@PathVariable Long id) {
        try {
            contaBancariaService.desativarConta(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Conta desativada com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao desativar conta: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PostMapping("/{id}/definir-principal")
    public ResponseEntity<Map<String, Object>> definirContaPrincipal(@PathVariable Long id) {
        try {
            contaBancariaService.definirContaPrincipal(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Conta principal definida com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao definir conta principal: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PostMapping("/{id}/sincronizar-saldo")
    public ResponseEntity<Map<String, Object>> sincronizarSaldo(@PathVariable Long id) {
        try {
            contaBancariaService.sincronizarSaldo(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Saldo sincronizado com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao sincronizar saldo: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/bancos-disponiveis")
    public ResponseEntity<Map<String, Object>> listarBancosDisponiveis() {
        try {
            List<Map<String, Object>> bancos = List.of(BancoBrasil.values())
                .stream()
                .map(banco -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("codigo", banco.getCodigo());
                    map.put("nome", banco.getNomeCompleto());
                    map.put("codigoNome", banco.getCodigoNome());
                    map.put("apiUrl", banco.getApiBaseUrl());
                    return map;
                })
                .toList();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "total", bancos.size(),
                "bancos", bancos
            ));
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao listar bancos: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/tipos-conta")
    public ResponseEntity<Map<String, Object>> listarTiposConta() {
        try {
            List<Map<String, Object>> tipos = List.of(TipoConta.values())
                .stream()
                .map(tipo -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("codigo", tipo.name());
                    map.put("descricao", tipo.getDescricao());
                    map.put("sigla", tipo.getSigla());
                    return map;
                })
                .toList();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "total", tipos.size(),
                "tipos", tipos
            ));
            
        } catch (Exception e) {
            logger.error("[CONTA_API] ❌ Erro ao listar tipos de conta: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    // Métodos auxiliares
    private Map<String, Object> mapearConta(ContaBancaria conta) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", conta.getId());
        map.put("nomeConta", conta.getNomeConta());
        map.put("banco", conta.getBanco().getCodigoNome());
        map.put("agencia", conta.getAgencia());
        map.put("conta", conta.getConta());
        map.put("digitoVerificador", conta.getDigitoVerificador() != null ? conta.getDigitoVerificador() : "");
        map.put("contaCompleta", conta.getContaCompleta());
        map.put("tipoConta", conta.getTipoConta().getDescricao());
        map.put("titular", conta.getTitular() != null ? conta.getTitular() : "");
        map.put("saldoAtual", conta.getSaldoAtual());
        map.put("ativa", conta.getAtiva());
        map.put("contaPrincipal", conta.getContaPrincipal());
        map.put("ativaBoletos", conta.getAtivaBoletos());
        map.put("ativaPix", conta.getAtivaPix());
        map.put("ambiente", conta.getAmbiente());
        map.put("integracaoAtiva", conta.isIntegracaoAtiva());
        map.put("dataCriacao", conta.getDataCriacao());
        return map;
    }
    
    private Map<String, Object> mapearContaCompleta(ContaBancaria conta) {
        Map<String, Object> map = mapearConta(conta);
        map.put("cpfCnpjTitular", conta.getCpfCnpjTitular() != null ? conta.getCpfCnpjTitular() : "");
        map.put("limiteCredito", conta.getLimiteCredito());
        map.put("clientId", conta.getClientId() != null ? conta.getClientId() : "");
        map.put("ativaTedDoc", conta.getAtivaTedDoc());
        map.put("ativaConciliacao", conta.getAtivaConciliacao());
        map.put("configuracoesBanco", conta.getConfiguracoesBanco());
        map.put("observacoes", conta.getObservacoes() != null ? conta.getObservacoes() : "");
        map.put("ultimaSincronizacao", conta.getUltimaSincronizacao());
        map.put("dataAtualizacao", conta.getDataAtualizacao());
        // Não expor dados sensíveis (clientSecret, certificados)
        return map;
    }
}