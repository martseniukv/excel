package ru.otus.exportsrv.model.request.item.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportRequestDto {

    private List<ImportSettings> importSettings;
}
