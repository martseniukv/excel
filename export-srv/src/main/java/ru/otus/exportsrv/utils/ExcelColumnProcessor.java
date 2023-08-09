package ru.otus.exportsrv.utils;

import org.apache.poi.ss.usermodel.Row;
import ru.otus.exportsrv.model.ExcelColumnWrapper;

import java.math.BigDecimal;
import java.time.Instant;

public interface ExcelColumnProcessor {

    ExcelColumnWrapper<String> getStringValue(Row row, int column, Long sheetDetailId, boolean mandatory);

    ExcelColumnWrapper<BigDecimal> getBigDecimalValue(Row row, int column, Long sheetDetailId, boolean mandatory);

    ExcelColumnWrapper<Instant> getInstantValue(Row row, int column, Long sheetDetailId, boolean mandatory);

    ExcelColumnWrapper<Boolean> getBooleanValue(Row row, int column, Long sheetDetailId, boolean mandatory);
}