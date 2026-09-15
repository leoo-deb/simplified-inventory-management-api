package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.productvariant.ProductVariantMapper;
import com.leo.estoque_api.dto.productvariant.ProductVariantRequestDTO;
import com.leo.estoque_api.dto.productvariant.ProductVariantResponseDTO;
import com.leo.estoque_api.exceptions.BusinessRuleException;
import com.leo.estoque_api.exceptions.ConflictException;
import com.leo.estoque_api.exceptions.ProductNotFoundException;
import com.leo.estoque_api.exceptions.ProductVariantNotFoundException;
import com.leo.estoque_api.infra.storage.StorageS3Service;
import com.leo.estoque_api.model.Product;
import com.leo.estoque_api.model.ProductVariant;
import com.leo.estoque_api.repository.ProductRepository;
import com.leo.estoque_api.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final StorageS3Service storageS3;
    private final ProductVariantRepository variantRepository;
    private final ProductVariantMapper variantMapper;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductVariantResponseDTO> getAllProductVariants(UUID productId, Boolean includeDisabled, Pageable pageable) {
        Page<ProductVariantResponseDTO> variantResponse;

        if (includeDisabled) {
            variantResponse = variantRepository.findAllByProductId(productId, pageable)
                    .map(variantMapper::toProductVariantDTO);
        } else {
            variantResponse = variantRepository.findAllByProductIdAndActiveTrue(productId, pageable)
                    .map(variantMapper::toProductVariantDTO);
        }

        return variantResponse;
    }

    @Transactional
    public ProductVariantResponseDTO createVariant(UUID productId, ProductVariantRequestDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));;

        if (!product.isActive()) {
            throw new BusinessRuleException(
                    String.format("Cannot possible to perform operations on the product " +
                    "with code '%s', as it is unavailable.", productId));
        }

        if (variantRepository.existsBySkuIgnoreCase(dto.sku())) {
            throw new ConflictException(
                    String.format("Product Variant with SKU '%s' already exists", dto.sku()));
        }

        ProductVariant variant = variantMapper.toProductVariant(dto);
        variant.setProduct(product);
        variant.setActive(Boolean.TRUE);

        variant = variantRepository.save(variant);
        return variantMapper.toProductVariantDTO(variant);
    }

    @Transactional
    public void toActivate(UUID productId, UUID variantId) {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        variant.toActive();
    }

    @Transactional
    public void toDeactivate(UUID productId, UUID variantId) {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        variant.toDeactivate();
    }

    @Transactional(readOnly = true)
    public ProductVariantResponseDTO findBySku(UUID productId, String sku) {
        ProductVariant productVariant = variantRepository.findByProductIdAndSkuIgnoreCase(productId, sku)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, sku));
        return variantMapper.toProductVariantDTO(productVariant);
    }

    public ProductVariantResponseDTO findById(UUID productId, UUID variantId) {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        return variantMapper.toProductVariantDTO(variant);
    }

    @Transactional
    public String updateImage(UUID productId, UUID variantId, MultipartFile file)
            throws HttpMediaTypeNotAcceptableException {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        String oldImageUrl = variant.getImageUrl();
        String url = storageS3.replaceFile(oldImageUrl, "products-image", file);

        variant.setImageUrl(url);

        return url;
    }

    @Transactional
    public void removeImage(UUID productId, UUID variantId) {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        if (variant.getImageUrl() == null) {
            throw new BusinessRuleException(
                    String.format("There is no image in the code Product Variant '%s'.",  variantId));
        }

        storageS3.removeFile(variant.getImageUrl());
        variant.setImageUrl(null);
    }

    @Transactional(readOnly = true)
    public String getImageUrl(UUID productId, UUID variantId) {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        if (variant.getImageUrl() == null) {
            throw new BusinessRuleException(
                    String.format("There is no image in the code Product Variant '%s'.",  variantId));
        }

        return variant.getImageUrl();
    }

}
