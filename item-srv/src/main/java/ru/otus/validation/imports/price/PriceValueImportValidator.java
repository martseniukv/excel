package ru.otus.validation.imports.price;

import ru.otus.model.ImportErrorDto;
import ru.otus.model.ItemImportValidationContext;
import ru.otus.model.request.imports.ImportItemPriceDto;

import java.util.List;

public interface PriceValueImportValidator {

    List<ImportErrorDto> validateImportPrices(List<ImportItemPriceDto> prices, ItemImportValidationContext context);
}
