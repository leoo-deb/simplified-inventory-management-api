package com.leo.estoque_api.dto.movement;

import com.leo.estoque_api.model.enums.TypeMovements;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MovementFiltersDTO(
        UUID variantId,
        UUID userId,
        TypeMovements type,
        Long initialQuantity,
        Long finalQuantity,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {}
