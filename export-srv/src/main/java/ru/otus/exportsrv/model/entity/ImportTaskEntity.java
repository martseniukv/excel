package ru.otus.exportsrv.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import ru.otus.exportsrv.model.enums.ImportStatus;
import ru.otus.exportsrv.model.enums.ImportType;

import java.sql.Types;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "import_task")
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "import_task_generator")
    @SequenceGenerator(name = "import_task_generator", sequenceName = "import_task_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "start_time")
    private Instant startTime;

    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column(name = "file", columnDefinition = "bytea")
    private byte[] file;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_type", nullable = false)
    private ImportType importType;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_status", nullable = false)
    private ImportStatus importStatus;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "importTask", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ImportTaskErrorEntity> errors;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "importTask", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ImportSheetDetailEntity> sheetDetails;
}
