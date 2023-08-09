package ru.otus.exportsrv.model.request.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskUpdateDto {

    private long importTask;
    private boolean isFinished;
    private ImportStatus status;
    private List<ImportTaskErrorAddDto> errors;
}
