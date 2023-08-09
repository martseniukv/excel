package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.config.AspectLogExecuteTime;
import ru.otus.model.entity.*;
import ru.otus.model.request.item.export.ItemExportFilter;
import ru.otus.model.response.ItemExportResponse;
import ru.otus.repository.ItemRepo;
import ru.otus.repository.PriceListRepository;
import ru.otus.service.barcode.BarcodeService;
import ru.otus.service.export.ItemExportService;
import ru.otus.service.item.ItemService;
import ru.otus.service.price.value.PriceValueService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ItemExportController {

    private final ItemExportService itemExportServiceImpl;
    private final ItemService itemService;
    private final BarcodeService barcodeServiceImpl;
    private final PriceValueService priceValueService;
    private final PriceListRepository priceListRepository;
    private final ItemRepo itemRepo;

    @PostMapping("/export/item")
    public List<ItemExportResponse> export(@RequestBody ItemExportFilter filter) {
        return itemExportServiceImpl.export(filter);
    }

    @GetMapping("/million")
    public ResponseEntity<Boolean> export2() {

        PriceListEntity priceList = priceListRepository.findById(1L).orElse(null);

        List<ItemEntity> itemEntities = new ArrayList<>();
        List<HierarchyEntity> hierarchyEntities = new ArrayList<>();
        List<ItemPriceValueEntity> priceValues = new ArrayList<>();
        List<BarcodeEntity> barcodes = new ArrayList<>();
        for (long i = 1; i <= 500_000; i++) {
            ItemEntity item = new ItemEntity();
            item.setCode("00" + i);
            item.setName("ItemName:" + i);
            item.setId(i);
            item.setVersion(1L);
            item.setDeleted(false);
            item.setCreateDate(Instant.now());
            item.setUpdateDate(Instant.now());
            HierarchyEntity hierarchy = new HierarchyEntity();
            hierarchy.setId(i);
            hierarchy.setCode("H_" + i);
            hierarchy.setName("HierarchyName_" + i);
            hierarchy.setVersion(1L);
            hierarchy.setDeleted(false);
            hierarchy.setCreateDate(Instant.now());
            hierarchy.setUpdateDate(Instant.now());
            item.setHierarchy(hierarchy);
            hierarchyEntities.add(hierarchy);
            itemEntities.add(item);

                BarcodeEntity barcodeEntity = new BarcodeEntity();
                barcodeEntity.setItem(item);
                barcodeEntity.setBarcode("ItemBarcode_" + item.getCode());
                barcodeEntity.setDescription("ItemDescription_" + item.getCode());
                barcodeEntity.setDefault(i % 2 == 0);
                barcodes.add(barcodeEntity);

//                barcodeRepository.save(barcodeEntity);

                ItemPriceValueEntity itemPriceValueEntity = new ItemPriceValueEntity();
                itemPriceValueEntity.setValue(new BigDecimal(i));
                itemPriceValueEntity.setPriceList(priceList);
                itemPriceValueEntity.setStartTime(Instant.now());
                itemPriceValueEntity.setItem(item);
                priceValues.add(itemPriceValueEntity);

//                itemPriceValueRepository.save(itemPriceValueEntity);
        }
        itemRepo.saveAllHierarchy(hierarchyEntities);
//        itemRepo.saveAll(itemEntities);
//        itemRepo.saveAllBarcode(barcodes);
//        itemRepo.saveAllPrices(priceValues);

//       List<ItemEntity> itemEntities1 = new ArrayList<>();//itemService.saveItems(itemEntities);

//        for (ItemEntity item : itemEntities1) {
//
//            for (int j = 0; j < 2; j++) {
//
//                BarcodeEntity barcodeEntity = new BarcodeEntity();
//                barcodeEntity.setItem(item);
//                barcodeEntity.setBarcode("ItemBarcode_" + item.getCode() + "_" + j);
//                barcodeEntity.setDescription("ItemDescription_" + item.getCode() + "_" + j);
//                barcodeEntity.setDefault(j % 2 == 0);
//                barcodes.add(barcodeEntity);
//
////                barcodeRepository.save(barcodeEntity);
//
//                ItemPriceValueEntity itemPriceValue = new ItemPriceValueEntity();
//                itemPriceValue.setValue(new BigDecimal(item.getId()).add(new BigDecimal(j)));
//                itemPriceValue.setPriceList(priceList);
//                itemPriceValue.setStartTime(Instant.now());
//                itemPriceValue.setItem(item);
//                priceValues.add(itemPriceValue);
//
////                itemPriceValueRepository.save(itemPriceValue);
//            }
//        }
//        barcodeServiceImpl.saveBarcodes(barcodes);
//        priceValueService.saveItemPriceValues(priceValues);
        return ResponseEntity.ok(true);
    }
}
