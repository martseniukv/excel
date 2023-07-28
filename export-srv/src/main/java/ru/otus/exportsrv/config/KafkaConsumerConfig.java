package ru.otus.exportsrv.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.otus.exportsrv.model.request.task.ImportTaskUpdateDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {



//
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

//    @Value(value = "${kafka.import.item.group-id}")
//    private String importItemGroupId;
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, ImportTaskUpdateDto>
//    importUpdateTask(ObjectMapper objectMapper) {
//        var factory = new ConcurrentKafkaListenerContainerFactory<String, ImportTaskUpdateDto>();
//        factory.setConsumerFactory(itemImportConsumerFactory(objectMapper));
//        return factory;
//    }
//
    @Bean
    public ConsumerFactory<String, String> itemImportConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "50000000");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "50000000");
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
