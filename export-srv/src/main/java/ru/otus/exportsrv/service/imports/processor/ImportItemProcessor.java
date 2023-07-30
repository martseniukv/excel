package ru.otus.exportsrv.service.imports.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.model.ExcelColumnWrapper;
import ru.otus.exportsrv.model.ImportItemProcessData;
import ru.otus.exportsrv.model.request.item.ItemImportDto;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportProcessor;
import ru.otus.exportsrv.utils.ExcelColumnProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Component
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportItemProcessor implements ImportProcessor {

    private final ExcelColumnProcessor excelColumnProcessorImpl;

    public ImportItemProcessData process(Sheet sheet, SheetDetailDto importSettings) {

        var processData = new ImportItemProcessData();
        if (isNull(sheet) || isNull(importSettings)) {
            return processData;
        }
        log.info("Start process items");
        List<ItemImportDto> itemImports = new ArrayList<>();
        List<ImportTaskErrorAddDto> errors = new ArrayList<>();

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
            var nameWrapper = excelColumnProcessorImpl.getStringValue(row, 1, sheetDetailId, false);
            var hierarchyCodeWrapper = excelColumnProcessorImpl.getStringValue(row, 2, sheetDetailId, true);

            addErrors(errors, itemCodeWrapper);
            addErrors(errors, nameWrapper);
            addErrors(errors, hierarchyCodeWrapper);

            if (errors.isEmpty()) {
                var itemImportDto = ItemImportDto.builder()
                        .code(itemCodeWrapper.getColumn())
                        .name(nameWrapper.getColumn())
                        .hierarchyCode(hierarchyCodeWrapper.getColumn())
                        .build();
                itemImports.add(itemImportDto);
            }
        }
        processData.setItems(itemImports);
        processData.setErrors(errors);

        log.info("End process items");
        return processData;
    }

    private static void addErrors(List<ImportTaskErrorAddDto> errors, ExcelColumnWrapper<?> wrapper) {
        if (isNotEmpty(wrapper.getErrors())) {
            errors.addAll(wrapper.getErrors());
        }
    }
}
