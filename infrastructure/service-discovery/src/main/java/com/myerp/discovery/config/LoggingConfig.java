package com.myerp.discovery.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class LoggingConfig {
    
    @Bean
    public FilterRegistrationBean<EurekaLoggingFilter> loggingFilter() {
        FilterRegistrationBean<EurekaLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EurekaLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
    
    public static class EurekaLoggingFilter extends OncePerRequestFilter {
        private static final Logger logger = LoggerFactory.getLogger(EurekaLoggingFilter.class);
        
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                FilterChain filterChain) throws ServletException, IOException {
            
            logger.info("=== EUREKA REQUEST ===");
            logger.info("Method: {}", request.getMethod());
            logger.info("URI: {}", request.getRequestURI());
            logger.info("Remote Address: {}", request.getRemoteAddr());
            
            filterChain.doFilter(request, response);
            
            logger.info("=== EUREKA RESPONSE ===");
            logger.info("Status: {}", response.getStatus());
        }
    }
}