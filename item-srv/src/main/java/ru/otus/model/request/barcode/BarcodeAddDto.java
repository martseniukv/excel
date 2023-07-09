package ru.otus.model.request.barcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeAddDto {

    private Long itemId;
    private String barcode;
    private String description;
}
