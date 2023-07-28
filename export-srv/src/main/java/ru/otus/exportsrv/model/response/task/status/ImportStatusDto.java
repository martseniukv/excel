package ru.otus.exportsrv.model.response.task.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportStatusDto {

    private Long id;
    private String code;
}
