package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.common.PageResponse;
import com.leo.estoque_api.dto.photovariant.PhotoVariantRequestDTO;
import com.leo.estoque_api.dto.photovariant.PhotoVariantResponseDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantRequestDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantResponseDTO;
import com.leo.estoque_api.service.PhotoVariantService;
import com.leo.estoque_api.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/products/{productId}/variants")
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private PhotoVariantService photoService;

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

    @PutMapping(path = "{variantId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoVariantResponseDTO> updatePhoto(@PathVariable UUID productId,
                                                               @PathVariable UUID variantId,
                                                               @RequestParam MultipartFile image)
            throws IOException {
        PhotoVariantRequestDTO photoRequest = new PhotoVariantRequestDTO(
                image.getOriginalFilename(),
                image.getContentType(),
                image.getSize(),
                image.getInputStream()
        );

        PhotoVariantResponseDTO photoResponse = photoService.updatePhoto(productId, variantId, photoRequest);

        return ResponseEntity.ok(photoResponse);
    }

}
