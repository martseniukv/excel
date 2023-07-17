package ru.otus.exportsrv.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportSheetSettings {

    private String sheetName;
    private String objectName;
    private List<ExportObjectSettings> objectSettings;
}