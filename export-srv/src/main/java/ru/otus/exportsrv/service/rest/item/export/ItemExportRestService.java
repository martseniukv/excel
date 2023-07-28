package ru.otus.exportsrv.service.rest.item.export;

import ru.otus.exportsrv.model.request.item.export.ExportItemFilter;
import ru.otus.exportsrv.model.response.item.export.ItemExportResponse;

import java.util.List;

public interface ItemExportRestService {

    List<ItemExportResponse> getExportItems(ExportItemFilter filter);
}
