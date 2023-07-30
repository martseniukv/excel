package ru.otus.exportsrv.service.task.sheetdetail;

import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

import java.util.Collection;
import java.util.List;

public interface ImportSheetDetailService {

    SheetDetailDto getById(Long id);

    List<SheetDetailDto> getByIds(Collection<Long> ids);
}