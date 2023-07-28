package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.entity.PriceListEntity;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImportValidationContext {

    private Map<String, HierarchyEntity> hierarchies;
    private Map<String, PriceListEntity> priceLists;
    private Map<String, BarcodeEntity> barcodes;

    private Map<String, String> barcodeItemCodeMap;
    private Map<String, Integer> barcodeCountMap;
}
