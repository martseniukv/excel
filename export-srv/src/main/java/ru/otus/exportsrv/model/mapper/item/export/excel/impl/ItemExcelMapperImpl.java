package ru.otus.exportsrv.model.mapper.item.export.excel.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import static ru.otus.exportsrv.utils.ExcelUtils.setObjectValue;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemExcelMapperImpl implements ItemExcelMapper {

    @Override
    public void map(Sheet sheet, List<ItemExport> items) {
        if (isNull(sheet) || isEmpty(items)) {
            return;
        }

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);

        Field[] declaredFields = ItemExport.class.getDeclaredFields();

        for (int i = 0; i < declaredFields.length; i++) {
            var cell = headerRow.createCell(i);
            setObjectValue(cell, declaredFields[i].getName());
        }

        for (var item : emptyIfNull(items)) {

            var row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(item.getCode());
            row.createCell(1).setCellValue(item.getHierarchyCode());
        }
    }
}
