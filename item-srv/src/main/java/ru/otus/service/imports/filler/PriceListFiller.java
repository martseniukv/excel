package ru.otus.service.imports.filler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import ru.otus.model.ItemImportDataContext;
import ru.otus.model.ItemImportSubEntityInfo;
import ru.otus.model.entity.PriceListEntity;
import ru.otus.repository.PriceListRepository;
import ru.otus.service.imports.ItemImportFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceListFiller implements ItemImportFiller {

    private final PriceListRepository priceListRepository;

    @Override
    public void fill(ItemImportSubEntityInfo subEntityInfo, ItemImportDataContext importDataContext) {

        if (isNull(subEntityInfo) || isNull(importDataContext) || isEmpty(importDataContext.getPriceListCodes())) {
            return;
        }

        Map<String, PriceListEntity> result = new HashMap<>();
        List<String> priceListCodes = new ArrayList<>(importDataContext.getPriceListCodes());
        List<List<String>> partition = ListUtils.partition(priceListCodes, PARTITION_SIZE);

        for (var part : partition) {
            var priceListMap = priceListRepository.getPriceListEntitiesByCodes(part)
                    .stream().collect(Collectors.toMap(PriceListEntity::getCode, Function.identity()));
            result.putAll(priceListMap);
        }
        subEntityInfo.setPriceListMap(result);
    }
}
