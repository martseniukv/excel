package ru.otus.service.export.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    @Transactional
    public List<ItemExportResponse> export(ItemExportFilter filter) {

        if (isNull(filter)){
            throw new IllegalArgumentException("Filter can not be null");
        }

        ItemExportSpecificationBuilder builder = new ItemExportSpecificationBuilder()
                .withCode(filter.getCode())
                .withHierarchyCode(filter.getHierarchyCode())
                .withBarcode(filter.getBarcode());
        List<ItemEntity> all = itemRepository.findAll(builder.build());

//        List<Long> itemIds = all.stream()
//                .map(ItemEntity::getId)
//                .toList();

        Map<Long, HierarchyEntity> hierarchyMap = hierarchyRepository.findAll().stream()
                .collect(Collectors.toMap(HierarchyEntity::getId, v -> v));
        Map<Long, List<ItemPriceValueEntity>> itemIdPriceMap = itemPriceValueRepository.findAll().stream()
                .collect(Collectors.groupingBy(priceValue -> priceValue.getItem().getId()));
        Map<Long, List<BarcodeEntity>> itemIdBarcodeMap = barcodeRepository.findAll().stream()
                .collect(Collectors.groupingBy(barcode -> barcode.getItem().getId()));

        return itemExportMapperImpl.getResponse(all, hierarchyMap, itemIdPriceMap, itemIdBarcodeMap);
    }
}
