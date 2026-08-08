package com.leo.estoque_api.dto.movement;

import com.leo.estoque_api.model.User;

import java.time.OffsetDateTime;

public record MovementResponseDTO(
        Long id,
        String variantId,
        String type,
        Long quantity,
        Long oldStock,
        Long newStock,
        OffsetDateTime dateTime,
        String description
) {}