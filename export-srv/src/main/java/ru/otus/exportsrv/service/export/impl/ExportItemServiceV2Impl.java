package ru.otus.exportsrv.service.export.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.exception.ExportException;
import ru.otus.exportsrv.model.mapper.item.export.ItemExportMapper;
import ru.otus.exportsrv.model.mapper.item.export.excel.v2.ItemBarcodeExcelDynamicMapper;
import ru.otus.exportsrv.model.mapper.item.export.excel.v2.ItemExcelDynamicMapper;
import ru.otus.exportsrv.model.mapper.item.export.excel.v2.ItemPriceValueExcelDynamicMapper;
import ru.otus.exportsrv.model.request.item.export.ExportItemFilter;
import ru.otus.exportsrv.service.export.ExportItemService;
import ru.otus.exportsrv.service.rest.item.export.ItemExportRestService;

import java.io.ByteArrayOutputStream;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Slf4j
@Service
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ExportItemServiceV2Impl implements ExportItemService {

    private static final int ROW_ACCESS_WINDOW_SIZE = 1;

    private final ItemExportMapper itemExportMapperImpl;
    private final ItemExportRestService itemExportRestService;
    private final ItemExcelDynamicMapper itemExcelDynamicMapper;
    private final ItemPriceValueExcelDynamicMapper itemPriceValueExcelMapperImpl;
    private final ItemBarcodeExcelDynamicMapper itemBarcodeExcelDynamicMapper;

    @Override
    @SneakyThrows
    public Resource exportItem(ExportItemFilter filter) {

        var exportItems = itemExportMapperImpl.getExportItems(itemExportRestService.getExportItems(filter));
        log.info("Export items: {}", emptyIfNull(exportItems.getItems()).size());
        log.info("Export barcodes: {}", emptyIfNull(exportItems.getBarcodes()).size());
        log.info("Export prices: {}", emptyIfNull(exportItems.getPrices()).size());
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE)) {

            var sheetSettings = filter.getSheetSettings();
            itemExcelDynamicMapper.map(workbook, exportItems.getItems(), sheetSettings);
            itemBarcodeExcelDynamicMapper.map(workbook, exportItems.getBarcodes(), sheetSettings);
            itemPriceValueExcelMapperImpl.map(workbook, exportItems.getPrices(), sheetSettings);

            var outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ExportException();
        }
    }
}