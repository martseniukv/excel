package ru.otus.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.otus.model.request.imports.ItemImportData;
import ru.otus.model.request.imports.ItemImportDto;

import java.util.HashMap;
import java.util.List;
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

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, ItemImportData>
//    itemImportKafkaListenerContainerFactory(ObjectMapper objectMapper) {
//        var factory = new ConcurrentKafkaListenerContainerFactory<String, ItemImportData>();
//        JsonDeserializer<ItemImportData> itemImportDataJsonDeserializer = new JsonDeserializer<>(ItemImportData.class, objectMapper, false);
//        factory.setConsumerFactory(itemImportDataJsonDeserializer);
//        return factory;
//    }

    public ConsumerFactory<String, List<ItemImportDto>> itemImportConsumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, importItemGroupId);
        config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 100 * 1024 * 1024);// 100 MB
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        var javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ItemImportDto.class);
        return new DefaultKafkaConsumerFactory<>(config,
                new StringDeserializer(),
                new JsonDeserializer<>(javaType, objectMapper, false));
    }
}
