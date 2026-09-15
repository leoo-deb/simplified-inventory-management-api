package com.leo.estoque_api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para requisição de uma Categoria")
public record CategoryRequestDTO(

        @Schema(description = "Nome da Categoria",
                example = "Domésticos")
        @NotBlank(message = "Name is required.")
        @Size(min = 4, max = 20, message = "Name must contain between 4 and 20 characters.")
        String name
) {}
