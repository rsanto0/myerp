package com.myerp.biometria.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Token de sistema para chamadas internas (usar token admin)
            String systemToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsInVzZXJJZCI6MSwiaWF0IjoxNzI1Mzk5NjAwLCJleHAiOjE3MjU0ODYwMDB9.placeholder";
            requestTemplate.header("Authorization", "Bearer " + systemToken);
        };
    }
}