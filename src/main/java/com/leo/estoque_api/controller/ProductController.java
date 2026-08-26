package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.common.PageResponse;
import com.leo.estoque_api.dto.product.ProductFilters;
import com.leo.estoque_api.dto.product.ProductRequestDTO;
import com.leo.estoque_api.dto.product.ProductResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.CategoryNotFoundException;
import com.leo.estoque_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> save(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO productResponse = productService.registrationProduct(productRequestDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(productResponse);
        } catch (CategoryNotFoundException e) {
            throw new BusinessRuleException(e.getMessage(), e);
        }
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponseDTO>> listAllProducts(Pageable pageable, ProductFilters filters) {
        return ResponseEntity.ok(new PageResponse<>(productService.getAllProducts(pageable, filters)));
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<PageResponse<ProductResponseDTO>> listAllProductsByCategory(@PathVariable Long idCategory,
                                                                                      Pageable pageable) {
        Page<ProductResponseDTO> productResponsePage = productService
                .getAllProductsByCategory(idCategory, pageable);
        return ResponseEntity.ok(new PageResponse<>(productResponsePage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        ProductResponseDTO productResponseDTO = productService.findDtoById(id);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable UUID id,
                                                     @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        try {
            ProductResponseDTO productResponseDTO = productService.updateProduct(id, productRequestDTO);
            return ResponseEntity.ok(productResponseDTO);
        } catch (CategoryNotFoundException e) {
            throw new BusinessRuleException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}/activation")
    public ResponseEntity<Void> activation(@PathVariable UUID id) {
        productService.toActiveProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/activation")
    public ResponseEntity<Void> deactivation(@PathVariable UUID id) {
        productService.toInactiveProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
