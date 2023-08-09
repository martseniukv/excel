package ru.otus.service.price.value;

import ru.otus.model.entity.ItemPriceValueEntity;

import java.util.List;

public interface PriceValueService {

    List<ItemPriceValueEntity> saveItemPriceValues(List<ItemPriceValueEntity> values);
}
