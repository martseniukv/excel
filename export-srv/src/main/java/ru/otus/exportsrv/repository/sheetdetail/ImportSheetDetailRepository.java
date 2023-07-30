package ru.otus.exportsrv.repository.sheetdetail;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.exportsrv.model.entity.ImportSheetDetailEntity;

public interface ImportSheetDetailRepository extends JpaRepository<ImportSheetDetailEntity, Long> {
}