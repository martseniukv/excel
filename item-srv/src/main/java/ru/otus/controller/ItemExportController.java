package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.request.item.export.ItemExportFilter;
import ru.otus.model.response.ItemExportResponse;
import ru.otus.service.export.ItemExportService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemExportController {

    private final ItemExportService itemExportServiceImpl;

    @PostMapping("/export/item")
    public List<ItemExportResponse> export(@RequestBody ItemExportFilter filter){
        return itemExportServiceImpl.export(filter);
    }
}
