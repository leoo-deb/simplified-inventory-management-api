package com.leo.estoque_api.repository;

import com.leo.estoque_api.model.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>, JpaSpecificationExecutor<Movement> {

    @Query("FROM Movement m JOIN FETCH m.productVariant JOIN FETCH m.user")
    Page<Movement> findAll(Pageable pageable);

    Page<Movement> findByUserId(UUID userId, Pageable pageable);

}
