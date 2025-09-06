package com.myerp.monitoring.controller;

import com.myerp.common.dto.BaseResponse;
import com.myerp.monitoring.service.SystemHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
@CrossOrigin(origins = "*")
public class MonitoringController {
    
    @Autowired
    private SystemHealthService healthService;
    
    @GetMapping("/health")
    public BaseResponse<Map<String, Object>> getSystemHealth() {
        Map<String, Object> health = healthService.getSystemHealth();
        return BaseResponse.success(health);
    }
    
    @GetMapping("/services")
    public BaseResponse<Map<String, Object>> getServicesStatus() {
        Map<String, Object> services = healthService.getServicesStatus();
        return BaseResponse.success(services);
    }
    
    @GetMapping("/metrics")
    public BaseResponse<Map<String, Object>> getSystemMetrics() {
        Map<String, Object> metrics = healthService.getSystemMetrics();
        return BaseResponse.success(metrics);
    }
    
    @GetMapping("/dashboard")
    public BaseResponse<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = healthService.getDashboardData();
        return BaseResponse.success(dashboard);
    }
}