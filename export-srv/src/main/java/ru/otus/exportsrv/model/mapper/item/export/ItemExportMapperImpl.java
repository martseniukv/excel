package ru.otus.exportsrv.model.mapper.item.export;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.exportsrv.model.domain.item.export.BarcodeExport;
import ru.otus.exportsrv.model.domain.item.export.ExportItems;
import ru.otus.exportsrv.model.domain.item.export.ItemExport;
import ru.otus.exportsrv.model.domain.item.export.ItemPriceExport;
import ru.otus.exportsrv.model.response.item.export.ExportResponse;
import ru.otus.exportsrv.model.response.item.export.ItemExportResponse;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
public class ItemExportMapperImpl implements ItemExportMapper {

    @Override
    public ExportItems getExportItems(ExportResponse exportResponse){

        if (isNull(exportResponse) || isEmpty(exportResponse.getItemResponse())) {
            return new ExportItems();
        }

        List<ItemExport> itemExports = new ArrayList<>();
        List<BarcodeExport> barcodes = new ArrayList<>();
        List<ItemPriceExport> prices = new ArrayList<>();

        List<ItemExportResponse> items = exportResponse.getItemResponse();
        for (var item : items) {

            var itemCode = item.getCode();
            ItemExport itemExport = ItemExport.builder()
                    .code(itemCode)
                    .name(item.getName())
                    .hierarchyCode(item.getHierarchyCode())
                    .build();
            itemExports.add(itemExport);


            for (var barcode : emptyIfNull(item.getBarcodes())) {

                var barcodeExport = BarcodeExport.builder()
                        .itemCode(itemCode)
                        .barcode(barcode.getBarcode())
                        .description(barcode.getDescription())
                        .isDefault(barcode.getIsDefault())
                        .build();
                barcodes.add(barcodeExport);
            }
            for (var price : emptyIfNull(item.getPrices())) {

                var itemPriceExport = ItemPriceExport.builder()
                        .itemCode(itemCode)
                        .priceListCode(price.getPriceListCode())
                        .value(price.getValue())
                        .startTime(price.getStartTime())
                        .build();
                prices.add(itemPriceExport);
            }
        }
        return ExportItems.builder()
                .totalPages(exportResponse.getTotalPages())
                .size(exportResponse.getSize())
                .totalElements(exportResponse.getTotalElements())
                .items(itemExports)
                .barcodes(barcodes)
                .prices(prices)
                .build();
    }
}
