package com.leo.estoque_api.repository;

import com.leo.estoque_api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("FROM Product p JOIN FETCH p.category")
    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByActiveTrue(Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    Page<Product> findAllByCategoryId(Long id, Pageable pageable);

}
