package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.entity.PriceListEntity;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImportSubEntityInfo {

    private Map<String, HierarchyEntity> hierarchyMap;
    private Map<String, PriceListEntity> priceListMap;
    private Map<String, ItemEntity> itemMap;
    private Map<String, BarcodeEntity> barcodeMap;
    private Map<String, String> barcodeItemCodeMap;
}
