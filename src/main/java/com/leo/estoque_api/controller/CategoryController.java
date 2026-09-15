package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.category.CategoryRequestDTO;
import com.leo.estoque_api.dto.category.CategoryResponseDTO;
import com.leo.estoque_api.dto.common.ApiResponseWrapper;
import com.leo.estoque_api.dto.common.PageResponseWrapper;
import com.leo.estoque_api.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Criar categoria",
            description = "Cria uma nova categoria na aplicação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Caregoria criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito ao criar categoria com nomes iguais")
    })
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<CategoryResponseDTO>> createCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.create(categoryRequestDTO);
        ApiResponseWrapper<CategoryResponseDTO> apiResponseWrapper =
                ApiResponseWrapper.success(categoryResponseDTO, "Category successfully created");

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseWrapper);
    }

    @Operation(summary = "Listar Categorias",
            description = "Lista todas as categorias com paginação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    @GetMapping
    public ResponseEntity<PageResponseWrapper<CategoryResponseDTO>> listAllCategory(Pageable pageable) {
        Page<CategoryResponseDTO> categoryResponsePage = categoryService.getAllCategoriesPage(pageable);
        return ResponseEntity.ok(new PageResponseWrapper<>(categoryResponsePage));
    }

    @Operation(summary = "Buscar Categoria",
            description = "Recupera uma categoria especifica pelo ID com todos os detalhes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<CategoryResponseDTO>> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO categoryResponse = categoryService.findDtoById(id);
        return ResponseEntity.ok(ApiResponseWrapper.success(categoryResponse, "Category successfully found"));
    }

    @Operation(summary = "Atualizar Categoria",
            description = "Atualiza todos os dados de uma categoria especifica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito ao atualizar categoria com nomes iguais")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<CategoryResponseDTO>> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.update(id, categoryRequestDTO);
        ApiResponseWrapper<CategoryResponseDTO> apiResponseWrapper = ApiResponseWrapper.success(categoryResponseDTO, "Category successfully updated");

        return ResponseEntity.ok(apiResponseWrapper);
    }

    @Operation(summary = "Desativar Categoria",
            description = "Realiza desativação da da categoria (soft delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria desativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
