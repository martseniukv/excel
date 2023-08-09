package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;


    @Value(value = "${kafka.import.item.group-id}")
    private String importItemGroupId;

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        // get configs on application.properties/yml
        Map<String, Object> properties = kafkaProperties.buildProducerProperties();
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "50000000");//100 * 1024 * 1024); // 100 MB
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip"); // Use GZIP compression
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "50000000");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "50000000");
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic importTaskUpdateStatus(@Value("${kafka.import.task.update-status}") String topicName) {
        Map<String, String> configs = new HashMap<>();
        configs.put("max.message.bytes", "50000000");
        return TopicBuilder
                .name(topicName)
                .configs(configs)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "50000000");
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "50000000");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "50000000");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "50000000");
        return new DefaultKafkaConsumerFactory<>(properties);
    }
}
