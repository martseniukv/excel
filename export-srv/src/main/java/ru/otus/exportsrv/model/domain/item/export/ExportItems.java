package ru.otus.exportsrv.model.domain.item.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportItems {

    private List<ItemExport> items;
    private List<BarcodeExport> barcodes;
    private List<ItemPriceExport> prices;
}
