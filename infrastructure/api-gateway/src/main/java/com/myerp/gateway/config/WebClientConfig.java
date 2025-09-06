package com.myerp.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuração do WebClient para comunicação com Auth Service
 */
@Configuration
public class WebClientConfig {
    
    /**
     * WebClient para chamadas HTTP ao Auth Service
     * @return WebClient configurado
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }
}
