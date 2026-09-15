package com.leo.estoque_api.dto.product;

import com.leo.estoque_api.dto.category.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados de retorno após operação com um produto")
public record ProductResponseDTO(

        @Schema(description = "UUID do produto",
                example = "987acdda-a3e7-40b3-b43a-4483fadf26ee")
        UUID id,

        @Schema(description = "Nome do produto",
                example = "Notebook 64gb de RAM")
        String name,

        @Schema(description = "Descrição do produto",
                example = "Notebook gamer de última geração por um preço imperdível")
        String description,

        @Schema(description = "Indica se o produto está, no estado atual, ativo",
                example = "false")
        Boolean active,

        @Schema(description = "ID da categoria",
                example = "23")
        CategoryResponseDTO category
) {
}
