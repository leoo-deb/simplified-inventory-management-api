package com.leo.estoque_api.dto.movement;

import com.leo.estoque_api.model.enums.TypeMovement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record MovementRequestDTO(
        @NotNull(message = "variantId is required.")
        UUID variantId,

        @NotNull(message = "Type is required.")
        TypeMovement type,

        @NotNull(message = "Quantity is required.")
        @Positive(message = "Cannot record a negative quantity.")
        Long quantity,

        String description
) {
}
