package ru.otus.exportsrv.service.imports.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import ru.otus.exportsrv.kafka.producer.ImportItemProducer;
import ru.otus.exportsrv.model.ImportItemBarcodeProcessData;
import ru.otus.exportsrv.model.ImportItemPriceProcessData;
import ru.otus.exportsrv.model.ImportItemProcessData;
import ru.otus.exportsrv.model.enums.ImportObject;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.enums.ImportType;
import ru.otus.exportsrv.model.request.item.ImportItemBarcodeDto;
import ru.otus.exportsrv.model.request.item.ImportItemPriceDto;
import ru.otus.exportsrv.model.request.item.ItemImportData;
import ru.otus.exportsrv.model.request.item.ItemImportDto;
import ru.otus.exportsrv.model.request.task.ImportTaskUpdateDto;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportItemBarcodeProcessor;
import ru.otus.exportsrv.service.imports.ImportItemPriceProcessor;
import ru.otus.exportsrv.service.imports.processor.ImportItemProcessor;
import ru.otus.exportsrv.service.task.ImportTaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportItemDocumentImpl implements ImportDocumentProcessor {

    public static final int BATCH_SIZE = 10_000;

    private final ImportItemProcessor importItemProcessorImpl;
    private final ImportItemBarcodeProcessor importItemBarcodeProcessorImpl;
    private final ImportItemPriceProcessor importItemPriceProcessorImpl;
    private final ImportItemProducer importItemProducer;
    private final ImportTaskService importTaskService;

    @Override
    public void importDocument(Workbook workbook, ImportTaskDto importTaskDto) {

        if (isNull(workbook) || isNull(importTaskDto)) {
            log.debug("ImportItemDocumentImpl: request param is null");
            return;
        }
        Long importTaskId = importTaskDto.getId();
        Map<ImportObject, SheetDetailDto> sheetNameSettingMap = emptyIfNull(importTaskDto.getSheetDetails())
                .stream()
                .collect(Collectors.toMap(SheetDetailDto::getImportObject, Function.identity()));

        List<ImportTaskErrorAddDto> errors = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // Use CompletableFuture for asynchronous processing
        var itemSheetDetail = sheetNameSettingMap.get(ImportObject.ITEM);
        var barCodeSheetDetail = sheetNameSettingMap.get(ImportObject.ITEM_BARCODE);
        var priceSheetDetail = sheetNameSettingMap.get(ImportObject.ITEM_PRICE);

        CompletableFuture<ImportItemProcessData> itemFuture = getItemFuture(workbook, executorService, itemSheetDetail);
        CompletableFuture<ImportItemBarcodeProcessData> barcodeFuture = getBarcodeFuture(workbook, executorService, barCodeSheetDetail);
        CompletableFuture<ImportItemPriceProcessData> priceFuture = getPriceFuture(workbook, executorService, priceSheetDetail);

        // Wait for all CompletableFuture to complete
        CompletableFuture.allOf(itemFuture, barcodeFuture, priceFuture).join();

        ImportItemProcessData itemProcessData = itemFuture.join();
        ImportItemBarcodeProcessData barcodeProcessData = barcodeFuture.join();
        ImportItemPriceProcessData priceProcessData = priceFuture.join();

        collectErrors(errors, itemProcessData.getErrors());
        collectErrors(errors, barcodeProcessData.getErrors());
        collectErrors(errors, priceProcessData.getErrors());

        List<ItemImportDto> itemImports = itemProcessData.getItems();
        Map<String, List<ImportItemBarcodeDto>> itemBarcodeMap = barcodeProcessData.getItemBarcodes();
        Map<String, List<ImportItemPriceDto>> itemPriceMap = priceProcessData.getItemPriceMap();

        executorService.shutdown();

        if (!errors.isEmpty()) {
            updateImportTask(importTaskId, errors);
            return;
        }
        for (var item : itemImports) {

            var itemCodeColumn = item.getCode();
            if (nonNull(itemCodeColumn)) {
                var itemCode = itemCodeColumn.getValue();
                item.setBarcodes(itemBarcodeMap.getOrDefault(itemCode, new ArrayList<>()));
                item.setPrices(itemPriceMap.getOrDefault(itemCode, new ArrayList<>()));
            }
        }
        batchSend(importTaskId, itemImports);
    }

    @Override
    public ImportType getType() {
        return ImportType.IMPORT_ITEM;
    }

    private CompletableFuture<ImportItemProcessData> getItemFuture(Workbook workbook,
                                                                   ExecutorService executorService,
                                                                   SheetDetailDto itemSheetDetail) {
        return CompletableFuture.supplyAsync(() -> {
            if (nonNull(itemSheetDetail)) {
                Sheet sheet = workbook.getSheet(itemSheetDetail.getSheetName());
                return nonNull(sheet)
                        ? importItemProcessorImpl.process(sheet, itemSheetDetail)
                        : new ImportItemProcessData();
            }
            return new ImportItemProcessData();
        }, executorService);
    }

    private CompletableFuture<ImportItemPriceProcessData> getPriceFuture(Workbook workbook,
                                                                         ExecutorService executorService,
                                                                         SheetDetailDto priceSheetDetail) {
        return CompletableFuture.supplyAsync(() -> {
            if (nonNull(priceSheetDetail)) {
                Sheet sheet = workbook.getSheet(priceSheetDetail.getSheetName());
                return nonNull(sheet)
                        ? importItemPriceProcessorImpl.process(sheet, priceSheetDetail)
                        : new ImportItemPriceProcessData();
            }
            return new ImportItemPriceProcessData();
        }, executorService);
    }

    private CompletableFuture<ImportItemBarcodeProcessData> getBarcodeFuture(Workbook workbook,
                                                                             ExecutorService executorService,
                                                                             SheetDetailDto barCodeSheetDetail) {
        return CompletableFuture.supplyAsync(() -> {
            if (nonNull(barCodeSheetDetail)) {
                Sheet sheet = workbook.getSheet(barCodeSheetDetail.getSheetName());
                return nonNull(sheet)
                        ? importItemBarcodeProcessorImpl.process(sheet, barCodeSheetDetail)
                        : new ImportItemBarcodeProcessData();
            }
            return new ImportItemBarcodeProcessData();
        }, executorService);
    }

    private void updateImportTask(Long importTaskId, List<ImportTaskErrorAddDto> errors) {

        ImportTaskUpdateDto importTaskUpdateDto = new ImportTaskUpdateDto();
        importTaskUpdateDto.setImportTask(importTaskId);
        importTaskUpdateDto.setErrors(errors);
        importTaskUpdateDto.setStatus(ImportStatus.VALIDATION_ERROR);
        importTaskUpdateDto.setFinished(true);
        importTaskService.updateTask(importTaskUpdateDto);
    }

    private void collectErrors(List<ImportTaskErrorAddDto> errors, List<ImportTaskErrorAddDto> newErrors) {

        if (isNotEmpty(newErrors)) {
            errors.addAll(newErrors);
        }
    }

    private void batchSend(long importTaskId, List<ItemImportDto> items) {

        List<List<ItemImportDto>> partition = ListUtils.partition(items, BATCH_SIZE);
        int size = partition.size();
        for (var importItems : partition) {
            var itemImportData = ItemImportData.builder()
                    .importTaskId(importTaskId)
                    .isLast(size == 1)
                    .items(importItems).build();
            importItemProducer.sendMessage(itemImportData);
            size--;
        }
    }
}