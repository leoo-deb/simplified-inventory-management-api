package com.leo.estoque_api.repository;

import com.leo.estoque_api.model.Movement;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query("FROM Movement m join fetch m.productVariant")
    Page<Movement> findAll(@Nonnull Pageable pageable);

    List<Movement> findByUserId(Long id);

}
