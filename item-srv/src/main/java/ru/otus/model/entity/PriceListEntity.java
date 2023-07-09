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
@Table(name = "price_list")
public class PriceListEntity extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_list_generator")
    @SequenceGenerator(name = "price_list_generator", sequenceName = "price_list_seq", allocationSize = 1)
    private Long id;

    @Column(name = "code",unique = true, nullable = false, length = 50)
    private String code;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    @Builder.Default
    @OneToMany(mappedBy = "priceList")
    private List<ItemPriceValue> priceValues = new ArrayList<>();
}
