package ru.otus.exportsrv.model.enums;

import lombok.Getter;

@Getter
public enum ImportType {

    OTHER(1L),
    IMPORT_ITEM(2L)
    ;


    private final long id;

    ImportType(long id) {
        this.id = id;
    }
}
