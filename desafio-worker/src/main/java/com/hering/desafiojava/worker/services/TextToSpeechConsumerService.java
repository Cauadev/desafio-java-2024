package com.hering.desafiojava.worker.services;

import com.hering.desafiojava.core.services.TextToSpeechService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TextToSpeechConsumerService {

    private TextToSpeechService textToSpeechService;

    public TextToSpeechConsumerService(TextToSpeechService textToSpeechService) {
        this.textToSpeechService = textToSpeechService;
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "tss-processed", partitions = { "0" }), containerFactory = "tssKafkaListenerContainerFactory")
    public void tssListener(Long id) {
        textToSpeechService.requestVoiceRSSAndUpdate(id);
    }
}
