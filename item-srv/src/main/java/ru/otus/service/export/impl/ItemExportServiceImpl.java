package ru.otus.service.export.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.mapper.ItemExportMapper;
import ru.otus.model.request.item.export.ItemExportFilter;
import ru.otus.model.response.BarcodeExportResponse;
import ru.otus.model.response.ExportResponse;
import ru.otus.model.response.ItemExportResponse;
import ru.otus.model.response.ItemPriceExportResponse;
import ru.otus.repository.ItemExportRepository;
import ru.otus.repository.ItemRepository;
import ru.otus.repository.specification.ItemExportSpecificationBuilder;
import ru.otus.service.export.ItemExportService;

import java.util.*;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemExportServiceImpl implements ItemExportService {

    private final ItemRepository itemRepository;
    private final ItemExportMapper itemExportMapperImpl;
    private final ItemExportRepository itemExportRepository;


    @Override
    @Transactional
    public ExportResponse export(ItemExportFilter filter, int page, int size) {

        if (isNull(filter)){
            throw new IllegalArgumentException("Filter can not be null");
        }

        ItemExportSpecificationBuilder builder = new ItemExportSpecificationBuilder()
                .withCode(filter.getCode())
                .withHierarchyCode(filter.getHierarchyCode())
                .withBarcode(filter.getBarcode());
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("Id").ascending());
        Page<ItemEntity> all = itemRepository.findAll(builder.build(), pageRequest);

        List<Long> itemIds = new ArrayList<>();
        Set<Long> hierarchyIds = new HashSet<>();
        for (ItemEntity item : all) {
            Long id = item.getId();
            itemIds.add(id);
            hierarchyIds.add(item.getHierarchy().getId());
        }

        String tempTable = itemExportRepository.getTempTable(itemIds, "itemIds");
        String hierarchyTable = itemExportRepository.getTempTable(new ArrayList<>(hierarchyIds), "hierarchyIds");
        Map<Long, HierarchyEntity> hierarchyMap = itemExportRepository.getHierarchyByTempTable(hierarchyTable);
        Map<Long, List<BarcodeExportResponse>> barCodeByTempTable = itemExportRepository.getBarCodeByTempTable(tempTable);
        Map<Long, List<ItemPriceExportResponse>> priceValueByTempTable = itemExportRepository.getPriceValueByTempTable(tempTable);
        itemExportRepository.dropTempTable(tempTable);
        itemExportRepository.dropTempTable(hierarchyTable);
        List<ItemExportResponse> exportItems = itemExportMapperImpl
                .getResponse2(all.getContent(), hierarchyMap, priceValueByTempTable, barCodeByTempTable);

        return ExportResponse.builder()
                .totalPages(all.getTotalPages())
                .size(all.getSize())
                .totalElements(all.getTotalElements())
                .itemResponse(exportItems)
                .build();
    }
}
