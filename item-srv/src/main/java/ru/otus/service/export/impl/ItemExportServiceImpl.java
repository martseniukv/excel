package ru.otus.service.export.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.entity.ItemPriceValueEntity;
import ru.otus.model.mapper.ItemExportMapper;
import ru.otus.model.request.item.export.ItemExportFilter;
import ru.otus.model.response.ItemExportResponse;
import ru.otus.repository.BarcodeRepository;
import ru.otus.repository.HierarchyRepository;
import ru.otus.repository.ItemPriceValueRepository;
import ru.otus.repository.ItemRepository;
import ru.otus.repository.specification.ItemExportSpecificationBuilder;
import ru.otus.service.export.ItemExportService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemExportServiceImpl implements ItemExportService {

    private final ItemRepository itemRepository;
    private final ItemPriceValueRepository itemPriceValueRepository;
    private final BarcodeRepository barcodeRepository;
    private final HierarchyRepository hierarchyRepository;
    private final ItemExportMapper itemExportMapperImpl;


    @Override
    @Transactional(readOnly = true)
    public List<ItemExportResponse> export(ItemExportFilter filter) {

        if (isNull(filter)){
            throw new IllegalArgumentException("Filter can not be null");
        }

        ItemExportSpecificationBuilder builder = new ItemExportSpecificationBuilder()
                .withCode(filter.getCode())
                .withHierarchyCode(filter.getHierarchyCode())
                .withBarcode(filter.getBarcode());
        List<ItemEntity> all = itemRepository.findAll(builder.build());

        List<Long> itemIds = new ArrayList<>();
        List<Long> hierarchyIds = new ArrayList<>();
        for (ItemEntity item : all) {
            Long id = item.getId();
            itemIds.add(id);
            hierarchyIds.add(item.getHierarchy().getId());
        }

        Map<Long, HierarchyEntity> hierarchyMap = new HashMap<>();
        Map<Long, List<ItemPriceValueEntity>> itemIdPriceMap = new HashMap<>();
        Map<Long, List<BarcodeEntity>> itemIdBarcodeMap = new HashMap<>();

        ListUtils.partition(hierarchyIds, 65000).forEach(hierarchyIdList->
                hierarchyMap.putAll(hierarchyRepository.findAllById(hierarchyIdList).stream()
                .collect(Collectors.toMap(HierarchyEntity::getId, v -> v))));
        ListUtils.partition(itemIds, 65000).forEach(itemIdList -> {

            itemIdPriceMap.putAll(itemPriceValueRepository.findByItemIdsNonDeleted(itemIdList).stream()
                .collect(Collectors.groupingBy(priceValue -> priceValue.getItem().getId())));
            itemIdBarcodeMap.putAll(barcodeRepository.findByItemIdsNonDeleted(itemIdList).stream()
                .collect(Collectors.groupingBy(barcode -> barcode.getItem().getId())));
        });

        return itemExportMapperImpl.getResponse(all, hierarchyMap, itemIdPriceMap, itemIdBarcodeMap);
    }
}
