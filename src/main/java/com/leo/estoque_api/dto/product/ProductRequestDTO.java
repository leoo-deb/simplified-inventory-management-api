package com.leo.estoque_api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados necessários para uma requisição de um produto")
public record ProductRequestDTO(

        @Schema(description = "ID da categoria",
                example = "15")
        @NotNull(message = "categoryId is required.")
        Long categoryId,

        @Schema(description = "Nome do produto",
                example = "Camisa de Time Personalizada")
        @NotBlank(message = "Name is required.")
        String name,

        @Schema(description = "Descrição do produto",
                example = "Camisas de time totalmente personalizáveis.")
        @NotBlank(message = "Description is required.")
        String description

) {
}