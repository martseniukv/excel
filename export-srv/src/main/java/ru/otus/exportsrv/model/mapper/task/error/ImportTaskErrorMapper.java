package ru.otus.exportsrv.model.mapper.task.error;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.exportsrv.model.entity.ImportTaskErrorEntity;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.model.response.task.error.ImportTaskErrorDto;

import java.util.List;

@Mapper
public interface ImportTaskErrorMapper {

    @Mapping(target = "sheetDetail", ignore = true)
    @Mapping(target = "rowNum", source = "row")
    @Mapping(target = "columnNum", source = "column")
    @Mapping(target = "message", source = "message")
    ImportTaskErrorEntity getEntity(ImportTaskErrorAddDto dto);

    List<ImportTaskErrorEntity> getEntity(List<ImportTaskErrorAddDto> dto);

    @Mapping(target = "sheetDetailId", source = "sheetDetail.id")
    @Mapping(target = "rowNum", source = "rowNum")
    @Mapping(target = "columnNum", source = "columnNum")
    @Mapping(target = "message", source = "message")
    ImportTaskErrorDto getDto(ImportTaskErrorEntity entity);

    List<ImportTaskErrorDto> getDto(List<ImportTaskErrorEntity> entity);
}
