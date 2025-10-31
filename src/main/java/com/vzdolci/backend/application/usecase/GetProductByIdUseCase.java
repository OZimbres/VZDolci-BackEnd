package com.vzdolci.backend.application.usecase;

import com.vzdolci.backend.application.exception.NotFoundException;
import com.vzdolci.backend.domain.model.Product;
import com.vzdolci.backend.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

/**
 * Use case for retrieving a product by its ID.
 * This service depends on the domain repository interface, not on infrastructure.
 * Follows the Dependency Inversion Principle - depends on abstraction, not concretions.
 */
@Service
public class GetProductByIdUseCase {
    
    private final ProductRepository productRepository;
    
    public GetProductByIdUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Product execute(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }
}
