package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.otus.model.request.imports.ItemImportData;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ItemImportKafkaConfig {

    @Value("${kafka.import.item.group-id}")
    private String groupId;

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, ItemImportData> itemImportFactory() {

        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new JsonDeserializer<>(ItemImportData.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ItemImportData> itemImportKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ItemImportData>();
        factory.setConsumerFactory(itemImportFactory());
        return factory;
    }
}