package ru.otus.model.request.item;

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
public class ItemPriceRequestDto {

    private Long id;
    private Long priceNumberId;
    private BigDecimal price;
    private Instant startTime;
}
