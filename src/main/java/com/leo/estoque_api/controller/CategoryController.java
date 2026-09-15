package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.category.CategoryRequestDTO;
import com.leo.estoque_api.dto.category.CategoryResponseDTO;
import com.leo.estoque_api.dto.common.PageResponse;
import com.leo.estoque_api.dto.product.ProductResponseDTO;
import com.leo.estoque_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> saveCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.create(categoryRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryResponseDTO);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponseDTO>> listAllCategory(Pageable pageable) {
        return ResponseEntity.ok(new PageResponse<>(categoryService.getAllCategoriesPage(pageable)));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<PageResponse<ProductResponseDTO>> listAllProductsByCategory(@PathVariable Long id) {
        Page<ProductResponseDTO> productResponse = categoryService.getAllProductsByCategoryPage(id);
        return ResponseEntity.ok(new PageResponse<>(productResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findDtoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id,
                                                              @RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.update(id, categoryRequestDTO);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
