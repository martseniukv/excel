package ru.otus.exportsrv.model.response.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.enums.ImportType;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.model.response.task.error.ImportTaskErrorDto;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskDto {

    private Long id;
    private String fileName;
    private Instant startTime;

    private ImportType importType;
    private ImportStatus importStatus;
    private List<ImportTaskErrorDto> errors;
    private List<SheetDetailDto> sheetDetails;
}
