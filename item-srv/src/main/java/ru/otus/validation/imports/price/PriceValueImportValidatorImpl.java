package ru.otus.validation.imports.price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import ru.otus.model.ImportErrorDto;
import ru.otus.model.ItemImportValidationContext;
import ru.otus.model.entity.PriceListEntity;
import ru.otus.model.request.imports.ImportExcelColumn;
import ru.otus.model.request.imports.ImportItemPriceDto;
import ru.otus.validation.imports.AbstractValidators;

import java.time.Instant;
import java.util.*;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static ru.otus.utils.ImportErrorDtoFactory.getErrorDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceValueImportValidatorImpl extends AbstractValidators implements PriceValueImportValidator {

    @Override
    public List<ImportErrorDto> validateImportPrices(List<ImportItemPriceDto> prices, ItemImportValidationContext context) {

        var errors = new ArrayList<ImportErrorDto>();
        if (isEmpty(prices) || isNull(context)) {
            return errors;
        }

        Map<String, PriceListEntity> priceListMap = MapUtils.emptyIfNull(context.getPriceLists());
        Map<String, Set<Instant>> priceListDatesMap = new HashMap<>();
        for (var priceDto : prices) {

            var priceListCode = priceDto.getPriceListCode();
            var price = priceDto.getPrice();
            var date = priceDto.getDate();

            validateCode(priceListMap, priceListCode).ifPresent(errors::add);
            validateNotNull(date, true, "StartTime").ifPresent(errors::add);
            if (errors.isEmpty()){
                var priceCode = priceListCode.getValue();
                checkDuplicateDateByPriceList(priceCode, date, priceListDatesMap).ifPresent(errors::add);
            }
            validateNonNegative(price, true, "Price").ifPresent(errors::add);
        }
        return errors;
    }

    private Optional<ImportErrorDto> validateCode(Map<String, PriceListEntity> priceListMap,
                                                  ImportExcelColumn<String> priceListCode) {
        if (!priceListMap.containsKey(priceListCode.getValue())) {
            var errorMessage = "Invalid priceCode: " + priceListCode.getValue();
            return Optional.of(getErrorDto(errorMessage, priceListCode.getRow(), priceListCode.getColumn()));
        }
        return Optional.empty();
    }

    private Optional<ImportErrorDto> checkDuplicateDateByPriceList(String priceListCode, ImportExcelColumn<Instant> startTimeColumn,
                                                                   Map<String, Set<Instant>> priceListDatesMap) {

        priceListDatesMap.putIfAbsent(priceListCode, new HashSet<>());
        var startTime = startTimeColumn.getValue();
        if (!priceListDatesMap.get(priceListCode).add(startTime)) {
            var errorMessage = String.format("Duplicate start time for price: %s", priceListCode);
            return Optional.of(getErrorDto(errorMessage, startTimeColumn.getRow(), startTimeColumn.getColumn()));
        }
        return Optional.empty();
    }
}

