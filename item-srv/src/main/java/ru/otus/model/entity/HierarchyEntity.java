package ru.otus.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hierarchy")
public class HierarchyEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hierarchy_generator")
    @SequenceGenerator(name = "hierarchy_generator", sequenceName = "hierarchy_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "code",unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", length = 50)
    private String name;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private HierarchyEntity parent;

    @OneToMany(mappedBy = "hierarchy", fetch = FetchType.LAZY)
    private List<ItemEntity> items;
}
