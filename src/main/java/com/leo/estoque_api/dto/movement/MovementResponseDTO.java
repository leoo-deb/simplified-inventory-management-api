package com.leo.estoque_api.dto.movement;

import com.leo.estoque_api.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record MovementResponseDTO(

        @Schema(description = "ID da movimentação",
                example = "12")
        Long id,

        @Schema(description = "UUID da variante movimentada",
                example = "987acdda-a3e7-40b3-b43a-4483fadf26ee")
        String variantId,

        @Schema(description = "Tipo da movimentação",
                example = "EXIT")
        String type,

        @Schema(description = "Quantidade movimentada",
                example = "60")
        Long quantity,

        @Schema(description = "Quantidade do estoque antigo da variante",
                example = "100")
        Long oldStock,

        @Schema(description = "Quantidade do novo estoque da variante",
                example = "40")
        Long newStock,

        @Schema(description = "Data e hora da movimentação",
                example = "2026-09-01T12:03:09")
        OffsetDateTime dateTime,

        @Schema(description = "Descrição da movimentação",
                example = "Movimentação de saída de estoque")
        String description

) {}