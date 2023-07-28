package ru.otus.model.mapper;

import org.mapstruct.Mapper;
import ru.otus.model.ItemImportDataContext;
import ru.otus.model.ItemImportSubEntityInfo;
import ru.otus.model.ItemImportValidationContext;
import ru.otus.model.entity.*;
import ru.otus.model.request.imports.ImportItemBarcodeDto;
import ru.otus.model.request.imports.ImportItemPriceDto;
import ru.otus.model.request.imports.ItemImportDto;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.MapUtils.emptyIfNull;

@Mapper
public interface ItemImportMapper {


    default ItemEntity getItemEntity(ItemImportDto itemImportDto, Map<String, HierarchyEntity> hierarchyMap){

        if (isNull(itemImportDto)) {
            return null;
        }
        var itemEntityBuilder = ItemEntity.builder();
        var hierarchyColumn = itemImportDto.getHierarchyCode();
        var codeColumn = itemImportDto.getCode();
        if (nonNull(hierarchyColumn)) {
            var hierarchyEntity = emptyIfNull(hierarchyMap).get(hierarchyColumn.getValue());
            itemEntityBuilder.hierarchy(hierarchyEntity);
        }
        if (nonNull(codeColumn)) {
            itemEntityBuilder.code(codeColumn.getValue());
        }
        var nameColumn = itemImportDto.getName();
        if (nonNull(nameColumn)) {
            itemEntityBuilder.name(nameColumn.getValue());
        }
        return itemEntityBuilder.build();
    }

    default ItemImportValidationContext getValidationContext(ItemImportSubEntityInfo subEntityInfo,
                                                             ItemImportDataContext importDataContext) {

        if (isNull(subEntityInfo)) {
            return null;
        }
        return ItemImportValidationContext.builder()
                .barcodes(subEntityInfo.getBarcodeMap())
                .priceLists(subEntityInfo.getPriceListMap())
                .hierarchies(subEntityInfo.getHierarchyMap())
                .barcodeCountMap(nonNull(importDataContext) ? importDataContext.getBarCodeCountMap() : null)
                .barcodeItemCodeMap(subEntityInfo.getBarcodeItemCodeMap())
                .build();
    }

    default List<ItemPriceValueEntity> getPriceEntities(List<ImportItemPriceDto> prices,
                                                        ItemEntity item,
                                                        Map<String, PriceListEntity> priceMap){

        List<ItemPriceValueEntity> result = new ArrayList<>();
        if (isEmpty(prices) || isNull(item)) {
            return result;
        }
        if (isNull(priceMap)) {
            priceMap = new HashMap<>();
        }
        for (var priceDto : prices) {

            var priceValueBuilder = ItemPriceValueEntity.builder();

            var priceCodeColumn = priceDto.getPriceListCode();
            if (nonNull(priceCodeColumn)) {
                priceValueBuilder.priceList(priceMap.get(priceCodeColumn.getValue()));
            }
            var priceDateColumn = priceDto.getDate();
            if (nonNull(priceDateColumn)) {
                priceValueBuilder.startTime(priceDateColumn.getValue());
            }
            var priceColumn = priceDto.getPrice();
            if (nonNull(priceColumn)) {
                priceValueBuilder.value(priceColumn.getValue());
            }
            priceValueBuilder.item(item);
            result.add(priceValueBuilder.build());
        }
        return result;
    }

    default List<BarcodeEntity> getBarcodeEntities(List<ImportItemBarcodeDto> barcodes, ItemEntity item){

        List<BarcodeEntity> result = new ArrayList<>();
        if (isEmpty(barcodes) || isNull(item)) {
            return result;
        }
        for (var barcodeDto : barcodes) {

            var barcodeBuilder = BarcodeEntity.builder();

            var barcodeColumn = barcodeDto.getBarcode();
            if (nonNull(barcodeColumn)) {
                barcodeBuilder.barcode(barcodeColumn.getValue());
            }
            var descriptionColumn = barcodeDto.getDescription();
            if (nonNull(descriptionColumn)) {
                barcodeBuilder.description(descriptionColumn.getValue());
            }
            var isDefaultColumn = barcodeDto.getIsDefault();
            if (nonNull(isDefaultColumn)) {
                barcodeBuilder.isDefault(isDefaultColumn.getValue());
            }
            barcodeBuilder.item(item);
            result.add(barcodeBuilder.build());
        }
        return result;
    }
}
