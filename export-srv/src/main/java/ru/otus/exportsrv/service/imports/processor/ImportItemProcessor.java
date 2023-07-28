package ru.otus.exportsrv.service.imports.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.model.request.item.ItemImportDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.isNull;
import static ru.otus.exportsrv.utils.ImportExcelColumnFactory.getStringColumn;

@Slf4j
@Component
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportItemProcessor implements ImportProcessor {

    public List<ItemImportDto> process(Sheet sheet, SheetDetailDto importSettings) {

        if (isNull(sheet) || isNull(importSettings)) {
            return new ArrayList<>();
        }
        log.info("Start process items");
        List<ItemImportDto> itemImports = new ArrayList<>();

        boolean continueLoop = true;
        int lineFrom = importSettings.getLineFrom() - 1;
        int lineTo = importSettings.getLineTo() - 1;

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext() && continueLoop) {

            Row row = rowIterator.next();
            if (isNull(row) || row.getLastCellNum() <= 0 || row.getRowNum() < lineFrom) {
                continue;
            }
            if (row.getRowNum() >= lineTo) {
                continueLoop = false;
            }
            var itemImportDto = ItemImportDto.builder()
                    .code(getStringColumn(row, 0))
                    .name(getStringColumn(row, 1))
                    .hierarchyCode(getStringColumn(row, 2))
                    .build();
            itemImports.add(itemImportDto);
        }
//        for (int i = lineFrom; i <= lineTo; i++) {
//
//            currentRow = i + 1;
//            Row row = sheet.getRow(currentRow);
//            if (isNull(row) || row.getLastCellNum() <= 0) {
//                continue;
//            }
//
//            var itemImportDto = ItemImportDto.builder()
//                    .code(getStringColumn(row, 0))
//                    .name(getStringColumn(row, 1))
//                    .hierarchyCode(getStringColumn(row, 2))
//                    .build();
//            itemImports.add(itemImportDto);
//        }
        log.info("End process items");
        return itemImports;
    }
}
