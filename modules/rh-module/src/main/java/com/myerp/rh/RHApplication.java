package com.myerp.rh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EntityScan(basePackages = {"com.myerp.rh.model", "com.myerp.common.model"})
public class RHApplication {
    public static void main(String[] args) {
        SpringApplication.run(RHApplication.class, args);
    }
}
