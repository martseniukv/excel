package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.model.entity.HierarchyEntity;

import java.util.Collection;

public interface HierarchyRepository extends JpaRepository<HierarchyEntity, Long>, JpaSpecificationExecutor<HierarchyEntity> {

    @Query(value =
            """
            SELECT h FROM HierarchyEntity h
            WHERE h.code IN (:codes)
            """
    )
    Collection<HierarchyEntity> getHierarchyEntitiesByCodes(@Param("codes")Collection<String> c);
}
