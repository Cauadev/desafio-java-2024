package com.hering.desafiojava.api;

import com.hering.desafiojava.api.config.WebMvcConfig;
import com.hering.desafiojava.api.controller.TextToSpeechController;
import com.hering.desafiojava.core.services.TextToSpeechService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {WebMvcConfig.class, TextToSpeechService.class, TextToSpeechController.class})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
