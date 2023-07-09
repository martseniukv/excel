package ru.otus.exportsrv.model.mapper.item.export.excel;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.domain.item.export.ItemExport;

import java.util.List;

public interface ItemExcelMapper {

    void map(Sheet sheet, List<ItemExport> items);
}
