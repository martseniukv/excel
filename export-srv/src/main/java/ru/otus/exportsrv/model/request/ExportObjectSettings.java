package ru.otus.exportsrv.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportObjectSettings {

    private int columnIndex;
    private long filedId;
}
