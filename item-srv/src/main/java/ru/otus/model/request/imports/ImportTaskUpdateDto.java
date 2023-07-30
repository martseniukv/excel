package ru.otus.model.request.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.model.ImportErrorDto;
import ru.otus.model.enums.ImportStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskUpdateDto {

    private long importTask;
    private boolean isFinished;
    private ImportStatus status;
    private List<ImportErrorDto> errors;
}