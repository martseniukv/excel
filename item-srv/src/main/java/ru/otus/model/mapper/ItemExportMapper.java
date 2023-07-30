package ru.otus.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.entity.ItemPriceValueEntity;
import ru.otus.model.response.BarcodeExportResponse;
import ru.otus.model.response.ItemExportResponse;
import ru.otus.model.response.ItemPriceExportResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Mapper
public interface ItemExportMapper {

    default List<ItemExportResponse> getResponse(List<ItemEntity> items,
                                                 Map<Long, HierarchyEntity> hierarchyMap,
                                                 Map<Long, List<ItemPriceValueEntity>> itemIdPriceMap,
                                                 Map<Long, List<BarcodeEntity>> itemIdBarcodeMap) {

        List<ItemExportResponse> itemExportResponses = new ArrayList<>();
        if ( items == null ) {
            return itemExportResponses;
        }
        for (var item : items) {

            var itemExportResponse = ItemExportResponse.builder();

            Long itemId = item.getId();
            itemExportResponse.id(itemId);
            itemExportResponse.code(item.getCode());
            itemExportResponse.name(item.getName());
            itemExportResponse.barcodes(getBarcodeExport(itemIdBarcodeMap.getOrDefault(itemId, new ArrayList<>())));
            itemExportResponse.prices(getItemPriceExportResponse(itemIdPriceMap.getOrDefault(itemId, new ArrayList<>())));

            HierarchyEntity hierarchy = hierarchyMap.get(item.getHierarchy().getId());
            if (nonNull(hierarchy)) {
                itemExportResponse.hierarchyCode(hierarchy.getCode());
            }
            itemExportResponses.add(itemExportResponse.build());
        }
        return itemExportResponses;
    }


    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "isDefault", source = "default")
    BarcodeExportResponse getBarcodeExport(BarcodeEntity entity);

    List<BarcodeExportResponse> getBarcodeExport(List<BarcodeEntity> entity);

    @Mapping(target = "priceListCode", source = "priceList.code")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "startTime", source = "startTime")
    ItemPriceExportResponse getItemPriceExportResponse(ItemPriceValueEntity itemPriceValueEntity);

    List<ItemPriceExportResponse> getItemPriceExportResponse(List<ItemPriceValueEntity> itemPriceValueEntity);

    private String entityHierarchyCode(ItemEntity itemEntity) {
        if ( itemEntity == null ) {
            return null;
        }
        HierarchyEntity hierarchy = itemEntity.getHierarchy();
        if ( hierarchy == null ) {
            return null;
        }
        return hierarchy.getCode();
    }

    default String stringToBoolean(boolean val){
        return val ? "Y" : "N";
    }
}
