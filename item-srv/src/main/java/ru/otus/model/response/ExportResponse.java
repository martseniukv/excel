package ru.otus.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportResponse {

    private int totalPages;
    private int size;
    private long totalElements;
    private List<ItemExportResponse> itemResponse;
}
