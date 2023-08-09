package ru.otus.exportsrv.model.response.task.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.enums.ImportObject;
import ru.otus.exportsrv.model.response.task.error.ImportTaskErrorDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SheetDetailDto {

    private Long id;
    private int lineFrom;
    private int lineTo;
    private String sheetName;
    private ImportObject importObject;
    private List<ImportTaskErrorDto> errors;
}
