package ru.otus.exportsrv.model.mapper.item.export;

import ru.otus.exportsrv.model.domain.item.export.ExportItems;
import ru.otus.exportsrv.model.response.item.export.ExportResponse;

public interface ItemExportMapper {

    ExportItems getExportItems(ExportResponse exportResponse);
}
