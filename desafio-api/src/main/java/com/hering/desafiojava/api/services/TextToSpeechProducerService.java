package com.hering.desafiojava.api.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TextToSpeechProducerService {

    private final KafkaTemplate<String, Long> kafkaTemplate;


    public TextToSpeechProducerService(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendIdToQueue(Long id){
        kafkaTemplate.send("tss-processed", 0, null, id);
    }
}
