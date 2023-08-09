package ru.otus.exportsrv.model.enums;

import lombok.Getter;
import ru.otus.exportsrv.model.domain.item.export.BarcodeExport;
import ru.otus.exportsrv.model.domain.item.export.ItemExport;
import ru.otus.exportsrv.model.domain.item.export.ItemPriceExport;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.isNull;

@Getter
public enum ExportObject {

    ITEM("item", ItemExport.class),
    ITEM_BARCODE("itemBarcode", BarcodeExport.class),
    ITEM_PRICE("itemPrice", ItemPriceExport.class)
    ;

    private final String name;
    private final Class<?> clazz;

    ExportObject(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public static Optional<ExportObject> getExportObject(String name) {

        if (isNull(name)) {
            return Optional.empty();
        }
        return Arrays.stream(ExportObject.values())
                .filter(f-> f.getName().equals(name))
                .findFirst();
    }
}
