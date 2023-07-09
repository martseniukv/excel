package ru.otus.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemExportResponse {

    private Long id;
    private String code;
    private String hierarchyCode;
    private List<BarcodeExportResponse> barcodes;
    private List<ItemPriceExportResponse> prices;
}
