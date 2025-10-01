package com.vzdolci.backend.infrastructure.persistence.repository;

import com.vzdolci.backend.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    
    List<ProductEntity> findByIsActiveTrue();
    
    Optional<ProductEntity> findBySlug(String slug);
}
