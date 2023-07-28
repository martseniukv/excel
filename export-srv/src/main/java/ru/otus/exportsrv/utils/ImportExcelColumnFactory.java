package ru.otus.exportsrv.utils;

import org.apache.poi.ss.usermodel.Row;
import ru.otus.exportsrv.model.ImportExcelColumn;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ImportExcelColumnFactory {

    public static final String Y = "Y";

    private ImportExcelColumnFactory() {
    }
    public static ImportExcelColumn<String> getStringColumn(Row row, int columnNum) {

        var importExcelColumn = new ImportExcelColumn<String>();
        importExcelColumn.setColumn(columnNum + 1);

        if (isNull(row)) {
            return importExcelColumn;
        }
        var cell = row.getCell(columnNum);
        if (nonNull(cell)) {
            importExcelColumn.setValue(cell.getStringCellValue());
        }
        importExcelColumn.setRow(row.getRowNum() + 1);
        return importExcelColumn;
    }

    public static ImportExcelColumn<BigDecimal> getBigDecimalColumn(Row row, int columnNum) {

        var importExcelColumn = new ImportExcelColumn<BigDecimal>();
        importExcelColumn.setColumn(columnNum + 1);

        if (isNull(row)) {
            return importExcelColumn;
        }
        var cell = row.getCell(columnNum);
        if (nonNull(cell)) {
            importExcelColumn.setValue(BigDecimal.valueOf(cell.getNumericCellValue()));
        }
        importExcelColumn.setRow(row.getRowNum() + 1);
        return importExcelColumn;
    }

    public static ImportExcelColumn<Instant> getInstantColumn(Row row, int columnNum) {

        var importExcelColumn = new ImportExcelColumn<Instant>();
        importExcelColumn.setColumn(columnNum + 1);

        if (isNull(row)) {
            return importExcelColumn;
        }
        var cell = row.getCell(columnNum);
        if (nonNull(cell)) {
            importExcelColumn.setValue(Instant.parse(cell.getStringCellValue()));
        }
        importExcelColumn.setRow(row.getRowNum() + 1);
        return importExcelColumn;
    }

    public static ImportExcelColumn<Boolean> getBooleanImportExcelColumn(Row row, int columnNum) {

        var importExcelColumn = new ImportExcelColumn<Boolean>();
        importExcelColumn.setColumn(columnNum);

        if (isNull(row)) {
            return importExcelColumn;
        }

        var cell = row.getCell(columnNum);
        if (nonNull(cell)) {
            var value = cell.getStringCellValue();
            importExcelColumn.setValue(Y.equals(value));
        }
        importExcelColumn.setRow(row.getRowNum());
        return importExcelColumn;
    }
}
