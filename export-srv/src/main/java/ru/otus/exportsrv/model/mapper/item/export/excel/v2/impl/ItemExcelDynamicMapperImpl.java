package ru.otus.exportsrv.model.mapper.item.export.excel.v2.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.domain.item.export.ItemExport;
import ru.otus.exportsrv.model.enums.ExportObject;
import ru.otus.exportsrv.model.mapper.item.export.excel.v2.ItemExcelDynamicMapper;
import ru.otus.exportsrv.model.request.ExportObjectSettings;
import ru.otus.exportsrv.model.request.ExportSheetSettings;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static ru.otus.exportsrv.utils.ExcelUtils.getDefaultCellStyle;
import static ru.otus.exportsrv.utils.ExcelUtils.setObjectValue;
import static ru.otus.exportsrv.utils.ReflectionUtils.getFieldValue;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemExcelDynamicMapperImpl implements ItemExcelDynamicMapper {

    private final Map<String, Field> declaredFieldMap = Arrays.stream(ItemExport.class.getDeclaredFields())
            .collect(toMap(Field::getName, Function.identity()));

    @Override
    public void map(Workbook workbook, List<ItemExport> items, List<ExportSheetSettings> sheetSettings) {

        if (isNull(workbook) || isEmpty(items)) {
            return;
        }

        var sheetSetting = sheetSettings.stream()
                .filter(f -> ExportObject.ITEM.getName().equals(f.getObjectName()))
                .findFirst().orElse(null);

        if (isNull(sheetSetting)) {
            return;
        }
        int rowNum = 0;

        Sheet sheet = workbook.createSheet(sheetSetting.getObjectName());
        sheet.setDefaultColumnWidth(20);
        Row headerRow = sheet.createRow(rowNum++);
        var defaultCellStyle = getDefaultCellStyle(workbook);

        for (ExportObjectSettings settings : sheetSetting.getObjectSettings()) {
            int columnIndex = settings.getColumnIndex() - 1;
            var field = declaredFieldMap.get(settings.getFiledName());
            if (nonNull(field)) {
                var cell = headerRow.createCell(columnIndex);
                cell.setCellStyle(defaultCellStyle);
                setObjectValue(cell, field.getName());
            }
        }

        for (var item : items) {

            var row = sheet.createRow(rowNum++);

            for (ExportObjectSettings settings : sheetSetting.getObjectSettings()) {

                int columnIndex = settings.getColumnIndex() - 1;
                Field field = declaredFieldMap.get(settings.getFiledName());

                if (nonNull(field)) {
                    Object value = getFieldValue(item, field);
                    Cell cell = row.createCell(columnIndex);
                    setObjectValue(cell, value);
                }
            }
        }
    }
}