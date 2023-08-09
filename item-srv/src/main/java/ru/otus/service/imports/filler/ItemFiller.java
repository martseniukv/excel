package ru.otus.service.imports.filler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import ru.otus.model.ItemImportDataContext;
import ru.otus.model.ItemImportSubEntityInfo;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.entity.PriceListEntity;
import ru.otus.repository.ItemRepository;
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
public class ItemFiller implements ItemImportFiller {

    private final ItemRepository itemRepository;

    @Override
    public void fill(ItemImportSubEntityInfo subEntityInfo, ItemImportDataContext importDataContext) {

        if (isNull(subEntityInfo) || isNull(importDataContext) || isEmpty(importDataContext.getItemCodes())) {
            return;
        }

        Map<String, ItemEntity> result = new HashMap<>();
        List<String> itemCodes = new ArrayList<>(importDataContext.getItemCodes());
        List<List<String>> partition = ListUtils.partition(itemCodes, PARTITION_SIZE);

        for (var part : partition) {
            var itemMap = itemRepository.getItemEntitiesByCodes(part)
                    .stream().collect(Collectors.toMap(ItemEntity::getCode, Function.identity()));
            result.putAll(itemMap);
        }
        subEntityInfo.setItemMap(result);
    }
}
