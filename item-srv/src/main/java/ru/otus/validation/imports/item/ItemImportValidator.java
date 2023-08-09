package ru.otus.validation.imports.item;

import ru.otus.model.ImportErrorDto;
import ru.otus.model.ItemImportValidationContext;
import ru.otus.model.request.imports.ItemImportDto;

import java.util.List;

public interface ItemImportValidator {

    List<ImportErrorDto> validateImportItem(ItemImportDto item, ItemImportValidationContext context);
}
