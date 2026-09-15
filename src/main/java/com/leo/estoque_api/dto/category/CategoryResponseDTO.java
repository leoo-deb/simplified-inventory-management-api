package com.leo.estoque_api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponseDTO(
        @Schema(description = "Id atual da categoria")
        Long id,

        @Schema(description = "Nome da categoria")
        String name,

        @Schema(description = "Indica se a categoria está, no estado atual, ativa")
        Boolean active
) {
}
