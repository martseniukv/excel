package ru.otus.exportsrv.model.mapper.task.detail;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.exportsrv.model.entity.ImportSheetDetailEntity;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

import java.util.List;

@Mapper
public interface ImportTaskSheetDetailMapper {

    @Mapping(target = "sheetName", source = "sheetName")
    @Mapping(target = "lineFrom", source = "lineFrom")
    @Mapping(target = "lineTo", source = "lineTo")
    @Mapping(target = "importObject", source = "importObject")
    SheetDetailDto getDto(ImportSheetDetailEntity entity);

    List<SheetDetailDto> getDto(List<ImportSheetDetailEntity> entities);
}
