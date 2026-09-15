package com.leo.estoque_api.repository.specs;

import com.leo.estoque_api.dto.product.ProductFilters;
import com.leo.estoque_api.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class ProductSpecs {

    public static Specification<Product> byFilters(ProductFilters filters) {
        return (root, query, build) -> {
            if (Product.class.equals(query.getResultType())) {
                root.fetch("category");
            }

            var predicates = new ArrayList<Predicate>();

            if (filters.categoryId() != null) {
                predicates.add(build.equal(root.get("category").get("id"), filters.categoryId()));
            }

            if (filters.name() != null) {
                predicates.add(build.equal(root.get("name"), filters.name()));
            }

            if (filters.startCreateData() != null) {
                predicates.add(build.greaterThanOrEqualTo(root.get("createdAt"), filters.startCreateData()));
            }

            if (filters.endCreateData() != null) {
                predicates.add(build.lessThanOrEqualTo(root.get("createdAt"), filters.endCreateData()));
            }

            return build.and(predicates.toArray(new Predicate[0]));
        };
    }

}
