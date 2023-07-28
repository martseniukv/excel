package ru.otus.model.request.imports;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportExcelColumn<T> {

    private T value;
    private int row;
    private int column;
}
