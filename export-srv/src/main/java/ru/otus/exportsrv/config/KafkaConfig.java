package ru.otus.exportsrv.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Value("${topic.import.item}")
    private String importItemTopicName;

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        // get configs on application.properties/yml
        Map<String, Object> properties = kafkaProperties.buildProducerProperties();
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "50000000"); // 100 MB
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip"); // Use GZIP compression
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic importItemTopic() {
        Map<String, String> configs = new HashMap<>();
        configs.put("max.message.bytes", "50000000");
        return TopicBuilder
                .name(importItemTopicName)
                .configs(configs)
                .partitions(1)
                .replicas(1)
                .build();
    }
}