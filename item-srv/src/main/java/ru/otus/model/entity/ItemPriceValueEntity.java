package ru.otus.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item_price_value")
public class ItemPriceValueEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_price_value_generator")
    @SequenceGenerator(name = "item_price_value_generator", sequenceName = "item_price_value_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "price_list_id", nullable = false)
    private PriceListEntity priceList;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;
}
