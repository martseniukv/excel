package ru.otus.exportsrv.service.task.sheetdetail.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.exportsrv.model.mapper.task.detail.ImportTaskSheetDetailMapper;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;
import ru.otus.exportsrv.repository.sheetdetail.ImportSheetDetailRepository;
import ru.otus.exportsrv.service.task.sheetdetail.ImportSheetDetailService;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportSheetDetailServiceImpl implements ImportSheetDetailService {


    private final ImportSheetDetailRepository sheetDetailRepository;
    private final ImportTaskSheetDetailMapper importTaskSheetDetailMapper;

    @Override
    public SheetDetailDto getById(Long id) {
        return sheetDetailRepository.findById(id)
                .map(importTaskSheetDetailMapper::getDto)
                .orElseThrow(() -> new IllegalStateException(String.format("Can not find sheet details with id: %s", id)));
    }

    @Override
    public List<SheetDetailDto> getByIds(Collection<Long> ids) {
        return sheetDetailRepository.findAllById(ids).stream()
                .map(importTaskSheetDetailMapper::getDto)
                .toList();
    }
}