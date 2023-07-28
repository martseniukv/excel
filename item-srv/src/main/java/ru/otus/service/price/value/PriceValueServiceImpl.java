package ru.otus.service.price.value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.entity.ItemPriceValueEntity;
import ru.otus.repository.ItemPriceValueRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceValueServiceImpl implements PriceValueService{

    private final ItemPriceValueRepository itemPriceValueRepository;

    @Override
    @Transactional
    public List<ItemPriceValueEntity> saveItemPriceValues(List<ItemPriceValueEntity> values) {
        return itemPriceValueRepository.saveAll(values);
    }
}
