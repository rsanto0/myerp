package com.myerp.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan(basePackages = {"com.myerp.device", "com.myerp.common"})
public class DeviceManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DeviceManagementApplication.class, args);
    }
}