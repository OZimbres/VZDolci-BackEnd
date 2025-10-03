package com.vzdolci.backend.application.usecase;

import com.vzdolci.backend.domain.model.Product;
import com.vzdolci.backend.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for retrieving all products.
 * This service depends on the domain repository interface, not on infrastructure.
 * Follows the Dependency Inversion Principle - depends on abstraction, not concretions.
 */
@Service
public class GetAllProductsUseCase {
    
    private final ProductRepository productRepository;
    
    public GetAllProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<Product> execute() {
        return productRepository.findAll();
    }
    
    public List<Product> executeActiveOnly() {
        return productRepository.findActiveProducts();
    }
}
