package ru.otus.exportsrv.model.response.item.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeExportResponse {

    private String barcode;
    private String description;
    private String isDefault;
}
