package ru.otus.exportsrv.service.imports.impl;

import com.github.pjfanning.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.exception.TaskAlreadyProcessedException;
import ru.otus.exportsrv.model.ImportItemBarcodeProcessData;
import ru.otus.exportsrv.model.ImportItemPriceProcessData;
import ru.otus.exportsrv.model.ImportItemProcessData;
import ru.otus.exportsrv.model.ResponseDto;
import ru.otus.exportsrv.model.enums.ImportObject;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.request.item.ItemImportData;
import ru.otus.exportsrv.model.request.item.ItemImportDto;
import ru.otus.exportsrv.model.request.task.ImportTaskUpdateDto;
import ru.otus.exportsrv.model.request.task.error.ImportTaskErrorAddDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.service.imports.ImportItemBarcodeProcessor;
import ru.otus.exportsrv.service.imports.ImportItemPriceProcessor;
import ru.otus.exportsrv.service.imports.ImportService;
import ru.otus.exportsrv.service.imports.processor.ImportItemProcessor;
import ru.otus.exportsrv.kafka.producer.ImportItemProducer;
import ru.otus.exportsrv.service.task.ImportTaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static ru.otus.exportsrv.utils.factory.ResponseDtoFactory.getResponse;

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

    public ResponseDto<ImportTaskDto> documentImport(long importTaskId, MultipartFile multipartFile) {

        if (isNull(multipartFile)) {
            return getResponse(null);
        }
        ImportTaskDto importTaskDto = importTaskService.getById(importTaskId);

        if (importTaskDto.getIsFinished()) {
            throw new TaskAlreadyProcessedException(String.format("Import task: %s already finished", importTaskId));
        }
        Map<String, SheetDetailDto> sheetNameSettingMap = emptyIfNull(importTaskDto.getSheetDetails())
                .stream()
                .collect(Collectors.toMap(SheetDetailDto::getSheetName, Function.identity()));

        try (var workbook = StreamingReader.builder()
                .rowCacheSize(1000)
                .bufferSize(10240)
                .open(multipartFile.getInputStream())
        ) {
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            Future<ImportItemProcessData> itemFuture = null;
            Future<ImportItemBarcodeProcessData> barcodeFuture = null;
            Future<ImportItemPriceProcessData> priceFuture = null;

            List<ImportTaskErrorAddDto> errors = new ArrayList<>();
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
            var itemProcessData = nonNull(itemFuture) ? itemFuture.get() : new ImportItemProcessData();
            collectErrors(errors, itemProcessData.getErrors());

            var itemBarcodeProcessData = nonNull(barcodeFuture) ? barcodeFuture.get() : new ImportItemBarcodeProcessData();
            collectErrors(errors, itemBarcodeProcessData.getErrors());

            var itemPriceProcessData = nonNull(priceFuture) ? priceFuture.get() : new ImportItemPriceProcessData();
            collectErrors(errors, itemPriceProcessData.getErrors());

            var itemImports = ListUtils.emptyIfNull(itemProcessData.getItems());
            var itemBarcodeMap = MapUtils.emptyIfNull(itemBarcodeProcessData.getItemBarcodes());
            var itemPriceMap = MapUtils.emptyIfNull(itemPriceProcessData.getItemPriceMap());
            executorService.shutdown();

            if (!errors.isEmpty()) {
                ImportTaskUpdateDto importTaskUpdateDto = new ImportTaskUpdateDto();
                importTaskUpdateDto.setImportTask(importTaskId);
                importTaskUpdateDto.setErrors(errors);
                importTaskUpdateDto.setStatus(ImportStatus.VALIDATION_ERROR);
                importTaskUpdateDto.setFinished(true);
                importTaskService.updateTask(importTaskUpdateDto);
                return getResponse(importTaskDto);
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
            return getResponse(importTaskDto);
        } catch (InterruptedException ie) {
            log.error("InterruptedException: ", ie);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return getResponse(importTaskDto);
    }


    private void collectErrors(List<ImportTaskErrorAddDto> errors, List<ImportTaskErrorAddDto> newErrors) {

        if (isNotEmpty(newErrors)) {
            errors.addAll(newErrors);
        }
    }

    private void batchSend(long importTaskId, List<ItemImportDto> items) {

        List<List<ItemImportDto>> partition = ListUtils.partition(items, 10_000);
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
