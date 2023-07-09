package ru.otus.exportsrv.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.exportsrv.model.request.item.ExportItemFilter;
import ru.otus.exportsrv.service.export.ExportItemService;

import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemExportController {

    private static final String DEFAULT_FILE_NAME = "ItemExport.xlsx";
    private static final String FILENAME_FORMAT = "attachment: filename=%s";

    private final ExportItemService exportItemService;

    /**
     * Экспортирует элементы с использованием заданного фильтра.
     *
     * @param filter фильтр для экспорта элементов
     * @return ответ с ресурсом, содержащим экспортированные элементы, или пустой ответ, если ресурс отсутствует
     */
    @PostMapping(value = "/item")
    public ResponseEntity<Resource> exportItem(@RequestBody ExportItemFilter filter){
        return getResourceResponseEntity(exportItemService.exportItem(filter));
    }


    private ResponseEntity<Resource> getResourceResponseEntity(Resource resource) {
        if (nonNull(resource)) {
            var headers = new HttpHeaders();
            headers.add(CONTENT_DISPOSITION, String.format(FILENAME_FORMAT, DEFAULT_FILE_NAME));
            headers.add(CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(PRAGMA, "no-cache");
            headers.add(EXPIRES, "0");
            try {
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .contentType(APPLICATION_OCTET_STREAM)
                        .body(resource);
            } catch (IOException e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(NO_CONTENT).build();
        }
    }
}
