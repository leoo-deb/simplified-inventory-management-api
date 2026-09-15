package com.leo.estoque_api.repository;

import com.leo.estoque_api.model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

    @Query("FROM ProductVariant v JOIN FETCH v.product p JOIN FETCH p.category WHERE p.id = :productId")
    Page<ProductVariant> findAllByProductId(UUID productId, Pageable pageable);

    Page<ProductVariant> findAllByProductIdAndActiveTrue(UUID productId, Pageable pageable);

    @Query("FROM ProductVariant v WHERE v.product.id = :productId AND v.id = :variantId")
    Optional<ProductVariant> findById(UUID productId, UUID variantId);

    Optional<ProductVariant> findByProductIdAndSkuIgnoreCase(UUID productId, String sku);

    boolean existsBySkuIgnoreCase(String sku);

}
