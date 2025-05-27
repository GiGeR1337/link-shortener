package com.example.tpo10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Locale;

@SpringBootApplication
public class Tpo10Application {
    public static void main(String[] args) {
//        Locale.setDefault(Locale.of("pl"));
        SpringApplication.run(Tpo10Application.class, args);
    }

}
