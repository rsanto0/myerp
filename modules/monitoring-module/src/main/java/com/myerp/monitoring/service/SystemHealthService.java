package com.myerp.monitoring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SystemHealthService {
    
    private final RestTemplate restTemplate;
    
    public SystemHealthService() {
        this.restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(3))
            .setReadTimeout(Duration.ofSeconds(5))
            .build();
    }
    
    @Value("${services.eureka.host}:${services.eureka.port}")
    private String eurekaHost;
    
    @Value("${services.api-gateway.host}:${services.api-gateway.port}")
    private String gatewayHost;
    
    @Value("${services.auth-service.host}:${services.auth-service.port}")
    private String authHost;
    
    @Value("${services.rh-module.host}:${services.rh-module.port}")
    private String rhHost;
    
    @Value("${services.biometria-module.host}:${services.biometria-module.port}")
    private String biometriaHost;
    
    public Map<String, Object> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // System info
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("uptime", runtime.getUptime() / 1000 + " seconds");
        health.put("memoryUsed", memory.getHeapMemoryUsage().getUsed() / 1024 / 1024 + " MB");
        health.put("memoryMax", memory.getHeapMemoryUsage().getMax() / 1024 / 1024 + " MB");
        
        return health;
    }
    
    public Map<String, Object> getServicesStatus() {
        Map<String, Object> services = new HashMap<>();
        
        // Check each service with fallback endpoints
        services.put("eureka", checkServiceWithFallback("http://" + eurekaHost, "/actuator/health", "/"));
        services.put("api-gateway", checkServiceWithFallback("http://" + gatewayHost, "/actuator/health", "/"));
        services.put("auth-service", checkServiceWithFallback("http://" + authHost, "/actuator/health", "/auth/health"));
        services.put("rh-module", checkServiceWithFallback("http://" + rhHost, "/actuator/health", "/api/health"));
        services.put("biometria-module", checkServiceWithFallback("http://" + biometriaHost, "/actuator/health", "/api/biometria/health"));
        
        return services;
    }
    
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        
        metrics.put("jvm", Map.of(
            "uptime", runtime.getUptime(),
            "heapUsed", memory.getHeapMemoryUsage().getUsed(),
            "heapMax", memory.getHeapMemoryUsage().getMax(),
            "nonHeapUsed", memory.getNonHeapMemoryUsage().getUsed()
        ));
        
        metrics.put("system", Map.of(
            "processors", Runtime.getRuntime().availableProcessors(),
            "freeMemory", Runtime.getRuntime().freeMemory(),
            "totalMemory", Runtime.getRuntime().totalMemory(),
            "maxMemory", Runtime.getRuntime().maxMemory()
        ));
        
        return metrics;
    }
    
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("health", getSystemHealth());
        dashboard.put("services", getServicesStatus());
        dashboard.put("metrics", getSystemMetrics());
        dashboard.put("summary", getSummary());
        
        return dashboard;
    }
    
    private Map<String, Object> checkService(String healthUrl) {
        try {
            Map<String, Object> response = restTemplate.getForObject(healthUrl, Map.class);
            return Map.of(
                "status", response != null ? response.getOrDefault("status", "DOWN") : "DOWN",
                "url", healthUrl,
                "lastCheck", LocalDateTime.now()
            );
        } catch (Exception e) {
            return Map.of(
                "status", "DOWN",
                "url", healthUrl,
                "error", e.getMessage(),
                "lastCheck", LocalDateTime.now()
            );
        }
    }
    
    private Map<String, Object> checkServiceWithFallback(String baseUrl, String primaryEndpoint, String fallbackEndpoint) {
        // Primeiro tenta o endpoint principal (actuator/health)
        try {
            String primaryUrl = baseUrl + primaryEndpoint;
            Map<String, Object> response = restTemplate.getForObject(primaryUrl, Map.class);
            if (response != null && "UP".equals(response.get("status"))) {
                return Map.of(
                    "status", "UP",
                    "url", primaryUrl,
                    "endpoint", "primary",
                    "lastCheck", LocalDateTime.now()
                );
            }
        } catch (Exception e) {
            // Ignora erro e tenta fallback
        }
        
        // Tenta endpoint de fallback
        try {
            String fallbackUrl = baseUrl + fallbackEndpoint;
            restTemplate.getForObject(fallbackUrl, String.class);
            return Map.of(
                "status", "UP",
                "url", fallbackUrl,
                "endpoint", "fallback",
                "lastCheck", LocalDateTime.now()
            );
        } catch (Exception e) {
            // Se ambos falharam, tenta apenas conectividade básica
            return checkBasicConnectivity(baseUrl);
        }
    }
    
    private Map<String, Object> checkBasicConnectivity(String baseUrl) {
        try {
            // Timeout mais curto para conectividade básica
            restTemplate.getForObject(baseUrl, String.class);
            return Map.of(
                "status", "UP",
                "url", baseUrl,
                "endpoint", "basic",
                "lastCheck", LocalDateTime.now()
            );
        } catch (Exception e) {
            return Map.of(
                "status", "DOWN",
                "url", baseUrl,
                "error", e.getMessage(),
                "lastCheck", LocalDateTime.now()
            );
        }
    }
    
    private Map<String, Object> getSummary() {
        Map<String, Object> services = getServicesStatus();
        long upServices = services.values().stream()
            .mapToLong(service -> {
                Map<String, Object> serviceMap = (Map<String, Object>) service;
                return "UP".equals(serviceMap.get("status")) ? 1 : 0;
            })
            .sum();
        
        return Map.of(
            "totalServices", services.size(),
            "upServices", upServices,
            "downServices", services.size() - upServices,
            "healthPercentage", (upServices * 100.0) / services.size()
        );
    }
}