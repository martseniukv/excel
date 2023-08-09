package ru.otus.exportsrv.model.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.exportsrv.model.entity.ImportTaskEntity;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.enums.ImportType;
import ru.otus.exportsrv.model.mapper.task.detail.ImportTaskSheetDetailMapper;
import ru.otus.exportsrv.model.request.task.ImportTaskAddDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;

import java.util.Arrays;
import java.util.List;

@Mapper(uses = {ImportTaskSheetDetailMapper.class})
public interface ImportTaskMapper {

    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "file", ignore = true)
    @Mapping(target = "importType", source = "importTypeId")
    @Mapping(target = "importStatus", source = "importStatusId")
    ImportTaskEntity getEntity(ImportTaskAddDto dto);

    List<ImportTaskEntity> getEntity(List<ImportTaskAddDto> dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isFinished", source = "isFinished")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "importType", source = "importType")
    @Mapping(target = "importStatus", source = "importStatus")
    ImportTaskDto getDto(ImportTaskEntity entity);

    List<ImportTaskDto> getDto(List<ImportTaskEntity> entity);

    default ImportStatus getImportStatus(long id) {
        return Arrays.stream(ImportStatus.values())
                .filter(f-> f.getId() == id)
                .findFirst()
                .orElse(null);
    }

    default ImportType getImportType(long id) {
        return Arrays.stream(ImportType.values())
                .filter(f-> f.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
