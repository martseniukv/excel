package ru.otus.service.imports.filler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import ru.otus.model.ItemImportDataContext;
import ru.otus.model.ItemImportSubEntityInfo;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.repository.HierarchyRepository;
import ru.otus.service.imports.ItemImportFiller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class HierarchyFiller implements ItemImportFiller {

    private final HierarchyRepository hierarchyRepository;

    @Override
    public void fill(ItemImportSubEntityInfo subEntityInfo, ItemImportDataContext importDataContext) {

        if (isNull(subEntityInfo) || isNull(importDataContext) || isEmpty(importDataContext.getHierarchyCodes())) {
            return;
        }

        Map<String, HierarchyEntity> result = new HashMap<>();
        List<String> hierarchyCodes = new ArrayList<>(importDataContext.getHierarchyCodes());
        List<List<String>> partition = ListUtils.partition(hierarchyCodes, PARTITION_SIZE);

        for (var part : partition) {
            var hierarchyMap = hierarchyRepository.getHierarchyEntitiesByCodes(part)
                    .stream().collect(Collectors.toMap(HierarchyEntity::getCode, Function.identity()));
            result.putAll(hierarchyMap);
        }
        subEntityInfo.setHierarchyMap(result);
    }
}
