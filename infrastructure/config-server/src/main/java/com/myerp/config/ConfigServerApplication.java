package com.myerp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    
    public static void main(String[] args) {
        System.out.println("🏢 [CONFIG] Iniciando Config Server...");
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("✅ [CONFIG] Config Server iniciado com sucesso!");
    }
}