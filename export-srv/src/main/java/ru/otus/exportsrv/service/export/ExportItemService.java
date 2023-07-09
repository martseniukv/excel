package ru.otus.exportsrv.service.export;

import org.springframework.core.io.Resource;
import ru.otus.exportsrv.model.request.item.ExportItemFilter;

public interface ExportItemService {
    Resource exportItem(ExportItemFilter filter);
}
