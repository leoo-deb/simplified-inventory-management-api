package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.common.ApiResponseWrapper;
import com.leo.estoque_api.dto.common.PageResponseWrapper;
import com.leo.estoque_api.dto.movement.MovementFiltersDTO;
import com.leo.estoque_api.dto.movement.MovementRequestDTO;
import com.leo.estoque_api.dto.movement.MovementResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.ProductVariantNotFoundException;
import com.leo.estoque_api.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
@Tag(name = "Movimentações", description = "Endpoints para gerenciamento de movimentações dos produtos")
public class MovementController {

    private final MovementService movementService;

    @Operation(summary = "Registrar Movimentação",
            description = "Registra uma nova ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimentação registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou Variante de Produto não encontrada"),
    })
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<MovementResponseDTO>> registerMovement(@Valid @RequestBody MovementRequestDTO dto) {
        try {
            MovementResponseDTO movementResponse = movementService.register(dto);


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponseWrapper.success(movementResponse, "Movement registered successfully"));
        } catch (ProductVariantNotFoundException e) {
            throw new BusinessRuleException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Listar Movimentações",
            description = "Lista todas as movimentações com filtros opcionais e paginação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    @GetMapping
    public ResponseEntity<PageResponseWrapper<MovementResponseDTO>> listAllMovements(Pageable pageable, MovementFiltersDTO movementFilters) {
        Page<MovementResponseDTO> movementResponsePage = movementService.getAllMovements(pageable, movementFilters);
        return ResponseEntity.ok(new PageResponseWrapper<>(movementResponsePage));
    }

}
