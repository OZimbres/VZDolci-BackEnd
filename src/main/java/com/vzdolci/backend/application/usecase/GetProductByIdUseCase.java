package com.vzdolci.backend.application.usecase;

import com.vzdolci.backend.application.exception.NotFoundException;
import com.vzdolci.backend.application.mapper.ProductMapper;
import com.vzdolci.backend.domain.model.Product;
import com.vzdolci.backend.infrastructure.persistence.entity.ProductEntity;
import com.vzdolci.backend.infrastructure.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class GetProductByIdUseCase {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    public GetProductByIdUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
    
    public Product execute(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        
        return productMapper.toDomain(entity);
    }
}
