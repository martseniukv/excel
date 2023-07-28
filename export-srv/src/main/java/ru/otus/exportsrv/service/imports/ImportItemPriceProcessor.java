package ru.otus.exportsrv.service.imports;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.request.item.ImportItemPriceDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

import java.util.List;
import java.util.Map;

public interface ImportItemPriceProcessor {

    Map<String, List<ImportItemPriceDto>> process(Sheet sheet, SheetDetailDto importSettings);
}
