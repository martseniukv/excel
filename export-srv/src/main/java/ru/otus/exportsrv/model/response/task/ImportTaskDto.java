package ru.otus.exportsrv.model.response.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.enums.ImportType;
import ru.otus.exportsrv.model.response.task.detail.SheetDetailDto;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskDto {

    private Long id;
    private String fileName;
    private Instant startTime;
    private boolean isFinished;

    private ImportType importType;
    private ImportStatus importStatus;
    private List<SheetDetailDto> sheetDetails;


    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean finished) {
        isFinished = finished;
    }
}
