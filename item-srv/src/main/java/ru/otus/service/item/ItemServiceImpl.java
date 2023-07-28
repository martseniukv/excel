package ru.otus.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.entity.ItemEntity;
import ru.otus.repository.ItemRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public List<ItemEntity> saveItems(List<ItemEntity> items) {
        return itemRepository.saveAll(items);
    }
}
