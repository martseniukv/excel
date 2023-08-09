package ru.otus.model.enums;

import lombok.Getter;

@Getter
public enum ImportStatus {

    SUCCESS(1L),
    VALIDATE_FILE(2L),
    VALIDATION_ERROR(3L),
    FAILED(4L)
    ;

    private final long id;

    ImportStatus(long id) {
        this.id = id;
    }
}
