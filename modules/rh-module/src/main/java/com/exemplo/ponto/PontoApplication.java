package com.exemplo.ponto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PontoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PontoApplication.class, args);
    }
}
