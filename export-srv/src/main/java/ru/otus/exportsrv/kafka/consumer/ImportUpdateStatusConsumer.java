package ru.otus.exportsrv.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.request.task.ImportTaskUpdateDto;
import ru.otus.exportsrv.service.task.ImportTaskService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportUpdateStatusConsumer {

    private final ObjectMapper objectMapper;
    private final ImportTaskService importTaskService;

    @KafkaListener(topics = "${topic.import.task.update-status}",
            groupId = "${topic.import.task.update-status-group-id}")
    public void consume(String message) {

        try {
            var importTaskUpdateDto = objectMapper.readValue(message, ImportTaskUpdateDto.class);
            importTaskService.updateTask(importTaskUpdateDto);
        } catch (JsonProcessingException e) {
           log.error(e.getMessage(), e);
        }
    }
}
