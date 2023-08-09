package ru.otus.exportsrv.service.task;

import ru.otus.exportsrv.model.request.task.ImportTaskAddDto;
import ru.otus.exportsrv.model.request.task.ImportTaskUpdateDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;

public interface ImportTaskService {

    ImportTaskDto saveTask(ImportTaskAddDto dto);

    ImportTaskDto getById(long id);
    ImportTaskDto updateTask(ImportTaskUpdateDto dto);
}
