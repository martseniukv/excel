package ru.otus.utils;

import ru.otus.model.ImportErrorDto;

public class ImportErrorDtoFactory {

    private ImportErrorDtoFactory() {
    }

    public static ImportErrorDto getErrorDto(String message, Long sheetDetailId, int row, int column) {
        return ImportErrorDto.builder()
                .code(1)
                .message(message)
                .row(row)
                .column(column)
                .sheetDetailId(sheetDetailId)
                .build();
    }

    public static ImportErrorDto getErrorDto(int code, String message, Long sheetDetailId, int row, int column) {
        return ImportErrorDto.builder()
                .code(code)
                .sheetDetailId(sheetDetailId)
                .message(message)
                .row(row)
                .column(column)
                .build();
    }

    public static ImportErrorDto getErrorDto(String message) {
        return ImportErrorDto.builder()
                .code(1)
                .message(message)
                .build();
    }

    public static ImportErrorDto getErrorDto(Long sheetDetailId, String message) {
        return ImportErrorDto.builder()
                .code(1)
                .sheetDetailId(sheetDetailId)
                .message(message)
                .build();
    }
}
