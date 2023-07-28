package ru.otus.exportsrv.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "import_task_error")
@NoArgsConstructor
@AllArgsConstructor
public class ImportTaskErrorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "import_task_error_generator")
    @SequenceGenerator(name = "import_task_error_generator", sequenceName = "import_task_error_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_task_id")
    private ImportTaskEntity importTask;

    @Column(name = "row_num")
    private int rowNum;

    @Column(name = "column_num")
    private int columnNum;

    @Column(name = "message")
    private String message;
}
