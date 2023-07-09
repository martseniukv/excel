package ru.otus.exportsrv.utils;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtils {

    private ExcelUtils() {
    }

    public static void setObjectValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String val) {
            cell.setCellValue(val);
        } else if (value instanceof Integer val) {
            cell.setCellValue(val);
        } else if (value instanceof Double val) {
            cell.setCellValue(val);
        } else if (value instanceof Boolean val) {
            cell.setCellValue(val);
        } else {
            // For any other object type, convert it to a string representation
            cell.setCellValue(value.toString());
        }
    }
}
