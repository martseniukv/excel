package ru.otus.exportsrv.model.mapper.item.export.excel.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.domain.item.export.ItemPriceExport;
import ru.otus.exportsrv.model.mapper.item.export.excel.ItemPriceValueExcelMapper;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static ru.otus.exportsrv.utils.ExcelUtils.getDefaultCellStyle;
import static ru.otus.exportsrv.utils.ExcelUtils.setObjectValue;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemPriceValueExcelMapperImpl implements ItemPriceValueExcelMapper {

    private static final Field[] DECLARED_FIELDS = ItemPriceExport.class.getDeclaredFields();

    @Override
    public int map(int rowNum, boolean withHeader, Sheet sheet, List<ItemPriceExport> prices) {

        if (isNull(sheet) || isEmpty(prices)) {
            return rowNum;
        }
        if (withHeader) {
            Row headerRow = sheet.createRow(rowNum++);
            CellStyle defaultCellStyle = getDefaultCellStyle(sheet.getWorkbook());
            for (int i = 0; i < DECLARED_FIELDS.length; i++) {
                var cell = headerRow.createCell(i);
                cell.setCellStyle(defaultCellStyle);
                setObjectValue(cell, DECLARED_FIELDS[i].getName());
            }
        }
        for (var price : emptyIfNull(prices)) {

            var row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(price.getItemCode());
            row.createCell(1).setCellValue(price.getPriceListCode());
            row.createCell(2).setCellValue(nonNull(price.getValue()) ? price.getValue().toString() : "");
            row.createCell(3).setCellValue(nonNull(price.getStartTime()) ? price.getStartTime().toString() : "");
        }
        return rowNum;
    }
}
