package com.leo.estoque_api.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(

        @Schema(description = "Nome da Categoria",
                example = "Domésticos")
        @NotBlank(message = "Nome é obrigatório.")
        String name
) {}
