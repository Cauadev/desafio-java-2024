package com.hering.desafiojava.api.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${KAFKA_BOOTSTRAP_SERVERS}")
    private String bootstrapAddress;

    @Value(value = "${KAFKA_USERNAME}")
    private String KAFKA_USERNAME;

    @Value(value = "${KAFKA_PASSWORD}")
    private String KAFKA_PASSWORD;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put("sasl.mechanism", "PLAIN");
        configs.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='"+KAFKA_USERNAME+"' password='"+KAFKA_PASSWORD+"';");
        configs.put("security.protocol", "SASL_SSL");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicTssProcessed() {
        return new NewTopic("tts-processed", 1, (short) 3);
    }
}
