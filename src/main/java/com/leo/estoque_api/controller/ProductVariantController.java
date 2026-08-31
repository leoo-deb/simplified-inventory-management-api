package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.common.PageResponse;
import com.leo.estoque_api.dto.productvariant.ProductVariantRequestDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantResponseDTO;
import com.leo.estoque_api.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products/{productId}/variants")
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<ProductVariantResponseDTO> createVariant(@PathVariable UUID productId,
                                                                   @RequestBody ProductVariantRequestDTO variantRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productVariantService.createVariant(productId, variantRequest));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductVariantResponseDTO>> findAllByProductId(@PathVariable UUID productId,
                                                                                      Pageable pageable) {
        Page<ProductVariantResponseDTO> productVariantResponseDTOs = productVariantService
                .getAllProductVariants(productId, pageable);
        return ResponseEntity.ok(new PageResponse<>(productVariantResponseDTOs));
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ProductVariantResponseDTO> findById(@PathVariable UUID productId,
                                                              @PathVariable UUID variantId) {
        ProductVariantResponseDTO variantResponse = productVariantService.findById(productId, variantId);
        return ResponseEntity.ok(variantResponse);
    }

    @GetMapping("/by-sku")
    public ResponseEntity<ProductVariantResponseDTO> findBySku(@PathVariable UUID productId, @RequestParam String sku) {
        return ResponseEntity.ok(productVariantService.findBySku(productId, sku));
    }

}
