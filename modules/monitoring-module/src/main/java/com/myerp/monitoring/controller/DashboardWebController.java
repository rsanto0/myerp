package com.myerp.monitoring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {
    
    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboardAlias() {
        return "dashboard";
    }
}