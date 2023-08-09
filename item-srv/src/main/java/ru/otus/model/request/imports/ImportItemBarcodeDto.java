package ru.otus.model.request.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemBarcodeDto {

    private ImportExcelColumn<String> barcode;
    private ImportExcelColumn<String> description;
    private ImportExcelColumn<Boolean> isDefault;
}
