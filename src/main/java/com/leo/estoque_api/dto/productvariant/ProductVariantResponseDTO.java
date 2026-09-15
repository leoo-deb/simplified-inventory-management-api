package com.leo.estoque_api.dto.productvariant;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados de retorno após operação com uma variante de produto")
public record ProductVariantResponseDTO(

        @Schema(description = "UUID da variante",
                example = "987acdda-a3e7-40b3-b43a-4483fadf26ee")
        UUID id,

        @Schema(description = "URL da imagem da variante",
                example = "https://stock-managerment-project.s3.amazonaws.com/987acdda-a3e7-40b3-b43a-4483fadf26ee_Algo.jpeg")
        String imageUrl,

        @Schema(description = "SKU da variante",
                example = "DOM-AZL-234")
        String sku,

        @Schema(description = "Modelo ou Marca da variante",
                example = "Azul")
        String model,

        @Schema(description = "Estoque atual da variante",
                example = "100")
        Long stock,

        @Schema(description = "Preço da variante",
                example = "50.99")
        BigDecimal price,

        @Schema(description = "Indica se a variante está, no estado atual, ativo",
                example = "true")
        Boolean active,

        @Schema(description = "Observação da variante do produto",
                example = "Variante de cor azul com detalhes listrados em volta")
        String observation
) {
}
