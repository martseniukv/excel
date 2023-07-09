package ru.otus.model.response;

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
public class ItemPriceExportResponse {

    private String priceListCode;
    private String currencyCode;
    private BigDecimal value;
    private Instant startTime;
}
