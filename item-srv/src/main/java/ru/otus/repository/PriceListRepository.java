package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.model.entity.BarcodeEntity;
import ru.otus.model.entity.PriceListEntity;

import java.util.Collection;

public interface PriceListRepository extends JpaRepository<PriceListEntity, Long> {

    @Query(value =
            """
            SELECT pl FROM PriceListEntity pl
            WHERE pl.code IN (:codes)
            """
    )
    Collection<PriceListEntity> getPriceListEntitiesByCodes(@Param("codes")Collection<String> c);
}
