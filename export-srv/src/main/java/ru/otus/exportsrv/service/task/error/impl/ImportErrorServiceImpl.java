package ru.otus.exportsrv.service.task.error.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.exportsrv.model.entity.ImportSheetDetailEntity;
import ru.otus.exportsrv.model.entity.ImportTaskErrorEntity;
import ru.otus.exportsrv.model.mapper.task.error.ImportTaskErrorMapper;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.repository.error.ImportTaskErrorRepository;
import ru.otus.exportsrv.repository.sheetdetail.ImportSheetDetailRepository;
import ru.otus.exportsrv.service.task.error.ImportErrorService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportErrorServiceImpl implements ImportErrorService {

    private final ImportTaskErrorRepository importTaskErrorRepository;
    private final ImportTaskErrorMapper importTaskErrorMapper;
    private final ImportSheetDetailRepository importSheetDetailRepository;


    public void saveAll(List<ImportTaskErrorAddDto> errors) {

        if (isEmpty(errors)) {
            return;
        }
        Set<Long> sheetDetailIds = errors.stream()
                .filter(Objects::nonNull)
                .map(ImportTaskErrorAddDto::getSheetDetailId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, ImportSheetDetailEntity> sheetDetailMap = importSheetDetailRepository.findAllById(sheetDetailIds).stream()
                .collect(Collectors.toMap(ImportSheetDetailEntity::getId, identity()));

        List<ImportTaskErrorEntity> entities = errors.stream()
                .map(error -> {
                    ImportTaskErrorEntity entity = importTaskErrorMapper.getEntity(error);
                    entity.setSheetDetail(sheetDetailMap.get(error.getSheetDetailId()));
                    return entity;
                })
                .toList();
        importTaskErrorRepository.saveAll(entities);
    }

    @Override
    public void deleteByTestId(long importTask) {
        importTaskErrorRepository.deleteByImportTaskAndId(importTask);
    }
}
