package com.leo.estoque_api.dto.movement;

import com.leo.estoque_api.model.enums.TypeMovement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record MovementRequestDTO(

        @Schema(description = "UUID da variante",
                example = "4505ec1a-d1f4-4ab5-a3b8-19e2b9e6cc5b")
        @NotNull(message = "variantId is required.")
        UUID variantId,

        @Schema(description = "Tipo da movimentação",
                example = "ADJUSTMENT")
        @NotNull(message = "Type is required.")
        TypeMovement type,


        @Schema(description = "Quantidade movimentada",
                example = "70")
        @NotNull(message = "Quantity is required.")
        @Min(value = 1, message = "Cannot record a negative quantity.")
        Long quantity,

        @Schema(description = "Descrição da movimentação",
                example = "Entrada no estoque da variante: 987acdda-a3e7-40b3-b43a-4483fadf26ee")
        String description
) {
}
