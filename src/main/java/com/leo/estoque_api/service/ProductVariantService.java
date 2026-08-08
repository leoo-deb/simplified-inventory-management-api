package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.productvariant.ProductVariantMapper;
import com.leo.estoque_api.dto.productvariant.ProductVariantRequestDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.ProductVariantNotFoundException;
import com.leo.estoque_api.model.Product;
import com.leo.estoque_api.model.ProductVariant;
import com.leo.estoque_api.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantMapper productVariantMapper;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public Page<ProductVariantResponseDTO> findAllProductVariants(UUID productId, Pageable pageable) {
        return productVariantRepository.findAllByProductId(productId, pageable)
                .map(productVariantMapper::toProductVariantDTO);
    }

    @Transactional
    public ProductVariantResponseDTO createVariant(UUID productId, ProductVariantRequestDTO productVariantRequestDTO) {
        Product product = productService.findById(productId);

        if (!product.isActive()) {
            throw new BusinessRuleException(String.format("It is not possible to perform operations on the product " +
                    "with code '%s', as it is unavailable.", productId));
        }

        ProductVariant productVariant = productVariantMapper.toProductVariant(productVariantRequestDTO);
        productVariant.setProduct(product);
        ProductVariant productVariantSaved = productVariantRepository.save(productVariant);

        return productVariantMapper.toProductVariantDTO(productVariantSaved);
    }

    public ProductVariantResponseDTO findBySku(String sku) {
        ProductVariant productVariant = productVariantRepository.findBySku(sku)
                .orElseThrow(() -> new ProductVariantNotFoundException(sku));
        return productVariantMapper.toProductVariantDTO(productVariant);
    }

    public ProductVariant findById(UUID id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new ProductVariantNotFoundException(id));
    }

}
