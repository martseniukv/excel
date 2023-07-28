package ru.otus.service.imports;

import ru.otus.model.request.imports.ItemImportData;

public interface ItemImportService {

    void importItems(ItemImportData items);
}
