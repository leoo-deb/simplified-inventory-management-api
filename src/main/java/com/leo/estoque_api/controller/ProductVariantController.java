package com.leo.estoque_api.controller;

import com.amazonaws.Response;
import com.leo.estoque_api.dto.common.PageResponse;
import com.leo.estoque_api.dto.productvariant.ProductVariantRequestDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantResponseDTO;
import com.leo.estoque_api.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/variants")
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
    public ResponseEntity<PageResponse<ProductVariantResponseDTO>> listAllByProduct(@PathVariable UUID productId,
                                                                                    @RequestParam(required = false, defaultValue = "false") Boolean includeDisabled,
                                                                                    Pageable pageable) {
        Page<ProductVariantResponseDTO> productVariantResponseDTOs =
                productVariantService.getAllProductVariants(productId, includeDisabled, pageable);
        return ResponseEntity.ok(new PageResponse<>(productVariantResponseDTOs));
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ProductVariantResponseDTO> findVariantById(@PathVariable UUID productId,
                                                              @PathVariable UUID variantId) {
        ProductVariantResponseDTO variantResponse = productVariantService.findById(productId, variantId);
        return ResponseEntity.ok(variantResponse);
    }

    @GetMapping("/by-sku")
    public ResponseEntity<ProductVariantResponseDTO> findVariantBySku(@PathVariable UUID productId,
                                                               @RequestParam String sku) {
        return ResponseEntity.ok(productVariantService.findBySku(productId, sku));
    }

    @GetMapping("/{variantId}/image")
    public ResponseEntity<String> getUrlImage(@PathVariable UUID productId,
                                           @PathVariable UUID variantId) {
        String url = productVariantService.getImageUrl(productId, variantId);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .body(url);
    }

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

    @DeleteMapping("/{variantId}/image")
    public ResponseEntity<Void>  deleteImage(@PathVariable UUID productId, @PathVariable UUID variantId) {
        productVariantService.removeImage(productId, variantId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<Void> ActivationVariant(@PathVariable UUID productId, @PathVariable UUID variantId) {
        productVariantService.toActivate(productId, variantId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void> deactivationVariant(@PathVariable UUID productId, @PathVariable UUID variantId) {
        productVariantService.toDeactivate(productId, variantId);
        return ResponseEntity
                .noContent()
                .build();
    }

}
