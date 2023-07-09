package ru.otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.mapper.ItemMapper;
import ru.otus.model.mapper.ItemMapperImpl;
import ru.otus.model.request.item.ItemAddDto;
import ru.otus.repository.ItemRepository;
import com.google.common.collect.Lists;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl {

    private final ItemRepository itemRepository;
    private final ItemMapper itemRepositoryImpl;

    public ItemEntity saveItem(ItemAddDto addDto){

        ItemEntity entity = itemRepositoryImpl.getEntity(addDto);

//        if (isNotEmpty(addDto.getBarcodeIds())) {
//
//        }

        return  new ItemEntity();
    }
}
