package com.leo.estoque_api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados de retorno após uma operação com uma categoria")
public record CategoryResponseDTO(
        @Schema(description = "Id atual da categoria")
        UUID id,

        @Schema(description = "Nome da categoria")
        String name,

        @Schema(description = "Indica se a categoria está, no estado atual, ativa")
        Boolean active
) {
}
