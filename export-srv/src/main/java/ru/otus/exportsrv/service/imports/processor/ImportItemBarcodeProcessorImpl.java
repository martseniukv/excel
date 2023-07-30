package ru.otus.exportsrv.service.imports.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.model.ExcelColumnWrapper;
import ru.otus.exportsrv.model.ImportItemBarcodeProcessData;
import ru.otus.exportsrv.model.request.item.ImportItemBarcodeDto;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportItemBarcodeProcessor;
import ru.otus.exportsrv.utils.ExcelColumnProcessor;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Component
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportItemBarcodeProcessorImpl implements ImportItemBarcodeProcessor {

    private final ExcelColumnProcessor excelColumnProcessorImpl;

    @Override
    public ImportItemBarcodeProcessData process(Sheet sheet, SheetDetailDto importSettings) {

        var processData = new ImportItemBarcodeProcessData();
        if (isNull(sheet) || isNull(importSettings)) {
            return processData;
        }
        log.info("Start process item barcodes");
        List<ImportTaskErrorAddDto> errors = new ArrayList<>();
        Map<String, List<ImportItemBarcodeDto>> itemBarcodes = new HashMap<>();

        boolean continueLoop = true;
        Long sheetDetailId = importSettings.getId();
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

            var itemCodeWrapper = excelColumnProcessorImpl.getStringValue(row, 0, sheetDetailId, true);
            var barcodeCodeWrapper = excelColumnProcessorImpl.getStringValue(row, 1, sheetDetailId, true);
            var descriptionWrapper = excelColumnProcessorImpl.getStringValue(row, 2, sheetDetailId, false);
            var isDefaultWrapper = excelColumnProcessorImpl.getBooleanValue(row, 3, sheetDetailId, true);

            addErrors(errors, itemCodeWrapper);
            addErrors(errors, barcodeCodeWrapper);
            addErrors(errors, descriptionWrapper);
            addErrors(errors, isDefaultWrapper);

            String itemCode = itemCodeWrapper.getColumn().getValue();
            if (nonNull(itemCode) && errors.isEmpty()) {
                var barcodeDto = ImportItemBarcodeDto.builder()
                        .barcode(barcodeCodeWrapper.getColumn())
                        .description(barcodeCodeWrapper.getColumn())
                        .isDefault(isDefaultWrapper.getColumn())
                        .build();

                itemBarcodes.putIfAbsent(itemCode, new ArrayList<>());
                itemBarcodes.get(itemCode).add(barcodeDto);
            }
        }

        processData.setItemBarcodes(itemBarcodes);
        processData.setErrors(errors);
        log.info("End process item barcodes");
        return processData;
    }

    private static void addErrors(List<ImportTaskErrorAddDto> errors, ExcelColumnWrapper<?> wrapper) {
        if (isNotEmpty(wrapper.getErrors())) {
            errors.addAll(wrapper.getErrors());
        }
    }
}
