package ru.otus.exportsrv.service.export.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.exception.ExportException;
import ru.otus.exportsrv.model.mapper.item.export.ItemExportMapper;
import ru.otus.exportsrv.model.mapper.item.export.excel.ItemBarcodeExcelMapper;
import ru.otus.exportsrv.model.mapper.item.export.excel.ItemExcelMapper;
import ru.otus.exportsrv.model.mapper.item.export.excel.ItemPriceValueExcelMapper;
import ru.otus.exportsrv.model.request.item.export.ExportItemFilter;
import ru.otus.exportsrv.service.export.ExportItemService;
import ru.otus.exportsrv.service.rest.item.export.ItemExportRestService;

import java.io.ByteArrayOutputStream;

@Slf4j
@Service
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ExportItemServiceImpl implements ExportItemService {

    private static final int ROW_ACCESS_WINDOW_SIZE = 1;
    public static final int SIZE = 50_000;

    private final ItemExcelMapper itemExcelMapperImpl;
    private final ItemExportMapper itemExportMapperImpl;
    private final ItemExportRestService itemExportRestService;
    private final ItemBarcodeExcelMapper itemBarcodeExcelMapperImpl;
    private final ItemPriceValueExcelMapper itemPriceValueExcelMapperImpl;

    @Override
    public Resource exportItem(ExportItemFilter filter) {

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE)) {

            var itemSheet = workbook.createSheet("item");
            var itemBarcodeSheet = workbook.createSheet("itemBarcode");
            var itemPriceSheet = workbook.createSheet("itemPrice");

            var exportItems = itemExportMapperImpl.getExportItems(itemExportRestService.getExportItems(1, SIZE, filter));
            int totalPages = exportItems.getTotalPages();
            int itemRow = 0;
            int barcodeRow = 0;
            int priceRow = 0;
            itemRow = itemExcelMapperImpl.map(itemRow, true, itemSheet, exportItems.getItems());
            barcodeRow = itemBarcodeExcelMapperImpl.map(barcodeRow, true, itemBarcodeSheet, exportItems.getBarcodes());
            priceRow = itemPriceValueExcelMapperImpl.map(priceRow, true, itemPriceSheet, exportItems.getPrices());
            for (int i = 2; i <= totalPages; i++) {

                var items = itemExportMapperImpl.getExportItems(itemExportRestService.getExportItems(i, SIZE, filter));
                itemRow = itemExcelMapperImpl.map(itemRow, false, itemSheet, items.getItems());
                barcodeRow = itemBarcodeExcelMapperImpl.map(barcodeRow, false, itemBarcodeSheet, items.getBarcodes());
                priceRow = itemPriceValueExcelMapperImpl.map(priceRow, false, itemPriceSheet, items.getPrices());

            }
            var outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ExportException();
        }
    }
}
