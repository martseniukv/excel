package ru.otus.service.imports.filler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import ru.otus.model.ItemImportDataContext;
import ru.otus.model.ItemImportSubEntityInfo;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.repository.BarcodeRepository;
import ru.otus.service.imports.ItemImportFiller;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class BarcodeFiller implements ItemImportFiller {

    private final BarcodeRepository barcodeRepository;

    @Override
    public void fill(ItemImportSubEntityInfo subEntityInfo, ItemImportDataContext importDataContext) {

        if (isNull(subEntityInfo) || isNull(importDataContext) || isEmpty(importDataContext.getBarcodesCodes())) {
            return;
        }

        Map<String, BarcodeEntity> result = new HashMap<>();
        Map<String, String> barcodeItemCodeMap = new HashMap<>();
        List<String> barcodeCodes = new ArrayList<>(importDataContext.getHierarchyCodes());
        List<List<String>> partition = ListUtils.partition(barcodeCodes, PARTITION_SIZE);

        for (var part : partition) {
//            Map<String, List<String>> itemCodeBarcodeMap = barcodeRepository.getBarcodeWithItemCode(part)
//                    .stream()
//                    .collect(Collectors.groupingBy(k -> k.getFirst(), HashMap::new,
//                            Collectors.mapping( pair -> pair.getSecond(), Collectors.toList())));
            var barcodeEntitiesByCodes = barcodeRepository.getBarcodeEntitiesByCodes(part);

            for (var barcodeEntity : barcodeEntitiesByCodes) {

                var barcode = barcodeEntity.getBarcode();
                result.putIfAbsent(barcode, barcodeEntity);
                if (nonNull(barcodeEntity.getItem())) {
                    var item = barcodeEntity.getItem();
                    barcodeItemCodeMap.put(barcode, item.getCode());
                }
            }

//            var barcodeMap = barcodeEntitiesByCodes
//                    .stream().collect(Collectors.toMap(BarcodeEntity::getBarcode, Function.identity()));
//            result.putAll(barcodeMap);
        }
        subEntityInfo.setBarcodeMap(result);
        subEntityInfo.setBarcodeItemCodeMap(barcodeItemCodeMap);
    }
}
