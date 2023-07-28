package ru.otus.exportsrv.model.request.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImportData {

    private long importTaskId;
    private List<ItemImportDto> items;
}
