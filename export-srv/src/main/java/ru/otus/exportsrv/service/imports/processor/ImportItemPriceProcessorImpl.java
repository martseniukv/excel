package ru.otus.exportsrv.service.imports.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.model.ExcelColumnWrapper;
import ru.otus.exportsrv.model.ImportItemPriceProcessData;
import ru.otus.exportsrv.model.request.item.ImportItemPriceDto;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportItemPriceProcessor;
import ru.otus.exportsrv.utils.ExcelColumnProcessor;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Component
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportItemPriceProcessorImpl implements ImportItemPriceProcessor {

    private final ExcelColumnProcessor excelColumnProcessorImpl;

    @Override
    public ImportItemPriceProcessData process(Sheet sheet, SheetDetailDto importSettings) {

        var processData = new ImportItemPriceProcessData();

        if (isNull(sheet) || isNull(importSettings)) {
            return processData;
        }

        log.info("Start process item prices");
        Map<String, List<ImportItemPriceDto>> itemPrices = new HashMap<>();

        boolean continueLoop = true;
        Long sheetDetailId = importSettings.getId();
        int lineFrom = importSettings.getLineFrom() - 1;
        int lineTo = importSettings.getLineTo() - 1;

        List<ImportTaskErrorAddDto> errors = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext() && continueLoop) {

            Row row = rowIterator.next();
            if (isNull(row) || row.getLastCellNum() <= 0 || row.getRowNum() < lineFrom) {
                continue;
            }
            if (row.getRowNum() >= lineTo) {
                continueLoop = false;
            }
            var itemCodeWrapper = excelColumnProcessorImpl.getStringValue(row, 0, sheetDetailId, true);
            var priceListCodeWrapper = excelColumnProcessorImpl.getStringValue(row, 1, sheetDetailId, true);
            var priceWrapper = excelColumnProcessorImpl.getBigDecimalValue(row, 2, sheetDetailId, true);
            var dateWrapper = excelColumnProcessorImpl.getInstantValue(row, 3, sheetDetailId, true);

            addErrors(errors, itemCodeWrapper);
            addErrors(errors, priceListCodeWrapper);
            addErrors(errors, priceWrapper);
            addErrors(errors, dateWrapper);

            String itemCode = itemCodeWrapper.getColumn().getValue();
            if (nonNull(itemCode) && errors.isEmpty()) {
                var priceDto = ImportItemPriceDto.builder()
                        .priceListCode(priceListCodeWrapper.getColumn())
                        .price(priceWrapper.getColumn())
                        .date(dateWrapper.getColumn())
                        .build();
                itemPrices.putIfAbsent(itemCode, new ArrayList<>());
                itemPrices.get(itemCode).add(priceDto);
            }
        }

        processData.setItemPriceMap(itemPrices);
        processData.setErrors(errors);
        log.info("End process item prices");
        return processData;
    }

    private static void addErrors(List<ImportTaskErrorAddDto> errors, ExcelColumnWrapper<?> wrapper) {
       if (isNotEmpty(wrapper.getErrors())) {
           errors.addAll(wrapper.getErrors());
       }
    }
}
