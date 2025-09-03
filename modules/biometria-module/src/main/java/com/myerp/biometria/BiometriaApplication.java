package com.myerp.biometria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class
})
@EnableFeignClients
@EnableScheduling
public class BiometriaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiometriaApplication.class, args);
    }
}