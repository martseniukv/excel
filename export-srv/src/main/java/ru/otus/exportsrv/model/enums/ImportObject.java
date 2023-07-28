package ru.otus.exportsrv.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ImportObject {

    ITEM(1L,"Item"),
    ITEM_BARCODE(2L,"ItemBarcode"),
    ITEM_PRICE(3L, "ItemPrice")
    ;

    private final long id;
    private final String name;

    ImportObject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Optional<ImportObject> getImportObject(long id) {
        return Arrays.stream(ImportObject.values())
                .filter(f-> f.getId() == id)
                .findFirst();
    }
}
