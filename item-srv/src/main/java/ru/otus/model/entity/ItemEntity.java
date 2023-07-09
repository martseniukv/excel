package ru.otus.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
public class ItemEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_generator")
    @SequenceGenerator(name = "item_generator", sequenceName = "item_seq", allocationSize = 1)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hierarchy_id")
    private HierarchyEntity hierarchy;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<BarcodeEntity> barcodes;

    @Builder.Default
    @OneToMany(mappedBy = "item")
    private List<ItemPriceValue> priceValues = new ArrayList<>();
}
