package ru.otus.validation.imports.barcode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import ru.otus.model.ImportErrorDto;
import ru.otus.model.ItemImportValidationContext;
import ru.otus.model.request.imports.ImportExcelColumn;
import ru.otus.model.request.imports.ItemImportDto;
import ru.otus.validation.imports.AbstractValidators;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.*;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static ru.otus.utils.ImportErrorDtoFactory.getErrorDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarcodeImportValidatorImpl extends AbstractValidators implements BarcodeImportValidator {

    @Override
    public List<ImportErrorDto> validateImportBarcodes(ItemImportDto item, ItemImportValidationContext context) {

        var errors = new ArrayList<ImportErrorDto>();
        if (isNull(item) || isNull(context)) {
            return errors;
        }

        var isDefaultUnique = new HashSet<Boolean>();
        var barcodeCountMap = MapUtils.emptyIfNull(context.getBarcodeCountMap());
        var itemBarcodeMap = MapUtils.emptyIfNull(context.getBarcodeItemCodeMap());

        for (var importBarcode : emptyIfNull(item.getBarcodes())) {

            var barcodeColumn = importBarcode.getBarcode();
            var barcodeError = validateString(barcodeColumn, true, 50, "Barcode");
            if (barcodeError.isEmpty()) {
                var itemCode = ofNullable(item.getCode()).map(ImportExcelColumn::getValue).orElse(null);
                checkUniqueInDb(itemBarcodeMap, barcodeColumn, itemCode).ifPresent(errors::add);
                checkDuplicate(barcodeCountMap, barcodeColumn).ifPresent(errors::add);
            } else {
                errors.add(barcodeError.get());
            }

            var descriptionColumn = importBarcode.getDescription();
            validateString(descriptionColumn, false, 255, "Description").ifPresent(errors::add);

            var isDefaultColumn = importBarcode.getIsDefault();
            validateNotNull(isDefaultColumn, true,"IsDefault").ifPresent(errors::add);
            checkQuantityDefaultBarcodes(isDefaultUnique, isDefaultColumn).ifPresent(errors::add);
        }
        return errors;
    }

    private Optional<ImportErrorDto> checkUniqueInDb(Map<String, String> barcodeMap, ImportExcelColumn<String> barcodeColumn,
                                                     String itemCode) {

        String barcode = barcodeColumn.getValue();
        String existingItemCode = barcodeMap.get(barcode);

        if (nonNull(existingItemCode) && !existingItemCode.equals(itemCode)) {
            String errorMessage = String.format("Barcode: %s is not unique", barcode);
            return of(getErrorDto(errorMessage, barcodeColumn.getRow(), barcodeColumn.getColumn()));
        }
        return empty();
    }

    private Optional<ImportErrorDto> checkDuplicate(Map<String, Integer> barcodeCountMap,
                                                    ImportExcelColumn<String> barcodeColumn) {

        String barcode = barcodeColumn.getValue();
        Integer countBarcodes = barcodeCountMap.get(barcode);

        if (nonNull(countBarcodes) && countBarcodes > 1) {
            String errorMessage = String.format("Barcode: %s are duplicate", barcode);
            return of(getErrorDto(errorMessage, barcodeColumn.getRow(), barcodeColumn.getColumn()));
        }
        return empty();
    }

    private Optional<ImportErrorDto> checkQuantityDefaultBarcodes(Set<Boolean> isDefaultUnique,
                                                                  ImportExcelColumn<Boolean> isDefaultColumn) {

        if (isNull(isDefaultColumn)) {
            return Optional.empty();
        }
        Boolean isDefault = isDefaultColumn.getValue();
        if (Boolean.TRUE.equals(isDefault) && !isDefaultUnique.add(true)) {
            String errorMessage = "Сan only be one default barcode";
            return of(getErrorDto(errorMessage, isDefaultColumn.getRow(), isDefaultColumn.getColumn()));
        }
        return empty();
    }
}
