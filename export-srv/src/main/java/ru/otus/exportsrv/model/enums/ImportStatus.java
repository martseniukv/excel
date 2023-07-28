package ru.otus.exportsrv.model.enums;

import lombok.Getter;

@Getter
public enum ImportStatus {

    CREATED(1L),
    SUCCESS(2L),
    VALIDATE_FILE(3L),
    VALIDATION_ERROR(4L),
    FAILED(5L)
    ;

    private final long id;

    ImportStatus(long id) {
        this.id = id;
    }
}
