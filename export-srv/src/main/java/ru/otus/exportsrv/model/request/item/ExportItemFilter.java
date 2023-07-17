package ru.otus.exportsrv.model.request.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.request.ExportSheetSettings;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportItemFilter {

    private String code;
    private String hierarchyCode;
    private String barcode;

    private List<ExportSheetSettings> sheetSettings;
}
