package com.leo.estoque_api.dto.movement;

import com.leo.estoque_api.model.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "user", ignore = true)
    Movement toMovement(MovementRequestDTO movementRequest);

    @Mapping(source = "productVariant.id", target = "variantId")
    MovementResponseDTO toMovementDTO(Movement movement);

    List<MovementResponseDTO> toCollectionMovementDTO(List<Movement> movements);

}
