package ru.otus.validation.imports;

import ru.otus.model.ImportErrorDto;
import ru.otus.model.request.imports.ImportExcelColumn;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;
import static ru.otus.utils.ImportErrorDtoFactory.getErrorDto;

public class AbstractValidators {

    protected Optional<ImportErrorDto> validateString(ImportExcelColumn<String> column, boolean mandatory,
                                                      int minSize, int maxSize, String fieldName) {

        if (!hasText(column.getValue()) && mandatory) {
            String errorMessage = String.format("%s are mandatory", fieldName);
            return Optional.of(getErrorDto(errorMessage, column.getRow(), column.getColumn()));
        }
        if (nonNull(column.getValue()) && column.getValue().length() > maxSize) {
            String errorMessage = String.format("%s size must be less than %s", fieldName, maxSize);
            return Optional.of(getErrorDto(errorMessage, column.getRow(), column.getColumn()));
        }
        if (nonNull(column.getValue()) && column.getValue().length() < minSize) {
            String errorMessage = String.format("%s size must be grater than %s", fieldName, maxSize);
            return Optional.of(getErrorDto(errorMessage));
        }
        return Optional.empty();
    }

    protected Optional<ImportErrorDto> validateNonNegative(ImportExcelColumn<BigDecimal> column,
                                                           boolean mandatory, String fieldName) {

        if (isNull(column.getValue()) && mandatory) {
            String errorMessage = String.format("%s are mandatory", fieldName);
            return Optional.of(getErrorDto(errorMessage, column.getRow(), column.getColumn()));
        }
        if (nonNull(column.getValue()) && BigDecimal.ZERO.compareTo(column.getValue()) > 0) {
            String errorMessage = String.format("%s can not be negative", fieldName);
            return Optional.of(getErrorDto(errorMessage));
        }
        return Optional.empty();
    }

    protected Optional<ImportErrorDto> validateNotNull(ImportExcelColumn<?> column, boolean mandatory, String fieldName) {

        if (isNull(column.getValue()) && mandatory) {
            String errorMessage = String.format("%s are mandatory", fieldName);
            return Optional.of(getErrorDto(errorMessage, column.getRow(), column.getColumn()));
        }
        return Optional.empty();
    }

    protected Optional<ImportErrorDto> validateString(ImportExcelColumn<String> column, boolean mandatory,
                                                      int maxSize, String fieldName) {
        return validateString(column, mandatory, 0, maxSize, fieldName);
    }
}
