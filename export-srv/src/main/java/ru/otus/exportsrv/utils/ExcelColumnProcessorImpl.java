package ru.otus.exportsrv.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.ExcelColumnWrapper;
import ru.otus.exportsrv.model.ImportExcelColumn;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


@Slf4j
@Component
public class ExcelColumnProcessorImpl implements ExcelColumnProcessor {

    private static final String THIS_COLUMN_ARE_MANDATORY = "This column are mandatory";

    @Override
    public ExcelColumnWrapper<String> getStringValue(Row row, int column, Long sheetDetailId, boolean mandatory) {

        var importExcelColumn = new ImportExcelColumn<String>();
        int columnNum = column + 1;
        importExcelColumn.setColumn(columnNum);
        importExcelColumn.setSheetDetailId(sheetDetailId);

        var wrapper = new ExcelColumnWrapper<>(importExcelColumn, new ArrayList<>());

        if (isNull(row)) {
            return wrapper;
        }
        int rowNum = row.getRowNum()  + 1;
        try {
            var cell = row.getCell(column);
            if (nonNull(cell) && nonNull(cell.getStringCellValue())) {
                importExcelColumn.setValue(cell.getStringCellValue());
            } else if (mandatory) {
                wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId, THIS_COLUMN_ARE_MANDATORY));
            }
        } catch (Exception e) {
            wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId,"Invalid value type, expected String"));
        }
        importExcelColumn.setRow(rowNum + 1);
        return wrapper;
    }

    @Override
    public ExcelColumnWrapper<BigDecimal> getBigDecimalValue(Row row, int column, Long sheetDetailId, boolean mandatory) {

        var importExcelColumn = new ImportExcelColumn<BigDecimal>();
        int columnNum = column + 1;
        importExcelColumn.setColumn(columnNum);
        importExcelColumn.setSheetDetailId(sheetDetailId);

        var wrapper = new ExcelColumnWrapper<>(importExcelColumn, new ArrayList<>());

        if (isNull(row)) {
            return wrapper;
        }
        int rowNum = row.getRowNum()  + 1;
        try {
            var cell = row.getCell(column);
            if (nonNull(cell) && nonNull(cell.getStringCellValue())) {
                importExcelColumn.setValue(BigDecimal.valueOf(cell.getNumericCellValue()));
            } else if (mandatory) {
                wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId, THIS_COLUMN_ARE_MANDATORY));
            }
        } catch (Exception e) {
            wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId, "Invalid value type, expected BigDecimal"));
        }
        importExcelColumn.setRow(rowNum + 1);
        return wrapper;
    }

    @Override
    public ExcelColumnWrapper<Instant> getInstantValue(Row row, int column, Long sheetDetailId, boolean mandatory) {

        var importExcelColumn = new ImportExcelColumn<Instant>();
        int columnNum = column + 1;
        importExcelColumn.setColumn(columnNum);
        importExcelColumn.setSheetDetailId(sheetDetailId);

        var wrapper = new ExcelColumnWrapper<>(importExcelColumn, new ArrayList<>());

        if (isNull(row)) {
            return wrapper;
        }
        int rowNum = row.getRowNum()  + 1;
        try {
            var cell = row.getCell(column);
            if (nonNull(cell) && nonNull(cell.getStringCellValue())) {
                importExcelColumn.setValue(Instant.parse(cell.getStringCellValue()));
            } else if (mandatory) {
                wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId, THIS_COLUMN_ARE_MANDATORY));
            }
        } catch (Exception e) {
            wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId, "Invalid value type, expected Instant"));
        }
        importExcelColumn.setRow(rowNum + 1);
        return wrapper;
    }

    @Override
    public ExcelColumnWrapper<Boolean> getBooleanValue(Row row, int column, Long sheetDetailId, boolean mandatory) {

        var importExcelColumn = new ImportExcelColumn<Boolean>();
        int columnNum = column + 1;
        importExcelColumn.setColumn(columnNum);
        importExcelColumn.setSheetDetailId(sheetDetailId);

        var wrapper = new ExcelColumnWrapper<>(importExcelColumn, new ArrayList<>());

        if (isNull(row)) {
            return wrapper;
        }
        int rowNum = row.getRowNum()  + 1;
        try {
            var cell = row.getCell(column);
            if (nonNull(cell) && nonNull(cell.getStringCellValue())) {
                importExcelColumn.setValue(resolveBoolean(cell));
            } else if (mandatory) {
                wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId, THIS_COLUMN_ARE_MANDATORY));
            }
        } catch (Exception e) {
            wrapper.getErrors().add(getError(rowNum, columnNum, sheetDetailId,"Invalid value type, expected boolean"));
        }
        importExcelColumn.setRow(rowNum + 1);
        return wrapper;
    }

    private boolean resolveBoolean(Cell cell) {

        switch (cell.getCellType()) {
            case BOOLEAN -> {
                return cell.getBooleanCellValue();
            }
            case STRING -> {
                String value = cell.getStringCellValue();
                if (value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("N")) {
                    return "Y".equalsIgnoreCase(value);
                }else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
                    return "true".equalsIgnoreCase(value);
                } else if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("no")){
                    return "yes".equalsIgnoreCase(value);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            default -> throw new IllegalArgumentException();
        }
    }  
    
    private ImportTaskErrorAddDto getError(int row, int column, Long sheetDetailId, String message) {
        return ImportTaskErrorAddDto.builder()
                .row(row)
                .column(column)
                .sheetDetailId(sheetDetailId)
                .message(message)
                .build();
    }
}