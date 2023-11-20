package ru.otus.exportsrv.kafka.consumer;

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

    private final ImportTaskService importTaskService;

    @KafkaListener(
            topics = "${topic.import.task.update-status}",
            groupId = "${topic.import.task.update-status-group-id}",
            containerFactory = "importTaskUpdateKafkaListenerContainerFactory"
    )
    public void consume(ImportTaskUpdateDto importTaskUpdateDto) {
        importTaskService.updateTask(importTaskUpdateDto);
    }
}
