package ru.otus.exportsrv.model.domain.item.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeExport {

    private String itemCode;
    private String barcode;
    private String description;
    private String isDefault;
}
