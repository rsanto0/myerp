package com.myerp.gateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private WebClient webClient;
    
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;
    
    @Mock
    private GatewayFilterChain filterChain;

    private JwtAuthFilter jwtAuthFilter;
    private JwtAuthFilter.Config config;

    @Mock
	private RequestBodySpec requestHeadersSpec;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter(webClient);
        config = new JwtAuthFilter.Config();
    }

    @Test
    void testSemToken_DeveRetornar401() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = jwtAuthFilter.apply(config);

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, filterChain))
                .expectComplete()
                .verify();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verifyNoInteractions(webClient);
    }

    @Test
    void testTokenSemBearer_DeveRetornar401() {
        // Arrange
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "InvalidToken")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = jwtAuthFilter.apply(config);

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, filterChain))
                .expectComplete()
                .verify();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verifyNoInteractions(webClient);
    }

    @Test
    void testTokenValido_DeveContinuar() {
        // Arrange
        String validResponse = "{\"userId\":1,\"sub\":\"admin\",\"role\":\"ADMIN\"}";
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
		when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(validResponse));
        when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = jwtAuthFilter.apply(config);

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, filterChain))
                .expectComplete()
                .verify();

        verify(webClient).post();
        verify(filterChain).filter(any(ServerWebExchange.class));
    }

    @Test
    void testTokenInvalido_DeveRetornar401() {
        // Arrange
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException("Token inválido")));

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = jwtAuthFilter.apply(config);

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, filterChain))
                .expectComplete()
                .verify();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verifyNoInteractions(filterChain);
    }

    @Test
    void testInjecaoHeaders_DeveAdicionarXUserHeaders() {
        // Arrange
        String validResponse = "{\"userId\":123,\"sub\":\"testuser\",\"role\":\"USER\"}";
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(validResponse));
        
        when(filterChain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange modifiedExchange = invocation.getArgument(0);
            
            // Verificar headers injetados
            assertEquals("123", modifiedExchange.getRequest().getHeaders().getFirst("X-User-Id"));
            assertEquals("testuser", modifiedExchange.getRequest().getHeaders().getFirst("X-User-Login"));
            assertEquals("USER", modifiedExchange.getRequest().getHeaders().getFirst("X-User-Role"));
            
            return Mono.empty();
        });

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = jwtAuthFilter.apply(config);

        // Act & Assert
        StepVerifier.create(filter.filter(exchange, filterChain))
                .expectComplete()
                .verify();

        verify(filterChain).filter(any(ServerWebExchange.class));
    }
}
