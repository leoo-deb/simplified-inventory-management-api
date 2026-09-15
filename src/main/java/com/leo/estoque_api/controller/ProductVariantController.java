package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.common.ApiResponseWrapper;
import com.leo.estoque_api.dto.common.PageResponseWrapper;
import com.leo.estoque_api.dto.product.ProductResponseDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantRequestDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.service.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/variants")
@RequiredArgsConstructor
@Tag(name = "Variantes de Produtos", description = "Endpoints para gerenciamento de variantes de produtos")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @Operation(summary = "Criar Variante de Produto",
            description = "Cria uma variante de um produto na aplicação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Variante de produto criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou produto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito ao criar uma variante com SKU iguais")
    })
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ProductVariantResponseDTO>> createVariant(@PathVariable UUID productId,
                                                                                       @Valid @RequestBody ProductVariantRequestDTO variantRequest) {
        try {
            ProductVariantResponseDTO variantResponse =
                    productVariantService.createVariant(productId, variantRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseWrapper.success(variantResponse, "Variant Product created successfully"));
        } catch (EntityNotFoundException e) {
            throw new BusinessRuleException(e.getMessage());
        }
    }

    @Operation(summary = "Listar Variantes por Produto",
            description = "Lista todas as variantes por um ID de produto especifico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<PageResponseWrapper<ProductVariantResponseDTO>> listAllByProduct(@PathVariable UUID productId,
                                                                                           @RequestParam(required = false, defaultValue = "false") Boolean includeDisabled,
                                                                                           Pageable pageable) {
        Page<ProductVariantResponseDTO> productVariantResponseDTOs =
                productVariantService.getAllProductVariants(productId, includeDisabled, pageable);
        return ResponseEntity.ok(new PageResponseWrapper<>(productVariantResponseDTOs));
    }

    @Operation(summary = "Buscar Variante de Produto",
            description = "Recupera uma variante de produto especifico pelo ID com todos os detalhes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Variante encotrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Variante não encontrado")
    })
    @GetMapping("/{variantId}")
    public ResponseEntity<ApiResponseWrapper<ProductVariantResponseDTO>> findVariantById(@PathVariable UUID productId,
                                                                     @PathVariable UUID variantId) {
        ProductVariantResponseDTO variantResponse = productVariantService.findById(productId, variantId);
        return ResponseEntity.ok(ApiResponseWrapper.success(variantResponse, "Variant found successfully"));
    }


    @Operation(summary = "Buscar Variante de Produto por SKU",
            description = "Recupera uma variante de produto especifico pelo SKU com todos os detalhes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Variante encotrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Variante não encontrado")
    })
    @GetMapping("/by-sku")
    public ResponseEntity<ApiResponseWrapper<ProductVariantResponseDTO>> findVariantBySku(@PathVariable UUID productId,
                                                                                   @RequestParam String sku) {
        ProductVariantResponseDTO variantResponse = productVariantService.findBySku(productId, sku);
        return ResponseEntity.ok(ApiResponseWrapper.success(variantResponse, "Variant found successfully"));
    }

    @Operation(summary = "Recuperar URL da imagem",
            description = "Recupera a URL da imagem e redireciona para essa URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagem encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imagem de Variante não encontrado")
    })
    @GetMapping("/{variantId}/image")
    public ResponseEntity<String> getUrlImage(@PathVariable UUID productId,
                                              @PathVariable UUID variantId) {
        String url = productVariantService.getImageUrl(productId, variantId);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .body(url);
    }

    @Operation(summary = "Adicionar imagem em Variante de Produto",
            description = "Adiciona uma imagem em uma variante de produto ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imagem adicionada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Variante não encontrado")
    })
    @PatchMapping("/{variantId}/image")
    public ResponseEntity<String> updateImage(@PathVariable UUID productId,
                                              @PathVariable UUID variantId,
                                              @RequestParam("file") MultipartFile file)
            throws HttpMediaTypeNotAcceptableException {
        String url = productVariantService.updateImage(productId, variantId, file);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .body(url);
    }

    @Operation(summary = "Remover imagem de Variante de Produto",
            description = "Remove a imagem de uma variante de um produto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Imagem removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Imagem de Variante não encontrado")
    })
    @DeleteMapping("/{variantId}/image")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID productId, @PathVariable UUID variantId) {
        productVariantService.removeImage(productId, variantId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Ativar Variante de Produto",
            description = "Ativa uma variante de um produto que esteja desativada")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Variante ativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Variante não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito ao ativar uma variante que já está ativada")
    })
    @PutMapping("/{variantId}")
    public ResponseEntity<Void> activationVariant(@PathVariable UUID productId, @PathVariable UUID variantId) {
        productVariantService.toActivate(productId, variantId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Desativar Variante de Produto",
            description = "Desativa uma variante de um produto que esteja ativada")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Variante desativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Variante não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito ao desativar uma variante que já está desativada")
    })
    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void> deactivationVariant(@PathVariable UUID productId, @PathVariable UUID variantId) {
        productVariantService.toDeactivate(productId, variantId);
        return ResponseEntity
                .noContent()
                .build();
    }

}
