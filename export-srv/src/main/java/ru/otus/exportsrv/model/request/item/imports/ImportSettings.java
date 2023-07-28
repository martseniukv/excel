package ru.otus.exportsrv.model.request.item.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportSettings {

    private String sheetName;
    private long importObjectType;
    private int rowFrom;
    private int rowTo;
}
