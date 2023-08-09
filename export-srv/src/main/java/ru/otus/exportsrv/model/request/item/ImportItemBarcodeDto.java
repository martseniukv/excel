package ru.otus.exportsrv.model.request.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.ImportExcelColumn;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemBarcodeDto {

    private ImportExcelColumn<String> barcode;
    private ImportExcelColumn<String> description;
    private ImportExcelColumn<Boolean> isDefault;
}
