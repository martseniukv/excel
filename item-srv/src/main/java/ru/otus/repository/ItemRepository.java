package ru.otus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.otus.model.entity.ItemEntity;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>, JpaSpecificationExecutor<ItemEntity> {

    @NonNull
    @Override
    Page<ItemEntity> findAll(@NonNull Specification<ItemEntity> specification, Pageable pageable);

    @Query(value =
            """
            SELECT i FROM ItemEntity i
            WHERE i.code IN (:codes)
            """
    )
    Collection<ItemEntity> getItemEntitiesByCodes(Collection<String> codes);
}
