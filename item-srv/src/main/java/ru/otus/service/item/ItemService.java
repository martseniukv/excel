package ru.otus.service.item;

import ru.otus.model.entity.ItemEntity;

import java.util.List;

public interface ItemService {

    List<ItemEntity> saveItems(List<ItemEntity> items);
}
