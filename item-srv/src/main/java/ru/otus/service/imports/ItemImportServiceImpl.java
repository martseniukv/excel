package ru.otus.service.imports;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.kafka.ImportUpdateStatusProducer;
import ru.otus.model.ImportErrorDto;
import ru.otus.model.ItemImportDataContext;
import ru.otus.model.ItemImportSubEntityInfo;
import ru.otus.model.entity.*;
import ru.otus.model.enums.ImportStatus;
import ru.otus.model.mapper.ItemImportMapper;
import ru.otus.model.request.imports.ImportExcelColumn;
import ru.otus.model.request.imports.ImportTaskUpdateDto;
import ru.otus.model.request.imports.ItemImportData;
import ru.otus.model.request.imports.ItemImportDto;
import ru.otus.repository.BarcodeRepository;
import ru.otus.repository.ItemPriceValueRepository;
import ru.otus.repository.ItemRepository;
import ru.otus.validation.imports.item.ItemImportValidator;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemImportServiceImpl implements ItemImportService {

    private final List<ItemImportFiller> fillers;
    private final ItemImportMapper itemImportMapper;
    private final ItemImportValidator itemImportValidator;
    private final ItemRepository itemRepository;
    private final BarcodeRepository barcodeRepository;
    private final ItemPriceValueRepository priceValueRepository;
    private final ImportUpdateStatusProducer importUpdateStatusProducer;

    @Override
    @Transactional
    public void importItems(ItemImportData importData) {

        if (isNull(importData) || isEmpty(importData.getItems())) {
            return;
        }
        List<ItemImportDto> items = importData.getItems();
        long importTaskId = importData.getImportTaskId();

        List<ItemEntity> saveItems = new ArrayList<>();
        List<BarcodeEntity> saveBarcodes = new ArrayList<>();
        List<ItemPriceValueEntity> savePriceValue = new ArrayList<>();
        var dataContext = getItemImportDataContext(items);

        ItemImportSubEntityInfo subEntityInfo = new ItemImportSubEntityInfo();

        fillers.forEach(filler -> filler.fill(subEntityInfo, dataContext));

        List<ImportErrorDto> allErrors = new ArrayList<>();
        var validationContext = itemImportMapper.getValidationContext(subEntityInfo, dataContext);
        Map<String, ItemEntity> itemMap = subEntityInfo.getItemMap();
        for (var item : items) {
            List<ImportErrorDto> errors = itemImportValidator.validateImportItem(item, validationContext);
            allErrors.addAll(errors);
            if (isEmpty(errors) && nonNull(item.getCode())) {
                ImportExcelColumn<String> itemCode = item.getCode();

                var hierarchyMap = subEntityInfo.getHierarchyMap();

                ItemEntity existingItem = itemMap.get(itemCode.getValue());
                var itemEntity = itemImportMapper.getItemEntity(item, hierarchyMap);
                if (nonNull(existingItem)) {

                    itemEntity.setId(existingItem.getId());
                    itemEntity.setVersion(existingItem.getVersion());
                }
                saveItems.add(itemEntity);
                var barcodeEntities = itemImportMapper.getBarcodeEntities(item.getBarcodes(), itemEntity);
                saveBarcodes.addAll(barcodeEntities);
                var priceEntities = itemImportMapper.getPriceEntities(item.getPrices(), itemEntity, subEntityInfo.getPriceListMap());
                savePriceValue.addAll(priceEntities);

//                if (nonNull(exsistItem)) {
//
//                    var itemEntity = itemImportMapper.getItemEntity(item, hierarchyMap);
//                    itemEntity.setId(exsistItem.getId());
//                    itemEntity.setVersion(exsistItem.getVersion());
//                    updateItems.add(itemEntity);
//                    var barcodeEntities = itemImportMapper.getBarcodeEntities(item.getBarcodes(), itemEntity);
//                    saveBarcodes.addAll(barcodeEntities);
//                    var priceEntities = itemImportMapper.getPriceEntities(item.getPrices(), itemEntity, subEntityInfo.getPriceListMap());
//                    savePriceValue.addAll(priceEntities);
//                } else {
//                    var itemEntity = itemImportMapper.getItemEntity(item, hierarchyMap);
//                    saveItems.add(itemEntity);
//
//                    var barcodeEntities = itemImportMapper.getBarcodeEntities(item.getBarcodes(), itemEntity);
//                    saveBarcodes.addAll(barcodeEntities);
//                    var priceEntities = itemImportMapper.getPriceEntities(item.getPrices(), itemEntity, subEntityInfo.getPriceListMap());
//                    savePriceValue.addAll(priceEntities);
//                }

            }
        }

        log.info("Errors size : {}", allErrors.size());

        if (!saveItems.isEmpty()) {
            Collection<ItemEntity> existingItems = itemMap.values();
            barcodeRepository.deleteByItems(existingItems);
            priceValueRepository.deleteByItems(existingItems);

            itemRepository.saveAll(saveItems);
            barcodeRepository.saveAll(saveBarcodes);
            priceValueRepository.saveAll(savePriceValue);
        }

        ImportTaskUpdateDto updateDto = ImportTaskUpdateDto.builder()
                .importTask(importTaskId)
                .status(allErrors.isEmpty() ? ImportStatus.SUCCESS : ImportStatus.FAILED)
                .errors(allErrors)
                .build();
        importUpdateStatusProducer.sendMessage(updateDto);
//        itemRepository.saveAll(updateItems);
//        priceValueRepository.saveAll(updatePriceValue);
//        barcodeRepository.saveAll(updateBarcodes);
    }

    private static ItemImportDataContext getItemImportDataContext(List<ItemImportDto> items) {

        ItemImportDataContext dataContext = new ItemImportDataContext();
        Set<String> itemCodes = new HashSet<>();
        Set<String> hierarchyCodes = new HashSet<>();
        Set<String> priceListCodes = new HashSet<>();
        Set<String> barcodesCodes = new HashSet<>();
        Map<String, Map<String, Integer>> itemBarcodeCountMap = new HashMap<>();

        for (var importItem : items) {

            String itemCode = null;
            var itemCodeColumn = importItem.getCode();
            var prices = importItem.getPrices();
            var barcodes = importItem.getBarcodes();

            if (itemCodeColumn != null) {
                var code = itemCodeColumn.getValue();
                if (code != null) {
                    itemCode = code;
                    itemCodes.add(code);
                }
            }
            ofNullable(importItem.getHierarchyCode())
                    .flatMap(code -> ofNullable(code.getValue()))
                    .ifPresent(hierarchyCodes::add);

            if (isNotEmpty(prices)) {
                for (var itemPriceDto : prices) {
                    var priceListCodeColumn = itemPriceDto.getPriceListCode();

                    if (nonNull(priceListCodeColumn)) {
                        String priceListCode = priceListCodeColumn.getValue();
                        ofNullable(priceListCode).ifPresent(priceListCodes::add);
                    }
                }
            }

            if (isNotEmpty(barcodes)) {
                for (var importBarcode : barcodes) {
                    //collect barcodes verify duplicate
                    itemBarcodeCountMap.putIfAbsent(itemCode, new HashMap<>());

                    ImportExcelColumn<String> barcodeColumn = importBarcode.getBarcode();
                    if (nonNull(barcodeColumn) && nonNull(barcodeColumn.getValue())) {

                        String barcode = barcodeColumn.getValue();
                        itemBarcodeCountMap.get(itemCode).putIfAbsent(barcode, 0);
                        itemBarcodeCountMap.get(itemCode).computeIfPresent(barcode, (k, v) -> v + 1);

                        barcodesCodes.add(barcode);
                    }
                }
            }
        }
        var barCodeCountMap = itemBarcodeCountMap.values().stream()
                .flatMap(barcodeEntry -> barcodeEntry.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));

        dataContext.setBarCodeCountMap(barCodeCountMap);
        dataContext.setItemCodes(itemCodes);
        dataContext.setHierarchyCodes(hierarchyCodes);
        dataContext.setPriceListCodes(priceListCodes);
        dataContext.setBarcodesCodes(barcodesCodes);
        return dataContext;
    }
}
