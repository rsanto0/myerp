package com.myerp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EntityScan(basePackages = "com.myerp.common.model")
@ComponentScan(basePackages = {"com.myerp.auth", "com.myerp.common.service"})
public class AuthServiceApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);
    
    public static void main(String[] args) {
        logger.info("[AUTH] Iniciando Auth Service...");
        SpringApplication.run(AuthServiceApplication.class, args);
    }
    
    @Autowired
    private Environment env;
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String[] activeProfiles = env.getActiveProfiles();
        String profileInfo = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";
        
        logger.info("[AUTH] ✅ Auth Service iniciado com sucesso!");
        logger.info("[AUTH] 🚀 Servidor rodando na porta 8081");
        logger.info("[AUTH] 📋 Profile ativo: {}", profileInfo);
        logger.info("[AUTH] 🔐 Endpoints disponíveis:");
        logger.info("[AUTH]   • POST /auth/login - Autenticação de usuários");
        logger.info("[AUTH]   • POST /auth/validate - Validação de tokens JWT");
        logger.info("[AUTH]   • POST /auth/users - Criação de usuários");
        String dbUrl = env.getProperty("spring.datasource.url", "N/A");
        String dbType = dbUrl.contains("postgresql") ? "PostgreSQL" : 
                       dbUrl.contains("h2") ? "H2" : "Desconhecido";
        logger.info("[AUTH] 🗄️ Banco {} configurado: {}", dbType, dbUrl);
        logger.info("[AUTH] 🔑 JWT configurado com expiração de 1 hora");
    }
}