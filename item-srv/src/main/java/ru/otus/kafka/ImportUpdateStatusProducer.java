package ru.otus.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ImportTaskUpdateDto updateDto)  {

        if (isNull(updateDto)) {
            log.debug("ImportUpdateStatusProducer updateDto isNull");
            return;
        }
        String itemMsg = null;
        try {
            itemMsg = objectMapper.writeValueAsString(updateDto);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        log.info("import update status produced {}", itemMsg);
        kafkaTemplate.send(updateStatusTopic, itemMsg);

    }
}
