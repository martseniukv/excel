package ru.otus.exportsrv.service.imports;

import org.springframework.web.multipart.MultipartFile;

public interface ImportService {

    Long documentImport(long importTaskId, MultipartFile multipartFile);
}
