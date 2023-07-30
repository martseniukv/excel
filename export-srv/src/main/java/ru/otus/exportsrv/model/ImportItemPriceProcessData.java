package ru.otus.exportsrv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.request.item.ImportItemPriceDto;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemPriceProcessData {

    private Map<String, List<ImportItemPriceDto>> itemPriceMap;
    private List<ImportTaskErrorAddDto> errors;
}