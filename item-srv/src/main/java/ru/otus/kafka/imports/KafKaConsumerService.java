package ru.otus.kafka.imports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.model.request.imports.ItemImportData;
import ru.otus.model.request.imports.ItemImportDto;
import ru.otus.service.imports.ItemImportService;

import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafKaConsumerService {

    private final ItemImportService itemImportServiceImpl;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.import.item.topic}",
            groupId = "${kafka.import.item.group-id}")
    public void consume(String message) {

        if (isNull(message)) {
            log.info("Import item data  is null");
            return;
        }
        try {
            ItemImportData itemImportData = objectMapper.readValue(message, ItemImportData.class);

            itemImportServiceImpl.importItems(itemImportData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
