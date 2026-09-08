package com.leo.estoque_api.dto.productvariant;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductVariantResponseDTO(
        UUID id,
        String imageUrl,
        String sku,
        String model,
        Long stock,
        BigDecimal price,
        String observation
) {
}
