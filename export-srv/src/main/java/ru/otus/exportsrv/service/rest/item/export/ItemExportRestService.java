package ru.otus.exportsrv.service.rest.item.export;

import ru.otus.exportsrv.model.request.item.ExportItemFilter;
import ru.otus.exportsrv.model.domain.item.export.ExportItems;
import ru.otus.exportsrv.model.response.item.export.ItemExportResponse;

import java.util.List;

public interface ItemExportRestService {

    List<ItemExportResponse> getExportItems(ExportItemFilter filter);
}
