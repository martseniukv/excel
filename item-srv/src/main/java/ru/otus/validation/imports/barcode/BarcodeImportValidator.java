package ru.otus.validation.imports.barcode;

import ru.otus.model.ImportErrorDto;
import ru.otus.model.ItemImportValidationContext;
import ru.otus.model.request.imports.ItemImportDto;

import java.util.List;

public interface BarcodeImportValidator {

    List<ImportErrorDto> validateImportBarcodes(ItemImportDto item, ItemImportValidationContext context);
}
