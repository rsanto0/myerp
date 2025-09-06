package com.myerp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Servidor de Configuração Centralizada do sistema MyERP.
 * 
 * <p><b>PROCESSO DE NEGÓCIO:</b> Esta classe é o <b>centro de configurações</b> do sistema MyERP,
 * fornecendo configurações centralizadas para todos os 8 módulos do sistema.
 * Trabalha em conjunto com <b>TODOS os módulos</b> para fornecer configurações dinâmicas.
 * 
 * <p><b>INTEGRAÇÕES:</b>
 * <ul>
 *   <li><b>Service Discovery (8761)</b>: Registra-se como config-server</li>
 *   <li><b>API Gateway (8080)</b>: Fornece configurações de roteamento</li>
 *   <li><b>Auth Service (8081)</b>: Configurações de JWT e segurança</li>
 *   <li><b>RH Module (8082)</b>: Configurações de ponto e funcionários</li>
 *   <li><b>Biometria Module (8083)</b>: Configurações de câmeras e biometria</li>
 *   <li><b>Monitoring Module (8084)</b>: Configurações de monitoramento</li>
 *   <li><b>Company Module (8085)</b>: Configurações multi-tenant</li>
 *   <li><b>Financial Module (8086)</b>: Configurações bancárias e financeiras</li>
 * </ul>
 * 
 * <p><b>FUNCIONALIDADES:</b>
 * <ul>
 *   <li><b>Configurações por ambiente</b>: local, docker, aws</li>
 *   <li><b>Configurações por módulo</b>: Específicas para cada serviço</li>
 *   <li><b>Refresh dinâmico</b>: Atualização sem restart</li>
 *   <li><b>Versionamento</b>: Controle de versões das configurações</li>
 * </ul>
 * 
 * <p><b>FLUXO DE USO:</b>
 * <ol>
 *   <li>Config Server inicia na porta 8888</li>
 *   <li>Módulos consultam configurações via bootstrap.yml</li>
 *   <li>Configurações são servidas baseadas no perfil ativo</li>
 *   <li>Módulos podem fazer refresh via /actuator/refresh</li>
 * </ol>
 * 
 * @author MyERP Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}