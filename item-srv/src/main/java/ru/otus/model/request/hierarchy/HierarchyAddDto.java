package ru.otus.model.request.hierarchy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HierarchyAddDto {

    private String code;
    private String name;
    private String parentId;
}
