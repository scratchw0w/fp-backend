package com.fstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.fstore.properties")
public class FstoreBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FstoreBackendApplication.class, args);
    }
}
