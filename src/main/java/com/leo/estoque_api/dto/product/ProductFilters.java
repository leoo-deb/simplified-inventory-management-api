package com.leo.estoque_api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Dados opcionais para fazer pesquisas filtradas em produtos")
public record ProductFilters(

        @Schema(description = "Filtrar por ID de categoria",
                example = "12")
        Long categoryId,

        @Schema(description = "Filtrar por nome do produto",
                example = "Copo Stanley")
        String name,

        @Schema(description = "Filtrar pelo estado atual do produto",
                example = "true")
        Boolean active,

        @Schema(description = "Filtrar por inclusão de somente ativos ou não",
                example = "false")
        Boolean includeDisabled,

        @Schema(description = "",
                example = "2026-09-01T12:00:00")
        OffsetDateTime startCreateData,

        @Schema(description = "Filtrar por data e hora de criação final",
                example = "2026-09-10T12:30:00")
        OffsetDateTime endCreateData
) {
}
