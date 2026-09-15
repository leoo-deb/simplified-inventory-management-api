package com.leo.estoque_api.dto.productvariant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Dados para uma requisição de variante de produto")
public record ProductVariantRequestDTO(

        @Schema(description = "SKU da variante do produto",
                example = "DOM-AZL-3345")
        @NotNull(message = "SKU is required.")
        String sku,

        @Schema(description = "Modelo ou Marca da variante do produto",
                example = "Azul")
        @NotNull(message = "Model is required.")
        String model,

        @Schema(description = "Quantidade do estoque da variante do produto",
                example = "20")
        @NotNull(message = "Stock's quantity is required.")
        @Positive(message = "You cannot enter a negative quantity.")
        Long stock,

        @Schema(description = "Preço da variante do produto",
                example = "34.90")
        @NotNull(message = "Price is required.")
        @DecimalMin("0.01")
        BigDecimal price,

        @Schema(description = "Observação da variante do produto",
                example = "Variante de cor azul com detalhes listrados em volta")
        String observation
) {
}
