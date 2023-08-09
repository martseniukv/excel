package ru.otus.exportsrv.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.otus.exportsrv.model.enums.ImportObject;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "import_sheet_detail")
@NoArgsConstructor
@AllArgsConstructor
public class ImportSheetDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "import_sheet_detail_generator")
    @SequenceGenerator(name = "import_sheet_detail_generator", sequenceName = "import_sheet_detail_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "sheet_name", nullable = false)
    private String sheetName;

    @Column(name = "line_from", nullable = false)
    private int lineFrom;

    @Column(name = "line_to", nullable = false)
    private int lineTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_task_id")
    private ImportTaskEntity importTask;

    @Enumerated(EnumType.STRING)
    @Column(name = "import_object", nullable = false)
    private ImportObject importObject;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "sheetDetail", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ImportTaskErrorEntity> errors;
}
