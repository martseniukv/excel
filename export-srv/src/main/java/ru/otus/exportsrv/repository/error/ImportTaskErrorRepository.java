package ru.otus.exportsrv.repository.error;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.exportsrv.model.entity.ImportTaskErrorEntity;

public interface ImportTaskErrorRepository extends JpaRepository<ImportTaskErrorEntity, Long> {


    @Modifying
    @Query(value =
            """
            DELETE FROM ImportTaskErrorEntity ite
            WHERE ite.importTask.id = :importTaskId
            """
    )
    void deleteByImportTaskAndId(@Param("importTaskId") Long importTaskId);
}
