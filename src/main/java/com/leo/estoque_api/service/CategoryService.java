package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.category.CategoryMapper;
import com.leo.estoque_api.dto.category.CategoryRequestDTO;
import com.leo.estoque_api.dto.category.CategoryResponseDTO;
import com.leo.estoque_api.dto.product.ProductMapper;
import com.leo.estoque_api.dto.product.ProductResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.CategoryNotFoundException;
import com.leo.estoque_api.exceptions.ConflictException;
import com.leo.estoque_api.model.Category;
import com.leo.estoque_api.model.Product;
import com.leo.estoque_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> getAllCategoriesPage(Pageable pageable) {
        return categoryRepository.findAllByActiveTrue(pageable)
                .map(categoryMapper::toCategoryDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProductsByCategoryPage(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        List<Product> products = category.getProducts();
        Page<Product> productsPage = new PageImpl<>(products);

        return productsPage.map(productMapper::toProductDTO);
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Optional<Category> categoryExists = categoryRepository.findByNameAndActiveTrueIgnoreCase(dto.name());

        if (categoryExists.isPresent()) {
            throw new ConflictException(String.format("Category with name: '%s' already exists", dto.name()));
        }

        Category category = categoryMapper.toCategory(dto);
        category.setActive(true);
        return categoryMapper.toCategoryDTO(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!category.getName().equalsIgnoreCase(dto.name())
                && categoryRepository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException(String.format("Category with name: '%s' already exists.", dto.name()));
        }

        categoryMapper.copyCategoryFromDto(dto, category);
        return categoryMapper.toCategoryDTO(category);
    }

    // Soft Delete
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if  (category.getActive() == false) {
            throw new BusinessRuleException(String.format("Category with id: '%s' is already deactivate.", id));
        }

        category.setActive(false);
    }

    public CategoryResponseDTO findDtoById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.toCategoryDTO(category);
    }

}
