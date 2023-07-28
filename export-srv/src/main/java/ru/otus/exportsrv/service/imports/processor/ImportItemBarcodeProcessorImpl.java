package ru.otus.exportsrv.service.imports.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.model.request.item.ImportItemBarcodeDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportItemBarcodeProcessor;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.otus.exportsrv.utils.ImportExcelColumnFactory.getBooleanImportExcelColumn;
import static ru.otus.exportsrv.utils.ImportExcelColumnFactory.getStringColumn;

@Slf4j
@Component
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportItemBarcodeProcessorImpl implements ImportItemBarcodeProcessor {

    @Override
    public Map<String, List<ImportItemBarcodeDto>> process(Sheet sheet, SheetDetailDto importSettings) {

        if (isNull(sheet) || isNull(importSettings)) {
            return new HashMap<>();
        }
        log.info("Start process item barcodes");
        Map<String, List<ImportItemBarcodeDto>> itemBarcodes = new HashMap<>();

        boolean continueLoop = true;
        int lineFrom = importSettings.getLineFrom();
        int lineTo = importSettings.getLineTo();

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext() && continueLoop) {

            Row row = rowIterator.next();
            if (isNull(row) || row.getLastCellNum() <= 0 || row.getRowNum() < lineFrom) {
                continue;
            }
            if (row.getRowNum() >= lineTo) {
                continueLoop = false;
            }
            Cell cell = row.getCell(0);

            if (nonNull(cell)) {
                String itemCode = cell.getStringCellValue();

                if (nonNull(itemCode)) {
                    var barcodeDto = ImportItemBarcodeDto.builder()
                            .barcode(getStringColumn(row, 1))
                            .description(getStringColumn(row, 2))
                            .isDefault(getBooleanImportExcelColumn(row, 3))
                            .build();
                    itemBarcodes.putIfAbsent(itemCode, new ArrayList<>());
                    itemBarcodes.get(itemCode).add(barcodeDto);
                }
            }
        }
        log.info("End process item barcodes");
        return itemBarcodes;
    }
}
