package ru.otus.exportsrv.model.request.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.ImportExcelColumn;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImportDto {

    private ImportExcelColumn<Long> id;
    private ImportExcelColumn<String> code;
    private ImportExcelColumn<String> name;
    private ImportExcelColumn<String> hierarchyCode;

    private List<ImportItemBarcodeDto> barcodes;
    private List<ImportItemPriceDto> prices;
}
