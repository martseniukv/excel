package ru.otus.exportsrv.model.response.task.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskErrorDto {

    private Long id;
    private Long importTaskId;
    private int rowNum;
    private int columnNum;
    private String message;
}
