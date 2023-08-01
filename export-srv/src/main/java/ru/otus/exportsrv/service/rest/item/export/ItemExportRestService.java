package ru.otus.exportsrv.service.rest.item.export;

import ru.otus.exportsrv.model.request.item.export.ExportItemFilter;
import ru.otus.exportsrv.model.response.item.export.ExportResponse;

public interface ItemExportRestService {

    ExportResponse getExportItems(int page, int size, ExportItemFilter filter);
}
