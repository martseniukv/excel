package ru.otus.exportsrv.model.domain.item.export;

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
public class ItemPriceExport {

    private String itemCode;
    private String priceListCode;
    private BigDecimal value;
    private Instant startTime;
}
