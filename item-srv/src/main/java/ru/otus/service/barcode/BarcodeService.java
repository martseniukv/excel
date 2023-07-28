package ru.otus.service.barcode;

import ru.otus.model.entity.BarcodeEntity;

import java.util.List;

public interface BarcodeService {

    List<BarcodeEntity> saveBarcodes(List<BarcodeEntity> barcodes);
}
