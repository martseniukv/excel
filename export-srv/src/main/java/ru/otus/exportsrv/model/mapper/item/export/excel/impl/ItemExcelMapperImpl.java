package ru.otus.exportsrv.model.mapper.item.export.excel.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.domain.item.export.ItemExport;
import ru.otus.exportsrv.model.mapper.item.export.excel.ItemExcelMapper;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static ru.otus.exportsrv.utils.ExcelUtils.getDefaultCellStyle;
import static ru.otus.exportsrv.utils.ExcelUtils.setObjectValue;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemExcelMapperImpl implements ItemExcelMapper {

    private static final Field[] DECLARED_FIELDS = ItemExport.class.getDeclaredFields();

    @Override
    public int map(int rowNum, boolean withHeader, Sheet sheet, List<ItemExport> items) {
        if (isNull(sheet) || isEmpty(items)) {
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
        for (var item : emptyIfNull(items)) {

            var row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(item.getCode());
            row.createCell(1).setCellValue(item.getName());
            row.createCell(2).setCellValue(item.getHierarchyCode());
        }
        return rowNum;
    }
}
