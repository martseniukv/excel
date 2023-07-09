package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.model.entity.ItemPriceValue;

import java.util.List;

public interface ItemPriceValueValue extends JpaRepository<ItemPriceValue, Long> {

    @Query("""
            SELECT ipv, pl, c FROM ItemPriceValue ipv
            INNER JOIN PriceListEntity pl ON pl.id = ipv.item.id
            INNER JOIN CurrencyEntity c ON c.id = pl.currency.id
            WHERE  ipv.isDeleted = false
              AND  ipv.item.id IN :itemIds
            """)
    List<ItemPriceValue> findByItemIdsNonDeleted(@Param("itemIds") List<Long> itemId);
}
