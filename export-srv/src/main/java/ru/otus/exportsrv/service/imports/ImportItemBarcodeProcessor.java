package ru.otus.exportsrv.service.imports;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.ImportItemBarcodeProcessData;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

public interface ImportItemBarcodeProcessor {

    ImportItemBarcodeProcessData process(Sheet sheet, SheetDetailDto importSettings);
}
