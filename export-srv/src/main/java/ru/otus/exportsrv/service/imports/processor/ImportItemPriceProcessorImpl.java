package ru.otus.exportsrv.service.imports.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.model.request.item.ImportItemPriceDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportItemPriceProcessor;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.otus.exportsrv.utils.ImportExcelColumnFactory.*;

@Slf4j
@Component
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportItemPriceProcessorImpl implements ImportItemPriceProcessor {

    @Override
    public Map<String, List<ImportItemPriceDto>> process(Sheet sheet, SheetDetailDto importSettings) {

        if (isNull(sheet) || isNull(importSettings)) {
            return new HashMap<>();
        }

        log.info("Start process item prices");
        Map<String, List<ImportItemPriceDto>> itemPrices = new HashMap<>();

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
            Cell cell = row.getCell( 0);

            if (nonNull(cell)) {
                String itemCode = cell.getStringCellValue();

                if (nonNull(itemCode)) {
                    var priceDto = ImportItemPriceDto.builder()
                            .priceListCode(getStringColumn(row, 1))
                            .price(getBigDecimalColumn(row, 2))
                            .date(getInstantColumn(row, 3))
                            .build();
                    itemPrices.putIfAbsent(itemCode, new ArrayList<>());
                    itemPrices.get(itemCode).add(priceDto);
                }
            }
        }
        log.info("End process item prices");
        return itemPrices;
    }
}
