package org.example.daas.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.daas.graphql")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
