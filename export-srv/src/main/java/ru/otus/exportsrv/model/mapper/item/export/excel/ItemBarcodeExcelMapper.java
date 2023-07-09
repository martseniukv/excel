package ru.otus.exportsrv.model.mapper.item.export.excel;

import org.apache.poi.ss.usermodel.Sheet;
import ru.otus.exportsrv.model.domain.item.export.BarcodeExport;

import java.util.List;

public interface ItemBarcodeExcelMapper {

    void map(Sheet sheet, List<BarcodeExport> barcodes);
}
