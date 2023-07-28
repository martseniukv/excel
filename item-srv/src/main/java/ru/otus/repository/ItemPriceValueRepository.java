package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.model.entity.ItemEntity;
import ru.otus.model.entity.ItemPriceValueEntity;

import java.util.Collection;
import java.util.List;

public interface ItemPriceValueRepository extends JpaRepository<ItemPriceValueEntity, Long> {

    @Query("""
            SELECT ipv, pl, c
            FROM ItemPriceValueEntity ipv
            INNER JOIN PriceListEntity pl ON pl.id = ipv.item.id
            INNER JOIN CurrencyEntity c ON c.id = pl.currency.id
            WHERE  ipv.isDeleted = false
              AND  ipv.item.id IN :itemIds
            """)
    List<ItemPriceValueEntity> findByItemIdsNonDeleted(@Param("itemIds") List<Long> itemId);

    @Modifying
    @Query(value =
            """
            UPDATE ItemPriceValueEntity
            SET isDeleted = true
            WHERE item IN (:items)
            """
    )
    void deleteByItems(@Param("items") Collection<ItemEntity> items);
}