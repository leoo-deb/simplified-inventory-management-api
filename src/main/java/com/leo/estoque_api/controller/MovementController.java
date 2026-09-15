package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.common.PageResponse;
import com.leo.estoque_api.dto.movement.MovementFiltersDTO;
import com.leo.estoque_api.dto.movement.MovementRequestDTO;
import com.leo.estoque_api.dto.movement.MovementResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.ProductVariantNotFoundException;
import com.leo.estoque_api.service.MovementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    @Autowired
    private MovementService movementService;

    @PostMapping
    public ResponseEntity<MovementResponseDTO> registerMovement(@Valid @RequestBody MovementRequestDTO dto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(movementService.register(dto));
        } catch (ProductVariantNotFoundException e) {
            throw new BusinessRuleException(e.getMessage(), e);
        }
    }

    @GetMapping
    public ResponseEntity<PageResponse<MovementResponseDTO>> listAllMovements(Pageable pageable, MovementFiltersDTO movementFilters) {
        Page<MovementResponseDTO> movementResponsePage = movementService.getAllMovements(pageable, movementFilters);
        return ResponseEntity.ok(new PageResponse<>(movementResponsePage));
    }

}
