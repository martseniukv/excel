package ru.otus.exportsrv.model.mapper.item.export.excel;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.domain.item.export.ItemPriceExport;

import java.util.List;

public interface ItemPriceValueExcelMapper {

    int map(int rowNum, boolean withHeader, Sheet sheet, List<ItemPriceExport> prices);
}
