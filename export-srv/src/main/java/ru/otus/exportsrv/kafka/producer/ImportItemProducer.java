package ru.otus.exportsrv.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.request.item.ItemImportData;
import ru.otus.exportsrv.model.request.item.ItemImportDto;

import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportItemProducer {

    @Value("${topic.import.item}")
    private String importItemTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ItemImportData itemImportData)  {

        if (isNull(itemImportData) || isEmpty(itemImportData.getItems())) {
            log.debug("ImportItemProducer items isEmpty");
            return;
        }
        try {
            String itemMsg = objectMapper.writeValueAsString(itemImportData);
//            log.info("import item produced {}", itemMsg);
            kafkaTemplate.send(importItemTopic, itemMsg);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
