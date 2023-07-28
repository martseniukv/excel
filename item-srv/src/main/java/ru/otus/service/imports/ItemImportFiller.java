package ru.otus.service.imports;

import ru.otus.model.ItemImportDataContext;
import ru.otus.model.ItemImportSubEntityInfo;

public interface ItemImportFiller {

     int PARTITION_SIZE = 64000;

    void fill(ItemImportSubEntityInfo subEntityInfo, ItemImportDataContext importDataContext);
}
