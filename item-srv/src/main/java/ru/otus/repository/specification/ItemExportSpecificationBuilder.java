package ru.otus.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.model.entity.HierarchyEntity;
import ru.otus.model.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemExportSpecificationBuilder {

    private List<Specification<ItemEntity>> specifications = new ArrayList<>();

    public ItemExportSpecificationBuilder withCode(String code) {
        if (code != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("code"), code));
        }
        return this;
    }

    public ItemExportSpecificationBuilder withName(String name) {
        if (name != null) {
            specifications.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("name"), name));
        }
        return this;
    }

    public ItemExportSpecificationBuilder withHierarchyCode(String hierarchyCode) {
        if (hierarchyCode != null) {
            specifications.add((root, query, criteriaBuilder) -> {
                Join<ItemEntity, HierarchyEntity> hierarchy = root.join("hierarchy");
                return criteriaBuilder.equal(hierarchy.get("code"), hierarchyCode);
            });
        }
        return this;
    }

    public ItemExportSpecificationBuilder withBarcode(String barcode) {
        if (barcode != null) {
            specifications.add((root, query, criteriaBuilder) -> {
                root.join("barcodes"); // Join с таблицей "barcodes"
                return criteriaBuilder.equal(root.get("barcodes").get("barcode"), barcode);
            });
        }
        return this;
    }

    public Specification<ItemEntity> build() {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = specifications.stream()
                    .map(specification -> specification.toPredicate(root, query, criteriaBuilder))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.and(predicates);
        };
    }
}
