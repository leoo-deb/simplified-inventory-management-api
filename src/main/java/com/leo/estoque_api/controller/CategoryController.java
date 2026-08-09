package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.category.CategoryRequestDTO;
import com.leo.estoque_api.dto.category.CategoryResponseDTO;
import com.leo.estoque_api.dto.common.PageResponse;
import com.leo.estoque_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponseDTO>> listAllCategory(Pageable pageable) {
        return ResponseEntity.ok(new PageResponse<>(categoryService.listAllCategoriesPage(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findDtoById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> saveCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id,
                                                              @RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.updateCategory(id, categoryRequestDTO);
        return ResponseEntity.ok(categoryResponseDTO);
    }

}
