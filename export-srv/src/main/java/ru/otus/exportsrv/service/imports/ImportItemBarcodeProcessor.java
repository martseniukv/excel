package ru.otus.exportsrv.service.imports;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.request.item.ImportItemBarcodeDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

import java.util.List;
import java.util.Map;

public interface ImportItemBarcodeProcessor {

    Map<String, List<ImportItemBarcodeDto>> process(Sheet sheet, SheetDetailDto importSettings);
}
