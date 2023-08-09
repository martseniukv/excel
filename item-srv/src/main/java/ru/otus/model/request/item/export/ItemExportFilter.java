package ru.otus.model.request.item.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemExportFilter {

    private String code;
    private String hierarchyCode;
    private String barcode;
}