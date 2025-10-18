package com.myerp.auth.controller;

import com.myerp.auth.dto.AuthResponse;
import com.myerp.auth.dto.LoginRequest;
import com.myerp.common.model.Usuario;
import com.myerp.auth.repository.UsuarioRepository;
import com.myerp.common.enums.Role;
import com.myerp.common.service.PasswordService;
import com.myerp.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordService passwordService;
    
    /**
     * Construtor com injeção de dependências
     */
    public AuthController(UsuarioRepository usuarioRepository, JwtService jwtService, PasswordService passwordService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordService = passwordService;
    }
    
    /**
     * Debug endpoint para verificar payload bruto
     */
    @PostMapping("/login-debug")
    public ResponseEntity<String> loginDebug(@RequestBody(required = false) String rawBody) {
        logger.info("[LOGIN-DEBUG] 📦 Raw body recebido: {}", rawBody);
        return ResponseEntity.ok("Payload recebido: " + rawBody);
    }
    
    /**
     * Autentica usuário e gera token JWT
     * @param request dados de login (login/senha)
     * @return token JWT + dados do usuário ou 401 se inválido
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("[LOGIN] 🔑 Tentativa de autenticação - Login: {}, Senha: {}", 
                   request.getLogin(), request.getSenha() != null ? "[PRESENTE]" : "[NULL]");
        logger.debug("[LOGIN] 📦 Payload completo: login='{}', senha='{}'", 
                    request.getLogin(), request.getSenha());
        
        Usuario user = usuarioRepository.findByLogin(request.getLogin());
        if (user == null) {
            logger.warn("[LOGIN] ❌ Usuário não encontrado - Login: {}", request.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!passwordService.matches(request.getSenha(), user.getSenha())) {
            logger.warn("[LOGIN] ❌ Credenciais inválidas - Login: {}", request.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        logger.info("[LOGIN] 🔍 Gerando token JWT para usuário: {}", user.getLogin());
        String token = jwtService.generateToken(user.getLogin(), user.getRole().name(), user.getId());
        
        logger.info("[LOGIN] ✅ Autenticação bem-sucedida - Login: {}, Role: {}, UserId: {}", 
                   user.getLogin(), user.getRole().name(), user.getId());
        
        return ResponseEntity.ok(new AuthResponse(token, user.getLogin(), user.getRole().name(), user.getId()));
    }
    
    /**
     * Valida token JWT e retorna claims
     * @param authHeader header Authorization com Bearer token
     * @return claims do token ou 401 se inválido
     */
    @PostMapping("/validate")
    public ResponseEntity<Claims> validate(@RequestHeader("Authorization") String authHeader) {
        logger.info("[VALIDATE] 🔍 Validando token JWT recebido do Gateway");
        
        try {
            String token = authHeader.replace("Bearer ", "");
            logger.debug("[VALIDATE] 🔑 Processando token: {}...", token.substring(0, Math.min(20, token.length())));
            
            Claims claims = jwtService.validateToken(token);
            
            String subject = claims.getSubject();
            String role = (String) claims.get("role");
            Integer userId = (Integer) claims.get("userId");
            
            logger.info("[VALIDATE] ✅ Token válido - Subject: {}, Role: {}, UserId: {}", subject, role, userId);
            
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            logger.warn("[VALIDATE] ❌ Token inválido - Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    /**
     * Cria novo usuário no sistema
     * @param createUserRequest dados do usuário
     * @return usuário criado
     */
    @PostMapping("/users")
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody CreateUserRequest createUserRequest) {
        logger.info("[CREATE_USER] 👤 Criando usuário - Login: {}, Role: {}", 
                   createUserRequest.getLogin(), createUserRequest.getRole());
        
        try {
            // Verificar se usuário já existe
            if (usuarioRepository.findByLogin(createUserRequest.getLogin()) != null) {
                logger.warn("[CREATE_USER] ❌ Usuário já existe - Login: {}", createUserRequest.getLogin());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            
            // Criar novo usuário
            Usuario user = new Usuario();
            user.setNome(createUserRequest.getNome());
            user.setCpf(createUserRequest.getCpf());
            user.setLogin(createUserRequest.getLogin());
            user.setSenha(passwordService.encode(createUserRequest.getSenha()));
            user.setRole(Role.valueOf(createUserRequest.getRole()));
            
            Usuario savedUser = usuarioRepository.save(user);
            
            logger.info("[CREATE_USER] ✅ Usuário criado com sucesso - ID: {}, Login: {}, Role: {}", 
                       savedUser.getId(), savedUser.getLogin(), savedUser.getRole());
            
            return ResponseEntity.ok(savedUser);
            
        } catch (Exception e) {
            logger.error("[CREATE_USER] ❌ Erro ao criar usuário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // DTO para criação de usuário
    public static class CreateUserRequest {
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        private String nome;
        
        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 11, message = "CPF deve ter 11 caracteres")
        private String cpf;
        
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres")
        private String login;
        
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 4, message = "Senha deve ter pelo menos 4 caracteres")
        private String senha;
        
        @NotBlank(message = "Role é obrigatória")
        private String role;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}