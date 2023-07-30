package ru.otus.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.otus.model.entity.ItemEntity;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>, JpaSpecificationExecutor<ItemEntity> {

    @EntityGraph(attributePaths = {"hierarchy"})
    List<ItemEntity> findAll(Specification<ItemEntity> specification);

    @Query(value =
            """
            SELECT i FROM ItemEntity i
            WHERE i.code IN (:codes)
            """
    )
    Collection<ItemEntity> getItemEntitiesByCodes(Collection<String> codes);
}
