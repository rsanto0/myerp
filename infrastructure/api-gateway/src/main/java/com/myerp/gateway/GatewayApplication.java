package com.myerp.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * API Gateway - Ponto de entrada único para microserviços
 * 
 * RESPONSABILIDADES:
 * • Roteamento: Direciona requisições para microserviços corretos
 * • Proxy de Autenticação: Delega validação JWT para Auth Service
 * • Headers: Injeta informações de usuário (X-User-Id, X-User-Login, X-User-Role)
 * • Centralização: Unifica acesso a múltiplos serviços em uma única porta
 * • Proxy Inteligente: Aplica filtros baseado em rotas
 * 
 * FLUXO:
 * Cliente → Gateway (8080) → Auth Service (8081) | Sistema Ponto (8082)
 */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
public class GatewayApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);
    
    /**
     * Método principal que inicializa o API Gateway
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        logger.info("[GATEWAY] Iniciando API Gateway...");
        SpringApplication.run(GatewayApplication.class, args);
    }
    
    /**
     * Evento executado quando aplicação está pronta
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("[GATEWAY] ✅ API Gateway iniciado com sucesso!");
        logger.info("[GATEWAY] 🚀 Servidor rodando na porta 8080");
        logger.info("[GATEWAY] 🔗 Rotas configuradas:");
        logger.info("[GATEWAY]   • /auth/** → Auth Service (8081) [PÚBLICO]");
        logger.info("[GATEWAY]   • /api/** → Sistema Ponto (8082) [PROTEGIDO]");
        logger.info("[GATEWAY] 🔐 Filtro JWT ativo para rotas protegidas");
    }
}
