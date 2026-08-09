package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.product.ProductMapper;
import com.leo.estoque_api.exceptions.ProductNotFoundException;
import com.leo.estoque_api.dto.product.ProductRequestDTO;
import com.leo.estoque_api.dto.product.ProductResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.model.Category;
import com.leo.estoque_api.model.Product;
import com.leo.estoque_api.repository.MovementRepository;
import com.leo.estoque_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    // TODO
    private MovementRepository movementsRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> listAllProductsPage(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toProductDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> listAllProductsByCategory(Long idCategory, Pageable pageable) {
        return productRepository.findAllByCategoryId(idCategory, pageable)
                .map(productMapper::toProductDTO);
    }

    @Transactional
    public ProductResponseDTO registrationProduct(ProductRequestDTO dto) {
        Product product = productMapper.toProduct(dto);
        product.setActive(Boolean.TRUE);

        if (productRepository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessRuleException(String.format("Product with name '%s' already exists.", dto.name()));
        }
        validateProduct(product, dto);

        return productMapper.toProductDTO(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO dto) {
        Product productCurrent = findById(id);

        if (!productCurrent.getName().equalsIgnoreCase(dto.name())
                && productRepository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessRuleException(String.format("Product with name '%s' already exists.", dto.name()));
        }
        validateProduct(productCurrent, dto);

        productMapper.copyProductFromDto(dto, productCurrent);
        return productMapper.toProductDTO(productCurrent);
    }

    @Transactional
    public void toActiveProduct(UUID id) {
        Product product = findById(id);
        product.toActive();
    }

    @Transactional
    public void toInactiveProduct(UUID id) {
        Product product = findById(id);
        product.toInactive();
    }

    public ProductResponseDTO findDtoById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toProductDTO(product);
    }

    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private void validateProduct(Product product, ProductRequestDTO dto) {
        if (!product.isActive()) {
            throw new BusinessRuleException(String.format("Cannot possible to perform operations with the " +
                    "product with code '%s' because it is inactive.", product.getId()));
        }

        Category category = categoryService.findById(dto.categoryId());
        product.setCategory(category);
    }

}
