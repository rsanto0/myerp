package com.myerp.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        logger.info("=== GATEWAY REQUEST ===");
        logger.info("Method: {}", request.getMethod());
        logger.info("URI: {}", request.getURI());
        logger.info("Path: {}", request.getPath());
        logger.info("Headers: {}", request.getHeaders());
        logger.info("Remote Address: {}", request.getRemoteAddress());
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("=== GATEWAY RESPONSE ===");
            logger.info("Status: {}", exchange.getResponse().getStatusCode());
            logger.info("Response Headers: {}", exchange.getResponse().getHeaders());
        }));
    }
    
    @Override
    public int getOrder() {
        return -1;
    }
}