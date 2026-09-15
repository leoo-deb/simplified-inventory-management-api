package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.common.ApiResponseWrapper;
import com.leo.estoque_api.dto.common.PageResponseWrapper;
import com.leo.estoque_api.dto.product.ProductFilters;
import com.leo.estoque_api.dto.product.ProductRequestDTO;
import com.leo.estoque_api.dto.product.ProductResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.CategoryNotFoundException;
import com.leo.estoque_api.service.ProductService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Criar Produto",
            description = "Cria um produto na aplicação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito ao criar produto com nomes iguais")
    })
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ProductResponseDTO>> saveProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO productResponse = productService.registration(productRequestDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponseWrapper.success(productResponse, "Product created successfully"));
        } catch (CategoryNotFoundException e) {
            throw new BusinessRuleException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Listar Produtos",
            description = "Lista todas os produtos com filtros opcionais paginação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    @GetMapping
    public ResponseEntity<PageResponseWrapper<ProductResponseDTO>> listAllProducts(Pageable pageable,
                                                                                   ProductFilters filters) {
        Page<ProductResponseDTO> productResponsePage =
                productService.getAllProducts(pageable, filters);
        return ResponseEntity.ok(new PageResponseWrapper<>(productResponsePage));
    }


    @Operation(summary = "Listar Produto por Categoria",
            description = "Lista todos os produtos pelo ID de uma categoria especifica com paginação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos encontrada com sucesso"),
    })
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<PageResponseWrapper<ProductResponseDTO>> listAllProductsByCategory(@PathVariable Long idCategory,
                                                                                             Pageable pageable) {
        Page<ProductResponseDTO> productResponsePage = productService
                .getAllProductsByCategory(idCategory, pageable);
        return ResponseEntity.ok(new PageResponseWrapper<>(productResponsePage));
    }

    @Operation(summary = "Buscar Produto",
            description = "Recupera um produto especifico pelo ID com todos os detalhes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ProductResponseDTO>> findProductById(@PathVariable UUID id) {
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return ResponseEntity.ok(ApiResponseWrapper.success(productResponseDTO, "Product found successfully"));
    }

    @Operation(summary = "Atualizar Produto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou categoria não encontrada"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito ao atualizar produto com nomes iguais")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ProductResponseDTO>> updateProduct(@PathVariable UUID id,
                                                     @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO productResponseDTO = productService.update(id, productRequestDTO);
            return ResponseEntity.ok(ApiResponseWrapper.success(productResponseDTO, "Product updated successfully"));
        } catch (CategoryNotFoundException e) {
            throw new BusinessRuleException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Ativar Produto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito ao ativar um produto já ativado")
    })
    @PutMapping("/{id}/activation")
    public ResponseEntity<Void> activationProduct(@PathVariable UUID id) {
        productService.toActive(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Desativar Produto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito ao desativar um produto já desativado")
    })
    @DeleteMapping("/{id}/activation")
    public ResponseEntity<Void> deactivationProduct(@PathVariable UUID id) {
        productService.toDeactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
