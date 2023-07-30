package ru.otus.exportsrv.service.task.error;

import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;

import java.util.List;

public interface ImportErrorService {

    void saveAll(List<ImportTaskErrorAddDto> errors);

    void deleteByTestId(long importTask);
}
