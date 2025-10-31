package com.vzdolci.backend.domain.repository;

import com.vzdolci.backend.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product domain model.
 * This interface defines the contract for data access operations on products.
 * Following the Dependency Inversion Principle, the domain layer defines this interface
 * and the infrastructure layer provides the implementation.
 */
public interface ProductRepository {
    
    /**
     * Find all products
     * @return List of all products
     */
    List<Product> findAll();
    
    /**
     * Find all active products
     * @return List of active products
     */
    List<Product> findActiveProducts();
    
    /**
     * Find a product by its ID
     * @param id The product ID
     * @return Optional containing the product if found
     */
    Optional<Product> findById(Long id);
    
    /**
     * Find a product by its slug
     * @param slug The product slug
     * @return Optional containing the product if found
     */
    Optional<Product> findBySlug(String slug);
    
    /**
     * Save a product
     * @param product The product to save
     * @return The saved product
     */
    Product save(Product product);
    
    /**
     * Delete a product by ID
     * @param id The product ID
     */
    void deleteById(Long id);
}
