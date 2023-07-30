package ru.otus.exportsrv.service.imports.impl;

import com.github.pjfanning.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.exportsrv.config.AspectLogExecuteTime;
import ru.otus.exportsrv.exception.TaskAlreadyProcessedException;
import ru.otus.exportsrv.model.ResponseDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;
import ru.otus.exportsrv.service.imports.ImportService;
import ru.otus.exportsrv.service.imports.item.ImportDocumentProcessor;
import ru.otus.exportsrv.service.task.ImportTaskService;

import java.util.List;

import static java.util.Objects.isNull;
import static ru.otus.exportsrv.utils.factory.ErrorDtoFactory.getError;
import static ru.otus.exportsrv.utils.factory.ResponseDtoFactory.getFailedResponse;
import static ru.otus.exportsrv.utils.factory.ResponseDtoFactory.getOkResponse;

@Slf4j
@Service
@AspectLogExecuteTime
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final ImportTaskService importTaskService;
    private final List<ImportDocumentProcessor> importDocuments;

    public ResponseDto<ImportTaskDto> documentImport(long importTaskId, MultipartFile multipartFile) {

        if (isNull(multipartFile)) {
            return getOkResponse(null);
        }
        ImportTaskDto importTaskDto = importTaskService.getById(importTaskId);

        var documentProcessor = importDocuments.stream()
                .filter(importDocument -> importDocument.getType().equals(importTaskDto.getImportType()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        if (importTaskDto.getIsFinished()) {
            throw new TaskAlreadyProcessedException(String.format("Import task: %s already finished", importTaskId));
        }
        try (var workbook = StreamingReader.builder()
                .rowCacheSize(1000)
                .bufferSize(10240)
                .open(multipartFile.getInputStream())
        ) {
            documentProcessor.importDocument(workbook, importTaskDto);
            return getOkResponse(importTaskDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            var errorMassage = String.format("Something went wrong during import task: %s", importTaskId);
            return getFailedResponse(List.of(getError(errorMassage)));
        }
    }
}
