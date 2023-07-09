package ru.otus.exportsrv.model.domain.item.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemExport {

    private String code;
    private String hierarchyCode;
}
