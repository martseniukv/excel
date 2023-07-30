package ru.otus.exportsrv.service.imports;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.ImportItemProcessData;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

public interface ImportProcessor {

    ImportItemProcessData process(Sheet sheet, SheetDetailDto importSettings);
}
