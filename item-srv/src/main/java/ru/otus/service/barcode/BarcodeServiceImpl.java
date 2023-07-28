package ru.otus.service.barcode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.repository.BarcodeRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarcodeServiceImpl implements BarcodeService{

    private final BarcodeRepository barcodeRepository;

    @Override
    @Transactional
    public List<BarcodeEntity> saveBarcodes(List<BarcodeEntity> barcodes) {
        return barcodeRepository.saveAll(barcodes);
    }
}
