package ru.otus.exportsrv.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.exportsrv.model.ResponseDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;
import ru.otus.exportsrv.service.imports.ImportService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemImportController {

    private final ImportService importServiceImpl;

    @PostMapping(value = "/import/{importTaskId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto<ImportTaskDto>> importItems(@PathVariable("importTaskId") long importTaskId,
                                                                  @RequestPart("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(importServiceImpl.documentImport(importTaskId, multipartFile));
    }
}
