package ru.otus.exportsrv.service.imports;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.request.item.ItemImportDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

import java.util.List;

public interface ImportProcessor {

    List<ItemImportDto> process(Sheet sheet, SheetDetailDto importSettings);
}
