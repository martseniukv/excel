package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorDto {

    private int row;
    private int column;
    private long code;
    private String message;
    private Long sheetDetailId;
}
