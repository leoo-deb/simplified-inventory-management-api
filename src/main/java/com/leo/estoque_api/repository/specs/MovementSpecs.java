package com.leo.estoque_api.repository.specs;

import com.leo.estoque_api.dto.movement.MovementFiltersDTO;
import com.leo.estoque_api.model.Movement;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class MovementSpecs {

    public static Specification<Movement> byFilters(MovementFiltersDTO movementFilters) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (movementFilters.variantId() != null) {
                predicates.add(builder.equal(root.get("productVariant").get("id"), movementFilters.variantId()));
            }

            if (movementFilters.userId() != null) {
                predicates.add(builder.equal(root.get("user").get("id"), movementFilters.userId()));
            }

            if (movementFilters.type() != null) {
                predicates.add(builder.equal(root.get("type"), movementFilters.type()));
            }

            if (movementFilters.initialQuantity() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("quantity"), movementFilters.initialQuantity()));
            }

            if (movementFilters.finalQuantity() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("quantity"), movementFilters.finalQuantity()));
            }

            if (movementFilters.startTime() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dateTime"), movementFilters.startTime()));
            }

            if (movementFilters.endTime() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dateTime"), movementFilters.endTime()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
