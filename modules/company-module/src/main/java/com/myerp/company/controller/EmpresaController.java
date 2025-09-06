package com.myerp.company.controller;

import com.myerp.company.entity.Empresa;
import com.myerp.company.enums.TipoPlano;
import com.myerp.company.service.EmpresaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/company/empresas")
public class EmpresaController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);
    private final EmpresaService empresaService;
    
    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarEmpresa(@Valid @RequestBody Empresa empresa) {
        logger.info("[EMPRESA_API] Criando empresa: {}", empresa.getRazaoSocial());
        
        try {
            Empresa criada = empresaService.criarEmpresa(empresa);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empresa criada com sucesso",
                "empresa", Map.of(
                    "id", criada.getId(),
                    "razaoSocial", criada.getRazaoSocial(),
                    "cnpj", criada.getCnpj() != null ? criada.getCnpj() : "",
                    "tipoPlano", criada.getTipoPlano().name(),
                    "grupoManutenção", criada.getGrupoManutenção()
                )
            ));
            
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao criar empresa: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao criar empresa: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarEmpresas() {
        try {
            List<Empresa> empresas = empresaService.listarAtivas();
            
            List<Map<String, Object>> empresasDto = empresas.stream()
                .map(this::mapearEmpresa)
                .toList();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "total", empresas.size(),
                "empresas", empresasDto
            ));
            
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao listar empresas: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarEmpresa(@PathVariable Long id) {
        try {
            Optional<Empresa> empresa = empresaService.buscarPorId(id);
            
            if (empresa.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "empresa", mapearEmpresaCompleta(empresa.get())
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao buscar empresa: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Map<String, Object>> buscarPorCnpj(@PathVariable String cnpj) {
        try {
            Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);
            
            if (empresa.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "empresa", mapearEmpresa(empresa.get())
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Empresa não encontrada para CNPJ: " + cnpj
                ));
            }
            
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao buscar por CNPJ: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/plano/{plano}")
    public ResponseEntity<Map<String, Object>> listarPorPlano(@PathVariable String plano) {
        try {
            TipoPlano tipoPlano = TipoPlano.valueOf(plano.toUpperCase());
            List<Empresa> empresas = empresaService.listarPorPlano(tipoPlano);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "plano", plano,
                "total", empresas.size(),
                "empresas", empresas.stream().map(this::mapearEmpresa).toList()
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Plano inválido: " + plano
            ));
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao listar por plano: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarEmpresa(
            @PathVariable Long id, 
            @RequestBody Empresa dadosAtualizacao) {
        
        try {
            Empresa atualizada = empresaService.atualizarEmpresa(id, dadosAtualizacao);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empresa atualizada com sucesso",
                "empresa", mapearEmpresa(atualizada)
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao atualizar empresa: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PostMapping("/{id}/ativar")
    public ResponseEntity<Map<String, Object>> ativarEmpresa(@PathVariable Long id) {
        try {
            empresaService.ativarEmpresa(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empresa ativada com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao ativar empresa: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PostMapping("/{id}/desativar")
    public ResponseEntity<Map<String, Object>> desativarEmpresa(@PathVariable Long id) {
        try {
            empresaService.desativarEmpresa(id);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empresa desativada com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao desativar empresa: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    // Feature Flags
    @PostMapping("/{id}/features/{feature}/habilitar")
    public ResponseEntity<Map<String, Object>> habilitarFeature(
            @PathVariable Long id, 
            @PathVariable String feature) {
        
        try {
            empresaService.habilitarFeature(id, feature);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Feature '" + feature + "' habilitada com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao habilitar feature: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @PostMapping("/{id}/features/{feature}/desabilitar")
    public ResponseEntity<Map<String, Object>> desabilitarFeature(
            @PathVariable Long id, 
            @PathVariable String feature) {
        
        try {
            empresaService.desabilitarFeature(id, feature);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Feature '" + feature + "' desabilitada com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao desabilitar feature: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/{id}/features/{feature}")
    public ResponseEntity<Map<String, Object>> verificarFeature(
            @PathVariable Long id, 
            @PathVariable String feature) {
        
        try {
            boolean ativa = empresaService.isFeatureAtiva(id, feature);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "feature", feature,
                "ativa", ativa
            ));
            
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao verificar feature: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    // Configurações
    @PostMapping("/{id}/configuracoes")
    public ResponseEntity<Map<String, Object>> adicionarConfiguracao(
            @PathVariable Long id,
            @RequestBody Map<String, String> configuracao) {
        
        try {
            String chave = configuracao.get("chave");
            String valor = configuracao.get("valor");
            
            if (chave == null || valor == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Chave e valor são obrigatórios"
                ));
            }
            
            empresaService.adicionarConfiguracao(id, chave, valor);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Configuração adicionada com sucesso"
            ));
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao adicionar configuração: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        try {
            long totalAtivas = empresaService.contarEmpresasAtivas();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "estatisticas", Map.of(
                    "totalEmpresasAtivas", totalAtivas,
                    "planosDisponiveis", TipoPlano.values(),
                    "gruposManutencao", List.of("A", "B", "C")
                )
            ));
            
        } catch (Exception e) {
            logger.error("[EMPRESA_API] ❌ Erro ao obter estatísticas: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Erro interno do servidor"
            ));
        }
    }
    
    // Métodos auxiliares
    private Map<String, Object> mapearEmpresa(Empresa empresa) {
        return Map.of(
            "id", empresa.getId(),
            "razaoSocial", empresa.getRazaoSocial(),
            "nomeFantasia", empresa.getNomeFantasia() != null ? empresa.getNomeFantasia() : "",
            "cnpj", empresa.getCnpj() != null ? empresa.getCnpj() : "",
            "cidade", empresa.getCidade() != null ? empresa.getCidade() : "",
            "estado", empresa.getEstado() != null ? empresa.getEstado() : "",
            "tipoPlano", empresa.getTipoPlano().name(),
            "grupoManutenção", empresa.getGrupoManutenção(),
            "ativa", empresa.getAtiva(),
            "dataCriacao", empresa.getDataCriacao()
        );
    }
    
    private Map<String, Object> mapearEmpresaCompleta(Empresa empresa) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", empresa.getId());
        map.put("razaoSocial", empresa.getRazaoSocial());
        map.put("nomeFantasia", empresa.getNomeFantasia() != null ? empresa.getNomeFantasia() : "");
        map.put("cnpj", empresa.getCnpj() != null ? empresa.getCnpj() : "");
        map.put("endereco", empresa.getEndereco() != null ? empresa.getEndereco() : "");
        map.put("cidade", empresa.getCidade() != null ? empresa.getCidade() : "");
        map.put("estado", empresa.getEstado() != null ? empresa.getEstado() : "");
        map.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        map.put("email", empresa.getEmail() != null ? empresa.getEmail() : "");
        map.put("tipoPlano", empresa.getTipoPlano().name());
        map.put("grupoManutenção", empresa.getGrupoManutenção());
        map.put("versaoSistema", empresa.getVersaoSistema());
        map.put("ativa", empresa.getAtiva());
        map.put("featureFlags", empresa.getFeatureFlags());
        map.put("configuracoes", empresa.getConfiguracoes());
        map.put("dataCriacao", empresa.getDataCriacao());
        map.put("dataAtualizacao", empresa.getDataAtualizacao());
        return map;
    }
}