package ru.otus.service.export;

import ru.otus.model.request.item.export.ItemExportFilter;
import ru.otus.model.response.ExportResponse;

public interface ItemExportService {

    ExportResponse export(ItemExportFilter filter, int page, int size);
}
