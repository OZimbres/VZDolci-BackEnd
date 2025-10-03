package com.vzdolci.backend.infrastructure.persistence.repository;

import com.vzdolci.backend.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for ProductEntity.
 * This is the Spring Data JPA interface for database operations.
 */
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    
    List<ProductEntity> findByIsActiveTrue();
    
    Optional<ProductEntity> findBySlug(String slug);
}
