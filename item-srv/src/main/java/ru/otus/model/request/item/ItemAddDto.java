package ru.otus.model.request.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAddDto {

    private String code;
    private String name;
    private Long hierarchyId;
    private List<ItemBarcodeRequestDto> barcodes;
    private List<ItemPriceRequestDto> prices;
}
