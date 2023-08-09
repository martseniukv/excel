package ru.otus.exportsrv.service.imports;

import org.springframework.web.multipart.MultipartFile;
import ru.otus.exportsrv.model.ResponseDto;
import ru.otus.exportsrv.model.response.task.ImportTaskDto;

public interface ImportService {

    ResponseDto<ImportTaskDto> documentImport(long importTaskId, MultipartFile multipartFile);
}
