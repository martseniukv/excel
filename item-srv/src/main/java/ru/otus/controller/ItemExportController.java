package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.config.AspectLogExecuteTime;
import ru.otus.model.request.item.export.ItemExportFilter;
import ru.otus.model.response.ExportResponse;
import ru.otus.service.export.ItemExportService;

@Slf4j
@RestController
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ItemExportController {

    private final ItemExportService itemExportServiceImpl;

    @PostMapping("/export/item")
    public ExportResponse export(@RequestParam(value = "page") int page,
                                 @RequestParam(value = "size") int size,
                                 @RequestBody ItemExportFilter filter) {
        return itemExportServiceImpl.export(filter, page, size);
    }
}
