package com.leo.estoque_api.dto.product;

import java.time.OffsetDateTime;

public record ProductFilters(
        Long categoryId,
        String name,
        Boolean status,
        OffsetDateTime startCreateData,
        OffsetDateTime endCreateData
) {
}
