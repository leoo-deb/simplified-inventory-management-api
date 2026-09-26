package com.leo.estoque_api.dto.product;

import com.leo.estoque_api.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDTO toProductDTO(Product product);

    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductRequestDTO productRequestDTO);

    void copyProductFromDto(ProductRequestDTO productRequestDTO, @MappingTarget Product product);

}
