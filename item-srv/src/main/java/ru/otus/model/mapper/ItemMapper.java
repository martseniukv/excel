package ru.otus.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.request.item.ItemAddDto;

@Mapper
public interface ItemMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "hierarchy", ignore = true)
    @Mapping(target = "barcodes", ignore = true)
    @Mapping(target = "priceValues", ignore = true)
    ItemEntity getEntity(ItemAddDto dto);
}
