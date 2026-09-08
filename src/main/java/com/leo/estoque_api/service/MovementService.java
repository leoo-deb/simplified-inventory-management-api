package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.movement.MovementFiltersDTO;
import com.leo.estoque_api.dto.movement.MovementMapper;
import com.leo.estoque_api.dto.movement.MovementRequestDTO;
import com.leo.estoque_api.dto.movement.MovementResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.ProductVariantNotFoundException;
import com.leo.estoque_api.model.Movement;
import com.leo.estoque_api.model.ProductVariant;
import com.leo.estoque_api.repository.MovementRepository;
import com.leo.estoque_api.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.leo.estoque_api.repository.specs.MovementSpecs.byFilters;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final MovementRepository movementRepository;
    private final ProductVariantRepository productRepository;
    private final MovementMapper movementMapper;

    @Transactional(readOnly = true)
    public Page<MovementResponseDTO> getAllMovements(Pageable pageable, MovementFiltersDTO movementFilters) {
        return movementRepository
                .findAll(byFilters(movementFilters), pageable)
                .map(movementMapper::toMovementDTO);
    }

    @Transactional
    public MovementResponseDTO register(MovementRequestDTO dto) {
        ProductVariant productVariant = productRepository.findById(dto.variantId())
                .orElseThrow(() -> new ProductVariantNotFoundException(dto.variantId()));

        Movement movement = movementMapper.toMovement(dto);
        Long stockCurrent = productVariant.getStock();

        switch (dto.type()) {
            case ENTRY -> productVariant.setStock(stockCurrent + dto.quantity());
            case EXIT, LOSS -> {
                validateStock(productVariant, dto);
                productVariant.setStock(stockCurrent - dto.quantity());
            }
            case ADJUSTMENT -> productVariant.setStock(dto.quantity());
        }

        movement.setOldStock(stockCurrent);
        movement.setNewStock(productVariant.getStock());
        movement.setProductVariant(productVariant);

        return movementMapper.toMovementDTO(movementRepository.save(movement));
    }

    private void validateStock(ProductVariant productVariant, MovementRequestDTO dto) {
        if (productVariant.getStock() < dto.quantity()) {
            throw new BusinessRuleException("Insufficient stock quantity.");
        }
    }

}
