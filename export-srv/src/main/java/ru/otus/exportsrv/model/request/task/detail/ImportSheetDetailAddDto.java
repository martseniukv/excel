package ru.otus.exportsrv.model.request.task.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.enums.ImportObject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportSheetDetailAddDto {

    private String sheetName;
    private ImportObject importObject;
    private int lineFrom;
    private int lineTo;
}
