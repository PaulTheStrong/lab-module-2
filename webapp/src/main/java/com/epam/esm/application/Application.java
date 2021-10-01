package com.epam.esm.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
