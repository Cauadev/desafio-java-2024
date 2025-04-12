package com.hering.desafiojava.api;

import com.hering.desafiojava.api.config.WebMvcConfig;
import com.hering.desafiojava.api.controller.TextToSpeechController;
import com.hering.desafiojava.core.services.TextToSpeechService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.hering.desafiojava")
@EntityScan(basePackages = "com.hering.desafiojava.core.entities")
@EnableJpaRepositories(basePackages = "com.hering.desafiojava.core.repositories")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
