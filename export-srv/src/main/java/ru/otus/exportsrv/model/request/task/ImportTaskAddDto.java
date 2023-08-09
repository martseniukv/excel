package ru.otus.exportsrv.model.request.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.request.task.detail.ImportSheetDetailAddDto;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskAddDto {

    private String fileName;
    private Instant startTime;
//    private byte[] file;
    private long importTypeId;
    private long importStatusId;
    private List<ImportSheetDetailAddDto> sheetDetails;
}
