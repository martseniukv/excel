package ru.otus.exportsrv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.request.item.ImportItemBarcodeDto;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemBarcodeProcessData {

    private Map<String, List<ImportItemBarcodeDto>> itemBarcodes;
    private List<ImportTaskErrorAddDto> errors;
}