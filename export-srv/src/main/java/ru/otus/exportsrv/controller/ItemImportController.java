package ru.otus.exportsrv.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.exportsrv.model.request.item.imports.ImportRequestDto;
import ru.otus.exportsrv.model.request.item.imports.ImportSettings;
import ru.otus.exportsrv.service.imports.ImportService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemImportController {

    private final ImportService importServiceImpl;

    @PostMapping(value = "/import/{importTaskId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> importItems(@PathVariable("importTaskId") long importTaskId,
                                            @RequestPart("file") MultipartFile multipartFile) {

//        List<ImportSettings> importSettings = new ArrayList<>();
//        importSettings.add(ImportSettings.builder().sheetName("item").importObjectType(1).build());
//        importSettings.add(ImportSettings.builder().sheetName("itemBarcode").importObjectType(2).build());
//        importSettings.add(ImportSettings.builder().sheetName("itemPrice").importObjectType(3).build());
//        ImportRequestDto importRequestDto = new ImportRequestDto();
//        importRequestDto.setImportSettings(importSettings);
        return ResponseEntity.ok(importServiceImpl.documentImport(importTaskId, multipartFile));
    }

}
