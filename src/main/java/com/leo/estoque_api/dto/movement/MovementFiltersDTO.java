package com.leo.estoque_api.dto.movement;

import com.leo.estoque_api.model.enums.TypeMovement;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MovementFiltersDTO(

        @Schema(description = "Filtro por ID de variante",
                example = "987acdda-a3e7-40b3-b43a-4483fadf26ee")
        UUID variantId,

        @Schema(description = "Filtro por ID de usuário",
                example = "4505ec1a-d1f4-4ab5-a3b8-19e2b9e6cc5b")
        UUID userId,

        @Schema(description = "Filtro por tipo de movimentação",
                example = "ENTRY")
        TypeMovement type,

        @Schema(description = "Filtrar por quantidade inicial",
                example = "50")
        Long initialQuantity,

        @Schema(description = "Filtrar por quantidade final",
                example = "25")
        Long finalQuantity,

        @Schema(description = "Filtrar por data e hora inicial",
                example = "2026-06-12T12:38:00")
        OffsetDateTime startTime,

        @Schema(description = "Filtrar por data e hora final",
                example = "2026-07-30T00:40:30")
        OffsetDateTime endTime

) {}
