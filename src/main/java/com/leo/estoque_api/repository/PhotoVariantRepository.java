package com.leo.estoque_api.repository;

import com.leo.estoque_api.model.PhotoVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoVariantRepository extends JpaRepository<PhotoVariant, UUID> {

}
