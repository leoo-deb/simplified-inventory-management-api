package com.leo.estoque_api.repository;

import com.leo.estoque_api.model.PhotoVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhotoVariantRepository extends JpaRepository<PhotoVariant, UUID> {

    @Query("""
    SELECT photo FROM PhotoVariant photo JOIN photo.productVariant v
    WHERE v.id = :variantId AND v.product.id = :productId""")
    Optional<PhotoVariant> findById(UUID productId, UUID variantId);

}
