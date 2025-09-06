package com.myerp.auth.config;

import com.myerp.common.model.Usuario;
import com.myerp.common.enums.Role;
import com.myerp.common.service.PasswordService;
import com.myerp.auth.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;
    
    public DataInitializer(UsuarioRepository usuarioRepository, PasswordService passwordService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordService = passwordService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("[INIT] Inicializando dados padrão do sistema...");
        
        // Verificar se master já existe
        Usuario existingMaster = usuarioRepository.findByLogin("master");
        if (existingMaster == null) {
            logger.info("[INIT] Criando usuário MASTER padrão...");
            
            Usuario master = new Usuario();
            master.setLogin("master");
            master.setSenha(passwordService.encode("master123"));
            master.setNome("Super Administrador");
            master.setCpf("99999999999");
            master.setRole(Role.MASTER);
            
            Usuario savedMaster = usuarioRepository.save(master);
            logger.info("[INIT] ✅ MASTER criado - ID: {}, Login: {}", 
                       savedMaster.getId(), savedMaster.getLogin());
        } else {
            logger.info("[INIT] ℹ️ MASTER já existe - ID: {}, Login: {}", 
                       existingMaster.getId(), existingMaster.getLogin());
        }
        
        // Verificar se admin já existe
        Usuario existingAdmin = usuarioRepository.findByLogin("admin");
        if (existingAdmin == null) {
            logger.info("[INIT] Criando usuário administrador padrão...");
            
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordService.encode("admin123"));
            admin.setNome("Administrador do Sistema");
            admin.setCpf("00000000000");
            admin.setRole(Role.ADMIN);
            
            Usuario savedAdmin = usuarioRepository.save(admin);
            logger.info("[INIT] ✅ Administrador criado - ID: {}, Login: {}", 
                       savedAdmin.getId(), savedAdmin.getLogin());
        } else {
            logger.info("[INIT] ℹ️ Administrador já existe - ID: {}, Login: {}", 
                       existingAdmin.getId(), existingAdmin.getLogin());
        }
        
        // Criar usuário funcionário de exemplo
        Usuario existingFunc = usuarioRepository.findByLogin("funcionario");
        if (existingFunc == null) {
            logger.info("[INIT] Criando usuário funcionário de exemplo...");
            
            Usuario funcionario = new Usuario();
            funcionario.setLogin("funcionario");
            funcionario.setSenha(passwordService.encode("123456"));
            funcionario.setNome("Funcionário Exemplo");
            funcionario.setCpf("11111111111");
            funcionario.setRole(Role.FUNCIONARIO);
            
            Usuario savedFunc = usuarioRepository.save(funcionario);
            logger.info("[INIT] ✅ Funcionário criado - ID: {}, Login: {}", 
                       savedFunc.getId(), savedFunc.getLogin());
        } else {
            logger.info("[INIT] ℹ️ Funcionário exemplo já existe - ID: {}, Login: {}", 
                       existingFunc.getId(), existingFunc.getLogin());
        }
        
        logger.info("[INIT] 🎯 Dados iniciais configurados com sucesso!");
        logger.info("[INIT] 🔥 MASTER: login=master, senha=master123");
        logger.info("[INIT] 👑 Admin: login=admin, senha=admin123");
        logger.info("[INIT] 👤 Funcionário: login=funcionario, senha=123456");
    }
}