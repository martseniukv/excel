package ru.otus.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.YesNoConverter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_barcode")
public class BarcodeEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barcode_generator")
    @SequenceGenerator(name = "barcode_generator", sequenceName = "item_barcode_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "barcode", nullable = false, length = 50)
    private String barcode;

    @Column(name = "description")
    private String description;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;
}
