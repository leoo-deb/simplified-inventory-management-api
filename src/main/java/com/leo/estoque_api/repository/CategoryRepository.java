package com.leo.estoque_api.repository;

import com.leo.estoque_api.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAllByActiveTrue(Pageable pageable);

    Optional<Category> findByNameAndActiveTrueIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

}
