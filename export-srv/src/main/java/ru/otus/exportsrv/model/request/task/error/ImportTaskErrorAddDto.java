package ru.otus.exportsrv.model.request.task.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskErrorAddDto {

    private int row;
    private int column;
    private Long sheetDetailId;
    private String message;
}
