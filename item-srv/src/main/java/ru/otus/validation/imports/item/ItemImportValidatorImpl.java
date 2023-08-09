package ru.otus.validation.imports.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import ru.otus.model.ImportErrorDto;
import ru.otus.model.ItemImportValidationContext;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.request.imports.ImportExcelColumn;
import ru.otus.model.request.imports.ItemImportDto;
import ru.otus.validation.imports.AbstractValidators;
import ru.otus.validation.imports.barcode.BarcodeImportValidator;
import ru.otus.validation.imports.price.PriceValueImportValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static ru.otus.utils.ImportErrorDtoFactory.getErrorDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemImportValidatorImpl extends AbstractValidators implements ItemImportValidator {

    private final BarcodeImportValidator barcodeImportValidator;
    private final PriceValueImportValidator priceValueImportValidator;

    @Override
    public List<ImportErrorDto> validateImportItem(ItemImportDto item, ItemImportValidationContext context) {

        List<ImportErrorDto> errors = new ArrayList<>();
        if (isNull(item) || isNull(context)) {
            return errors;
        }
        validateString(item.getCode(), true, 50, "ItemCode").ifPresent(errors::add);
        validateString(item.getName(), false, 50, "ItemName").ifPresent(errors::add);
        Map<String, HierarchyEntity> hierarchyMap = MapUtils.emptyIfNull(context.getHierarchies());

        var hierarchyCodeColumn = item.getHierarchyCode();
        validateHierarchyCode(hierarchyMap, hierarchyCodeColumn).ifPresent(errors::add);

        errors.addAll(barcodeImportValidator.validateImportBarcodes(item, context));
        errors.addAll(priceValueImportValidator.validateImportPrices(item.getPrices(), context));
        return errors;
    }

    private Optional<ImportErrorDto> validateHierarchyCode(Map<String, HierarchyEntity> hierarchyMap,
                                                           ImportExcelColumn<String> column) {
        if (isNull(column)) {
            return Optional.of(getErrorDto("Hierarchy are mandatory"));
        }
        if (!hierarchyMap.containsKey(column.getValue())) {
            var errorMassage = String.format("Invalid Hierarchy code: %s", column.getValue());
            return Optional.of(getErrorDto(errorMassage, column.getSheetDetailId(), column.getRow(), column.getColumn()));
        }
        return Optional.empty();
    }
}
