package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.otus.model.request.imports.ImportTaskUpdateDto;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ImportUpdateStatusKafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, ImportTaskUpdateDto> importUpdateStatusProducerFactory() {

        Map<String, Object> configProps = kafkaProperties.buildProducerProperties();

        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ImportTaskUpdateDto> importUpdateStatusKafkaTemplate() {
        return new KafkaTemplate<>(importUpdateStatusProducerFactory());
    }
}