package ru.otus.exportsrv.service.imports.item;

import org.apache.poi.ss.usermodel.Workbook;
import ru.otus.exportsrv.model.enums.ImportType;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;

public interface ImportDocumentProcessor {

    void importDocument(Workbook workbook, ImportTaskDto importTaskDto);

    ImportType getType();
}