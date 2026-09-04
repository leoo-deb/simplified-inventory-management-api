package com.leo.estoque_api.dto.photovariant;

import java.io.InputStream;

public record PhotoVariantRequestDTO(
        String name,
        String contentType,
        Long size,
        InputStream inputStream
)
{}
