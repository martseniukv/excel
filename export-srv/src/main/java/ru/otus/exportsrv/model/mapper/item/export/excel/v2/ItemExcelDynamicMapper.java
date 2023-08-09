package ru.otus.exportsrv.model.mapper.item.export.excel.v2;

import org.apache.poi.ss.usermodel.Workbook;
import ru.otus.exportsrv.model.domain.item.export.ItemExport;
import ru.otus.exportsrv.model.request.ExportSheetSettings;

import java.util.List;

public interface ItemExcelDynamicMapper {

    void map(Workbook workbook, List<ItemExport> items, List<ExportSheetSettings> sheetSettings);
}