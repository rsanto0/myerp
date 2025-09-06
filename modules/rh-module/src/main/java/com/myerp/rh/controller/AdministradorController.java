package com.myerp.rh.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.myerp.common.model.Usuario;
import com.myerp.rh.client.AuthServiceClient;
import com.myerp.rh.model.RegistroPonto;
import com.myerp.rh.repository.RegistroPontoRepository;
import com.myerp.rh.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdministradorController {

    private static final Logger logger = LoggerFactory.getLogger(AdministradorController.class);
    private final UsuarioRepository usuarioRepo;
    private final RegistroPontoRepository pontoRepo;
    private final AuthServiceClient authServiceClient;

    public AdministradorController(UsuarioRepository usuarioRepo,
                                 RegistroPontoRepository pontoRepo,
                                 AuthServiceClient authServiceClient) {
        this.usuarioRepo = usuarioRepo;
        this.pontoRepo = pontoRepo;
        this.authServiceClient = authServiceClient;
    }
    
    /**
     * Valida se o usuário tem role de ADMIN
     * @param userRole role do usuário vinda do header X-User-Role
     * @throws ResponseStatusException se não for ADMIN
     */
    private void validarAdmin(String userRole) {
        if (!"ADMIN".equals(userRole)) {
            logger.warn("[ADMIN_ACCESS_DENIED] Tentativa de acesso negada - Role: {}", userRole);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: apenas administradores");
        }
        logger.debug("[ADMIN_ACCESS_GRANTED] Acesso autorizado - Role: {}", userRole);
    }



    @PostMapping("/funcionarios")
    public Usuario criarFuncionario(@Valid @RequestBody Usuario funcionario,
                                       @RequestHeader("X-User-Role") String userRole) {
        validarAdmin(userRole);
        logger.info("[ADMIN_CRIAR_FUNCIONARIO] Iniciando criação de funcionário: {} - Role: {}", 
                   funcionario.getNome(), funcionario.getRole());
        
        try {
            logger.debug("[ADMIN_CRIAR_FUNCIONARIO] Validando dados do funcionário - CPF: {}, Login: {}", 
                        funcionario.getCpf(), funcionario.getLogin());
            
            // 1. Salvar funcionário no RH Module
            logger.debug("[ADMIN_CRIAR_FUNCIONARIO] Salvando funcionário no banco de dados RH");
            Usuario salvo = usuarioRepo.save(funcionario);
            
            // 2. Sincronizar com Auth Service
            logger.debug("[ADMIN_CRIAR_FUNCIONARIO] Sincronizando com Auth Service");
            try {
                AuthServiceClient.CreateUserRequest userRequest = new AuthServiceClient.CreateUserRequest(
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getLogin(),
                    funcionario.getSenha(),
                    funcionario.getRole().toString()
                );
                
                AuthServiceClient.UserResponse userResponse = authServiceClient.criarUsuario(userRequest);
                logger.info("[ADMIN_CRIAR_FUNCIONARIO] Usuário sincronizado no Auth Service - ID: {}", userResponse.getId());
                
            } catch (Exception authError) {
                logger.warn("[ADMIN_CRIAR_FUNCIONARIO] Erro ao sincronizar com Auth Service: {}", authError.getMessage());
                logger.warn("[ADMIN_CRIAR_FUNCIONARIO] Funcionário criado no RH, mas login pode não funcionar");
            }
            
            logger.info("[ADMIN_CRIAR_FUNCIONARIO] Funcionário criado com sucesso - ID: {}, Nome: {}, Role: {}", 
                       salvo.getId(), salvo.getNome(), salvo.getRole());
            return salvo;
            
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            logger.error("[ADMIN_CRIAR_FUNCIONARIO] Erro de integridade ao criar funcionário {}: {}", 
                        funcionario.getNome(), e.getMessage());
            
            if (e.getMessage().contains("login") && e.getMessage().contains("already exists")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Login '" + funcionario.getLogin() + "' já existe. Escolha outro login.");
            }
            if (e.getMessage().contains("cpf") && e.getMessage().contains("already exists")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "CPF '" + funcionario.getCpf() + "' já está cadastrado.");
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dados duplicados: " + e.getRootCause().getMessage());
            
        } catch (Exception e) {
            logger.error("[ADMIN_CRIAR_FUNCIONARIO] Erro ao criar funcionário {}: {}", 
                        funcionario.getNome(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @DeleteMapping("/funcionarios/{id}")
    public ResponseEntity<Void> removerFuncionario(@PathVariable Long id,
                                                  @RequestHeader("X-User-Role") String userRole) {
        validarAdmin(userRole);
        logger.info("[ADMIN_REMOVER_FUNCIONARIO] Iniciando remoção do funcionário ID: {}", id);
        
        try {
            logger.debug("[ADMIN_REMOVER_FUNCIONARIO] Verificando existência do funcionário ID: {}", id);
            
            if (usuarioRepo.existsById(id)) {
                logger.debug("[ADMIN_REMOVER_FUNCIONARIO] Funcionário encontrado, executando remoção");
                usuarioRepo.deleteById(id);
                
                logger.info("[ADMIN_REMOVER_FUNCIONARIO] Funcionário ID {} removido com sucesso", id);
                return ResponseEntity.ok().build();
            }
            
            logger.warn("[ADMIN_REMOVER_FUNCIONARIO] Funcionário ID {} não encontrado", id);
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            logger.error("[ADMIN_REMOVER_FUNCIONARIO] Erro ao remover funcionário ID {}: {}", 
                        id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/funcionarios")
    public List<Usuario> listarFuncionarios(@RequestHeader("X-User-Role") String userRole) {
        validarAdmin(userRole);
        logger.info("[ADMIN_LISTAR_FUNCIONARIOS] Iniciando listagem de funcionários pelo admin");
        
        try {
            logger.debug("[ADMIN_LISTAR_FUNCIONARIOS] Consultando banco de dados");
            List<Usuario> funcionarios = usuarioRepo.findAll();
            
            logger.info("[ADMIN_LISTAR_FUNCIONARIOS] Listagem concluída - {} funcionários encontrados", 
                       funcionarios.size());
            return funcionarios;
            
        } catch (Exception e) {
            logger.error("[ADMIN_LISTAR_FUNCIONARIOS] Erro ao listar funcionários: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/pontos")
    public List<RegistroPonto> listarTodosPontos(@RequestHeader("X-User-Role") String userRole) {
        validarAdmin(userRole);
        logger.info("[ADMIN_LISTAR_PONTOS] Iniciando listagem de todos os pontos pelo admin");
        
        try {
            logger.debug("[ADMIN_LISTAR_PONTOS] Consultando banco de dados");
            List<RegistroPonto> pontos = pontoRepo.findAll();
            
            logger.info("[ADMIN_LISTAR_PONTOS] Listagem concluída - {} registros de ponto encontrados", 
                       pontos.size());
            return pontos;
            
        } catch (Exception e) {
            logger.error("[ADMIN_LISTAR_PONTOS] Erro ao listar pontos: {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/pontos/{id}")
    public ResponseEntity<Void> removerPonto(@PathVariable Long id,
                                            @RequestHeader("X-User-Role") String userRole) {
        validarAdmin(userRole);
        logger.info("[ADMIN_REMOVER_PONTO] Iniciando remoção do ponto ID: {}", id);
        
        try {
            logger.debug("[ADMIN_REMOVER_PONTO] Verificando existência do ponto ID: {}", id);
            
            if (pontoRepo.existsById(id)) {
                logger.debug("[ADMIN_REMOVER_PONTO] Ponto encontrado, executando remoção");
                pontoRepo.deleteById(id);
                
                logger.info("[ADMIN_REMOVER_PONTO] Ponto ID {} removido com sucesso", id);
                return ResponseEntity.ok().build();
            }
            
            logger.warn("[ADMIN_REMOVER_PONTO] Ponto ID {} não encontrado", id);
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            logger.error("[ADMIN_REMOVER_PONTO] Erro ao remover ponto ID {}: {}", 
                        id, e.getMessage(), e);
            throw e;
        }
    }
}
