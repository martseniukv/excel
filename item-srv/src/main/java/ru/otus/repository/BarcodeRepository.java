package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.model.entity.BarcodeEntity;

import java.util.List;

public interface BarcodeRepository extends JpaRepository<BarcodeEntity, Long> {

    @Query("""
            SELECT ib FROM BarcodeEntity ib
            WHERE  ib.isDeleted = false
              AND  ib.item.id IN :itemIds
            
            """)
    List<BarcodeEntity> findByItemIdsNonDeleted(@Param("itemIds") List<Long> itemIds);
}
