package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.product.ProductFilters;
import com.leo.estoque_api.dto.product.ProductMapper;
import com.leo.estoque_api.dto.product.ProductRequestDTO;
import com.leo.estoque_api.dto.product.ProductResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.CategoryNotFoundException;
import com.leo.estoque_api.exceptions.ConflictException;
import com.leo.estoque_api.exceptions.ProductNotFoundException;
import com.leo.estoque_api.model.Category;
import com.leo.estoque_api.model.Product;
import com.leo.estoque_api.repository.CategoryRepository;
import com.leo.estoque_api.repository.MovementRepository;
import com.leo.estoque_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.leo.estoque_api.repository.specs.ProductSpecs.byFilters;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    // TODO
    private MovementRepository movementsRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable,
                                                   ProductFilters filters) {
        return productRepository.findAll(byFilters(filters), pageable)
                .map(productMapper::toProductDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProductsByCategory(Long idCategory, Pageable pageable) {
        return productRepository.findAllByCategoryId(idCategory, pageable)
                .map(productMapper::toProductDTO);
    }

    @Transactional
    public ProductResponseDTO registration(ProductRequestDTO dto) {
        Product product = productMapper.toProduct(dto);
        product.setActive(Boolean.TRUE);

        if (productRepository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException(String.format("Product with name '%s' already exists.", dto.name()));
        }

        validateProduct(product, dto);

        Product saved = productRepository.save(product);

        return productMapper.toProductDTO(saved);
    }

    @Transactional
    public ProductResponseDTO update(UUID id, ProductRequestDTO dto) {
        Product productCurrent = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!productCurrent.getName().equalsIgnoreCase(dto.name())
                && productRepository.existsByNameIgnoreCase(dto.name())) {
            throw new ConflictException(String.format("Product with name '%s' already exists.", dto.name()));
        }
        validateProduct(productCurrent, dto);

        productMapper.copyProductFromDto(dto, productCurrent);
        return productMapper.toProductDTO(productCurrent);
    }

    @Transactional
    public void toActive(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.toActive();
    }

    @Transactional
    public void toDeactivate(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.toDeactivate();

        product.getVariants()
                .forEach(v -> v.setActive(false));
    }

    public ProductResponseDTO findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return productMapper.toProductDTO(product);
    }

    private void validateProduct(Product product, ProductRequestDTO dto) {
        if (!product.isActive()) {
            throw new BusinessRuleException(String.format("Cannot possible to perform operations with the " +
                    "product with code '%s' because it is inactive.", product.getId()));
        }

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId()));

        if (!category.isActive()) {
            throw new BusinessRuleException(String.format("Cannot possible to perform operations with the " +
                    "category with code '%s' because it is inactive.", category.getId()));
        }

        product.setCategory(category);
    }

}
