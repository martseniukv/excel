package ru.otus.exportsrv.service.task.error.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.exportsrv.model.entity.ImportTaskEntity;
import ru.otus.exportsrv.model.entity.ImportTaskErrorEntity;
import ru.otus.exportsrv.model.mapper.task.error.ImportTaskErrorMapper;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.repository.error.ImportTaskErrorRepository;
import ru.otus.exportsrv.service.task.error.ImportErrorService;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportErrorServiceImpl implements ImportErrorService {

    private final ImportTaskErrorRepository importTaskErrorRepository;
    private final ImportTaskErrorMapper importTaskErrorMapper;


    public void saveAll(List<ImportTaskErrorAddDto> errors, long importTask) {

        if (isEmpty(errors)) {
            return;
        }
        List<ImportTaskErrorEntity> entities = errors.stream()
                .map(error -> {
                    ImportTaskErrorEntity entity = importTaskErrorMapper.getEntity(error);
                    entity.setImportTask(ImportTaskEntity.builder().id(importTask).build());
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
