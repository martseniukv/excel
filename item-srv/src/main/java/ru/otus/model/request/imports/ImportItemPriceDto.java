package ru.otus.model.request.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemPriceDto {

    private ImportExcelColumn<String> priceListCode;
//    private ImportExcelColumn<String> currencyCode;
    private ImportExcelColumn<Instant> date;
    private ImportExcelColumn<BigDecimal> price;
}
