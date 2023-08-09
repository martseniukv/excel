package ru.otus.exportsrv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelColumnWrapper<T> {

    private ImportExcelColumn<T> column;
    private List<ImportTaskErrorAddDto> errors;
}
