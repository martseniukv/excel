package ru.otus.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.model.request.imports.ImportTaskUpdateDto;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportUpdateStatusProducer {

    @Value("${kafka.import.task.update-status}")
    private String updateStatusTopic;

    @Qualifier("importUpdateStatusKafkaTemplate")
    private final KafkaTemplate<String, ImportTaskUpdateDto> kafkaTemplate;

    public void sendMessage(ImportTaskUpdateDto updateDto)  {

        if (isNull(updateDto)) {
            log.debug("ImportUpdateStatusProducer updateDto isNull");
            return;
        }
        log.info("import update status produced {}", updateDto);
        try {
            kafkaTemplate.send(updateStatusTopic, updateDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
