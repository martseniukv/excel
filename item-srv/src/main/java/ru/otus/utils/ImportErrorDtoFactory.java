package ru.otus.utils;

import ru.otus.model.ImportErrorDto;

public class ImportErrorDtoFactory {

    private ImportErrorDtoFactory() {
    }

    public static ImportErrorDto getErrorDto(String message, int row, int column) {
        return ImportErrorDto.builder()
                .code(1)
                .message(message)
                .row(row)
                .column(column)
                .build();
    }

    public static ImportErrorDto getErrorDto(int code, String message, int row, int column) {
        return ImportErrorDto.builder()
                .code(code)
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
}
