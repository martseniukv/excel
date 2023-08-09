package ru.otus.exportsrv.utils;

import org.apache.poi.ss.usermodel.*;

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

    public static CellStyle getDefaultCellStyle(Workbook workbook){
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setWrapText(true);
        return headerStyle;
    }
}
