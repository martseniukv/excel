package ru.otus.exportsrv.model.response.task.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportTypeDto {

    private Long id;
    private String code;
}
