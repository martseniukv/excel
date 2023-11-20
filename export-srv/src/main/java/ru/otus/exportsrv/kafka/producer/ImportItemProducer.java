package ru.otus.exportsrv.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.request.item.ItemImportData;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportItemProducer {

    @Value("${topic.import.item}")
    private String importItemTopic;

    @Qualifier("itemImportKafkaTemplate")
    private final KafkaTemplate<String, ItemImportData> kafkaTemplate;

    public void sendMessage(ItemImportData itemImportData)  {

        if (isNull(itemImportData) || isEmpty(itemImportData.getItems())) {
            log.debug("ImportItemProducer items isEmpty");
            return;
        }
        try {
            kafkaTemplate.send(importItemTopic, itemImportData);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
