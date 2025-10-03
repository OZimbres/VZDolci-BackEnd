package com.vzdolci.backend.infrastructure.persistence.repository;

import com.vzdolci.backend.application.mapper.ProductMapper;
import com.vzdolci.backend.domain.model.Product;
import com.vzdolci.backend.domain.repository.ProductRepository;
import com.vzdolci.backend.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ProductRepository interface.
 * This adapter translates between the domain model (Product) and the persistence model (ProductEntity).
 * Follows the Dependency Inversion Principle - the domain interface is implemented by infrastructure.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductJpaRepository jpaRepository;
    private final ProductMapper productMapper;
    
    public ProductRepositoryImpl(ProductJpaRepository jpaRepository, ProductMapper productMapper) {
        this.jpaRepository = jpaRepository;
        this.productMapper = productMapper;
    }
    
    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findActiveProducts() {
        return jpaRepository.findByIsActiveTrue()
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(productMapper::toDomain);
    }
    
    @Override
    public Optional<Product> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug)
                .map(productMapper::toDomain);
    }
    
    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
