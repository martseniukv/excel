package ru.otus.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item_barcode")
public class BarcodeEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barcode_generator")
    @SequenceGenerator(name = "barcode_generator", sequenceName = "item_barcode_seq", allocationSize = 1)
    private Long id;

    @Column(name = "barcode", unique = true, nullable = false, length = 50)
    private String barcode;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;
}
