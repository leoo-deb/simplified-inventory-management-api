package com.leo.estoque_api.dto.productvariant;

import com.leo.estoque_api.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(target = "productVariant.product", ignore = true)
    @Mapping(target = "productVariant.image", ignore = true)
    ProductVariant toProductVariant(ProductVariantRequestDTO productVariantRequestDTO);

    ProductVariantResponseDTO toProductVariantDTO(ProductVariant productVariant);

}
