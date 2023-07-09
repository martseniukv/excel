package ru.otus.service.export;

import ru.otus.model.request.item.export.ItemExportFilter;
import ru.otus.model.response.ItemExportResponse;

import java.util.List;

public interface ItemExportService {

    List<ItemExportResponse> export(ItemExportFilter filter);
}
