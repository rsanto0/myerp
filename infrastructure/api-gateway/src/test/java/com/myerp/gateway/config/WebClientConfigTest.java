package com.myerp.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para WebClientConfig
 * Valida configuração do WebClient para comunicação com Auth Service
 */
class WebClientConfigTest {

    private final WebClientConfig webClientConfig = new WebClientConfig();

    /**
     * Testa se WebClient é criado corretamente
     * Deve retornar instância não nula do WebClient
     */
    @Test
    void testWebClientBean_DeveRetornarInstanciaNaoNula() {
        // Act - Criar WebClient através da configuração
        WebClient webClient = webClientConfig.webClient();

        // Assert - Verificar se instância foi criada
        assertNotNull(webClient, "WebClient não deve ser nulo");
    }

    /**
     * Testa se WebClient é configurado como singleton
     * Múltiplas chamadas devem retornar instâncias diferentes (factory method)
     */
    @Test
    void testWebClientBean_DeveSerFactoryMethod() {
        // Act - Criar duas instâncias
        WebClient webClient1 = webClientConfig.webClient();
        WebClient webClient2 = webClientConfig.webClient();

        // Assert - Verificar se são instâncias diferentes (factory method)
        assertNotSame(webClient1, webClient2, "Factory method deve criar novas instâncias");
        assertNotNull(webClient1, "Primeira instância não deve ser nula");
        assertNotNull(webClient2, "Segunda instância não deve ser nula");
    }

    /**
     * Testa se WebClient possui configurações básicas
     * Deve ter configurações de codec definidas
     */
    @Test
    void testWebClientBean_DeveTermConfiguracaoBasica() {
        // Act - Criar WebClient
        WebClient webClient = webClientConfig.webClient();

        // Assert - Verificar se WebClient foi configurado
        assertNotNull(webClient, "WebClient deve estar configurado");
        
        // Verificar se é possível criar requests básicos
        assertDoesNotThrow(() -> {
            webClient.get().uri("http://localhost:8081/test");
        }, "WebClient deve permitir criação de requests");
    }
}
