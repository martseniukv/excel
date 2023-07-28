package ru.otus.exportsrv.service.imports.impl;

import com.github.pjfanning.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.concurrent.Computable;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.model.enums.ImportObject;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.request.item.ImportItemBarcodeDto;
import ru.otus.exportsrv.model.request.item.ImportItemPriceDto;
import ru.otus.exportsrv.model.request.item.ItemImportData;
import ru.otus.exportsrv.model.request.item.ItemImportDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportItemBarcodeProcessor;
import ru.otus.exportsrv.service.imports.ImportItemPriceProcessor;
import ru.otus.exportsrv.service.imports.ImportService;
import ru.otus.exportsrv.service.imports.processor.ImportItemProcessor;
import ru.otus.exportsrv.kafka.producer.ImportItemProducer;
import ru.otus.exportsrv.service.task.ImportTaskService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Slf4j
@Service
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final ImportItemProcessor importItemProcessorImpl;
    private final ImportItemBarcodeProcessor importItemBarcodeProcessorImpl;
    private final ImportItemPriceProcessor importItemPriceProcessorImpl;
    private final ImportItemProducer importItemProducer;
    private final ImportTaskService importTaskService;

    public Long documentImport(long importTaskId, MultipartFile multipartFile) {

        if (isNull(multipartFile)) {
            return null;
        }
        ImportTaskDto importTaskDto = importTaskService.getById(importTaskId);

//        if (ImportStatus.CREATED != importTaskDto.getImportStatus()) {
//            throw new IllegalArgumentException(String.format("Import task: %s already processed", importTaskId));
//        }// change 500 error
        Map<String, SheetDetailDto> sheetNameSettingMap = emptyIfNull(importTaskDto.getSheetDetails())
                .stream()
                .collect(Collectors.toMap(SheetDetailDto::getSheetName, Function.identity()));

        try (var workbook = StreamingReader.builder()
                .rowCacheSize(1000)
                .bufferSize(10240)
                .open(multipartFile.getInputStream())
        ) {
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            Future<List<ItemImportDto>> itemFuture = null;
            Future<Map<String, List<ImportItemBarcodeDto>>> barcodeFuture = null;
            Future<Map<String, List<ImportItemPriceDto>>> priceFuture = null;

//            var taskAddDto = ImportTaskAddDto.builder()
//                    .importStatusId(VALIDATE_FILE.getId())
//                    .importTypeId(ImportType.IMPORT_ITEM.getId())
//                    .file(multipartFile.getBytes())
//                    .lineFrom(2)
//                    .lineTo(5000)
//                    .fileName("items")
//                    .startTime(Instant.now())
//                    .build();
//            ImportTaskDto importTaskDto = importTaskService.saveTask(taskAddDto);
            for (Sheet sheet : workbook) {

                SheetDetailDto sheetDetail = sheetNameSettingMap.get(sheet.getSheetName());
                if (nonNull(sheetDetail)) {
                    if (ImportObject.ITEM.equals(sheetDetail.getImportObject())) {
                        itemFuture = executorService.submit(() -> importItemProcessorImpl.process(sheet, sheetDetail));
                    }

                    if (ImportObject.ITEM_BARCODE.equals(sheetDetail.getImportObject())) {
                        barcodeFuture = executorService.submit(() -> importItemBarcodeProcessorImpl.process(sheet, sheetDetail));
                    }
                    if (ImportObject.ITEM_PRICE.equals(sheetDetail.getImportObject())) {
                        priceFuture = executorService.submit(() -> importItemPriceProcessorImpl.process(sheet, sheetDetail));

                    }
                }
            }
            List<ItemImportDto> items = nonNull(itemFuture) ? itemFuture.get() : new ArrayList<>();
            Map<String, List<ImportItemBarcodeDto>> itemBarcodeMap = nonNull(barcodeFuture) ? barcodeFuture.get() : new HashMap<>();
            Map<String, List<ImportItemPriceDto>> itemPriceMap = nonNull(priceFuture) ? priceFuture.get() : new HashMap<>();
            executorService.shutdown();

            for (var item : items) {
                var itemCodeColumn = item.getCode();

                if (nonNull(itemCodeColumn)) {
                    var itemCode = itemCodeColumn.getValue();
                    item.setBarcodes(itemBarcodeMap.getOrDefault(itemCode, new ArrayList<>()));
                    item.setPrices(itemPriceMap.getOrDefault(itemCode, new ArrayList<>()));
                }
            }

            for (var importItems : ListUtils.partition(items, 50_000)) {
                var itemImportData = ItemImportData.builder()
                        .importTaskId(importTaskId)
                        .items(importItems).build();
                importItemProducer.sendMessage(itemImportData);
            }
//            var itemImportData = ItemImportData.builder().importTaskId(importTaskId).items(items).build();
//            importItemProducer.sendMessage(itemImportData);
            return importTaskId;
        } catch (InterruptedException ie) {
            log.error("InterruptedException: ", ie);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
