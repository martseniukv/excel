package ru.otus.exportsrv.repository.task;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.exportsrv.model.entity.ImportTaskEntity;

import java.util.Optional;

public interface ImportTaskRepository extends JpaRepository<ImportTaskEntity, Long> {


    Optional<ImportTaskEntity> findById(Long id);
}
