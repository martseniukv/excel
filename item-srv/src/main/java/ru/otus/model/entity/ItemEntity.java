package ru.otus.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

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
    @SequenceGenerator(name = "item_generator", sequenceName = "item_id_seq", allocationSize = 5000)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hierarchy_id")
    private HierarchyEntity hierarchy;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<BarcodeEntity> barcodes;

    @Builder.Default
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "item")
    private List<ItemPriceValueEntity> priceValues = new ArrayList<>();

    public boolean isNew(){
        return isNull(id);
    }
}
