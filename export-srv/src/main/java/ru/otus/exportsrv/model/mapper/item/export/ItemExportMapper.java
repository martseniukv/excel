package ru.otus.exportsrv.model.mapper.item.export;

import ru.otus.exportsrv.model.domain.item.export.ExportItems;
import ru.otus.exportsrv.model.response.item.export.ItemExportResponse;

import java.util.List;

public interface ItemExportMapper {

    ExportItems getExportItems(List<ItemExportResponse> items);
}
