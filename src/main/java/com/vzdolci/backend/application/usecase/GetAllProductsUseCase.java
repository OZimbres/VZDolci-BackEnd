package com.vzdolci.backend.application.usecase;

import com.vzdolci.backend.application.mapper.ProductMapper;
import com.vzdolci.backend.domain.model.Product;
import com.vzdolci.backend.infrastructure.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllProductsUseCase {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    public GetAllProductsUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
    
    public List<Product> execute() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    public List<Product> executeActiveOnly() {
        return productRepository.findByIsActiveTrue()
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }
}
