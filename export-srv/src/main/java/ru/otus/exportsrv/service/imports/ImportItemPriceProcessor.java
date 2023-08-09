package ru.otus.exportsrv.service.imports;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.ImportItemPriceProcessData;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

public interface ImportItemPriceProcessor {

    ImportItemPriceProcessData process(Sheet sheet, SheetDetailDto importSettings);
}
