package com.vzdolci.backend.web.controller;

import com.vzdolci.backend.application.dto.ProductResponse;
import com.vzdolci.backend.application.usecase.GetAllProductsUseCase;
import com.vzdolci.backend.application.usecase.GetProductByIdUseCase;
import com.vzdolci.backend.domain.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Product endpoints.
 * Controllers in the web layer handle HTTP requests and delegate to use cases.
 * They are kept thin, focusing only on HTTP concerns.
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    
    public ProductController(GetAllProductsUseCase getAllProductsUseCase, 
                           GetProductByIdUseCase getProductByIdUseCase) {
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
    }
    
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        List<Product> products = activeOnly 
            ? getAllProductsUseCase.executeActiveOnly()
            : getAllProductsUseCase.execute();
        
        List<ProductResponse> response = products.stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = getProductByIdUseCase.execute(id);
        ProductResponse response = ProductResponse.fromDomain(product);
        return ResponseEntity.ok(response);
    }
}
