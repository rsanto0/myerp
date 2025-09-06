package com.myerp.rh.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myerp.rh.dto.RegistrarPontoRequest;
import com.myerp.rh.model.RegistroPonto;
import com.myerp.rh.model.TipoPonto;
import com.myerp.rh.repository.RegistroPontoRepository;
import com.myerp.rh.repository.UsuarioRepository;

@RestController
@RequestMapping("/pontos")
public class RegistroPontoController {

    private static final Logger logger = LoggerFactory.getLogger(RegistroPontoController.class);
    private final RegistroPontoRepository registroRepo;
    private final UsuarioRepository funcionarioRepo;

    public RegistroPontoController(RegistroPontoRepository registroRepo, UsuarioRepository funcionarioRepo) {
        this.registroRepo = registroRepo;
        this.funcionarioRepo = funcionarioRepo;
    }

    @PostMapping("/{funcionarioId}/registrar")
    public ResponseEntity<RegistroPonto> registrarPonto(@PathVariable Long funcionarioId, @RequestBody RegistrarPontoRequest request) {
        TipoPonto tipo = request.getTipo();
        logger.info("[REGISTRAR_PONTO] Iniciando registro de ponto - Funcionário ID: {}, Tipo: {}", 
                   funcionarioId, tipo);
        
        try {
            logger.debug("[REGISTRAR_PONTO] Buscando funcionário ID: {}", funcionarioId);
            
            return funcionarioRepo.findById(funcionarioId)
                .map(funcionario -> {
                    logger.debug("[REGISTRAR_PONTO] Funcionário encontrado: {} - Criando registro", 
                                funcionario.getNome());
                    
                    RegistroPonto ponto = new RegistroPonto();
                    ponto.setFuncionario(funcionario);
                    ponto.setTipo(tipo);
                    LocalDateTime agora = LocalDateTime.now();
                    ponto.setDataHora(agora);
                    
                    logger.debug("[REGISTRAR_PONTO] Salvando registro no banco - Data/Hora: {}", agora);
                    RegistroPonto salvo = registroRepo.save(ponto);
                    
                    logger.info("[REGISTRAR_PONTO] Ponto registrado com sucesso - ID: {}, Funcionário: {}, Tipo: {}", 
                               salvo.getId(), funcionario.getNome(), tipo);
                    return ResponseEntity.ok(salvo);
                })
                .orElseGet(() -> {
                    logger.warn("[REGISTRAR_PONTO] Funcionário ID {} não encontrado", funcionarioId);
                    return ResponseEntity.notFound().build();
                });
                
        } catch (Exception e) {
            logger.error("[REGISTRAR_PONTO] Erro ao registrar ponto - Funcionário ID: {}, Tipo: {}, Erro: {}", 
                        funcionarioId, tipo, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/registrar-automatico")
    public ResponseEntity<RegistroPonto> registrarPontoAutomatico(
            @RequestParam Long funcionarioId,
            @RequestParam String dataHora,
            @RequestParam String tipoMovimento) {
        
        logger.info("[REGISTRAR_PONTO_AUTOMATICO] Registro automático via biometria - Funcionário ID: {}, Tipo: {}", 
                   funcionarioId, tipoMovimento);
        
        try {
            TipoPonto tipo = TipoPonto.valueOf(tipoMovimento);
            
            return funcionarioRepo.findById(funcionarioId)
                .map(funcionario -> {
                    RegistroPonto ponto = new RegistroPonto();
                    ponto.setFuncionario(funcionario);
                    ponto.setTipo(tipo);
                    ponto.setDataHora(LocalDateTime.now()); // Usa horário atual
                    
                    RegistroPonto salvo = registroRepo.save(ponto);
                    
                    logger.info("[REGISTRAR_PONTO_AUTOMATICO] Ponto registrado automaticamente - ID: {}, Funcionário: {}", 
                               salvo.getId(), funcionario.getNome());
                    return ResponseEntity.ok(salvo);
                })
                .orElseGet(() -> {
                    logger.warn("[REGISTRAR_PONTO_AUTOMATICO] Funcionário ID {} não encontrado", funcionarioId);
                    return ResponseEntity.notFound().build();
                });
                
        } catch (Exception e) {
            logger.error("[REGISTRAR_PONTO_AUTOMATICO] Erro: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{funcionarioId}")
    public ResponseEntity<List<RegistroPonto>> listarPontos(@PathVariable Long funcionarioId) {
        logger.info("[LISTAR_PONTOS] Iniciando listagem de pontos do funcionário ID: {}", funcionarioId);
        
        try {
            logger.debug("[LISTAR_PONTOS] Buscando funcionário ID: {}", funcionarioId);
            
            return funcionarioRepo.findById(funcionarioId)
                .map(funcionario -> {
                    logger.debug("[LISTAR_PONTOS] Funcionário encontrado: {} - Consultando registros", 
                                funcionario.getNome());
                    
                    List<RegistroPonto> pontos = registroRepo.findByFuncionario(funcionario);
                    
                    logger.info("[LISTAR_PONTOS] Listagem concluída - {} pontos encontrados para {}", 
                               pontos.size(), funcionario.getNome());
                    return ResponseEntity.ok(pontos);
                })
                .orElseGet(() -> {
                    logger.warn("[LISTAR_PONTOS] Funcionário ID {} não encontrado", funcionarioId);
                    return ResponseEntity.notFound().build();
                });
                
        } catch (Exception e) {
            logger.error("[LISTAR_PONTOS] Erro ao listar pontos do funcionário ID {}: {}", 
                        funcionarioId, e.getMessage(), e);
            throw e;
        }
    }
}
