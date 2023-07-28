package ru.otus.exportsrv.model.request.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.ImportExcelColumn;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemPriceDto {

    private ImportExcelColumn<String> priceListCode;
    private ImportExcelColumn<Instant> date;
    private ImportExcelColumn<BigDecimal> price;
}
