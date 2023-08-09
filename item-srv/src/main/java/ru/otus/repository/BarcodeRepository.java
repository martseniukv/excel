package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.model.entity.ItemEntity;

import java.util.Collection;
import java.util.List;

public interface BarcodeRepository extends JpaRepository<BarcodeEntity, Long> {

    @Query("""
            SELECT ib FROM BarcodeEntity ib
            WHERE  ib.isDeleted = false
              AND  ib.item.id IN :itemIds
            
            """)
    List<BarcodeEntity> findByItemIdsNonDeleted(@Param("itemIds") List<Long> itemIds);

    @Query(value =
            """
            SELECT b FROM BarcodeEntity b
            JOIN FETCH b.item
            WHERE b.barcode IN (:codes)
            """
    )
    Collection<BarcodeEntity> getBarcodeEntitiesByCodes(@Param("codes")Collection<String> c);

    @Modifying
    @Query(value =
            """
            UPDATE BarcodeEntity
            SET isDeleted = true 
            WHERE item IN (:items)
            """
    )
    void deleteByItems(@Param("items") Collection<ItemEntity> items);
}
