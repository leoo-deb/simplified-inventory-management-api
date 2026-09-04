package com.leo.estoque_api.dto.photovariant;

import java.util.UUID;

public record PhotoVariantResponseDTO(
        UUID id,
        String name,
        String contentType,
        String url,
        Long size
) {
}
