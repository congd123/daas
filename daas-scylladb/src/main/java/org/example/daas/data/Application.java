package org.example.daas.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.daas.data")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
