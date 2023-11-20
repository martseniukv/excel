package ru.otus.kafka.imports;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.model.request.imports.ItemImportData;
import ru.otus.service.imports.ItemImportService;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafKaConsumerService {

    private final ItemImportService itemImportServiceImpl;

    @KafkaListener(
            topics = "${kafka.import.item.topic}",
            groupId = "${kafka.import.item.group-id}",
            containerFactory = "itemImportKafkaListenerContainerFactory"
    )
    public void consume(ItemImportData itemImportData) {

        if (isNull(itemImportData)) {
            log.info("Import item data  is null");
            return;
        }
        itemImportServiceImpl.importItems(itemImportData);
    }
}
