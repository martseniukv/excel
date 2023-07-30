package ru.otus.exportsrv.service.task.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exportsrv.model.entity.ImportTaskEntity;
import ru.otus.exportsrv.model.mapper.task.ImportTaskMapper;
import ru.otus.exportsrv.model.request.task.ImportTaskAddDto;
import ru.otus.exportsrv.model.request.task.ImportTaskUpdateDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;
import ru.otus.exportsrv.repository.task.ImportTaskRepository;
import ru.otus.exportsrv.service.task.ImportTaskService;
import ru.otus.exportsrv.service.task.error.ImportErrorService;

import static java.lang.String.format;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportTaskServiceImpl implements ImportTaskService {

    private final ImportTaskMapper importTaskMapper;
    private final ImportTaskRepository importTaskRepository;
    private final ImportErrorService importErrorService;

    @Override
    @Transactional
    public ImportTaskDto saveTask(ImportTaskAddDto dto) {
        ImportTaskEntity entity = importTaskMapper.getEntity(dto);
        emptyIfNull(entity.getSheetDetails()).forEach(f-> f.setImportTask(entity));

        ImportTaskEntity save = importTaskRepository.save(entity);
        return importTaskMapper.getDto(save);
    }

    @Override
    public ImportTaskDto getById(long id) {
        return importTaskRepository.findById(id)
                .map(importTaskMapper::getDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public ImportTaskDto updateTask(ImportTaskUpdateDto dto) {

        long importTask = dto.getImportTask();
        ImportTaskEntity byId = importTaskRepository.findById(importTask)
                .orElseThrow(() -> new IllegalStateException(format("Import task with id: %s not found", importTask)));

        byId.setIsFinished(dto.isFinished());
        importErrorService.saveAll(dto.getErrors());
        return importTaskMapper.getDto(byId);
    }
}
