package com.vzdolci.backend.application.mapper;

import com.vzdolci.backend.domain.model.Product;
import com.vzdolci.backend.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {
    
    public Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        // Convert price_cents (INTEGER) to BigDecimal
        product.setPrice(entity.getPriceCents() != null 
            ? BigDecimal.valueOf(entity.getPriceCents()).movePointLeft(2) 
            : null);
        product.setIngredients(entity.getIngredients());
        product.setStory(entity.getStory());
        product.setEmoji(entity.getEmoji());
        product.setSlug(entity.getSlug());
        product.setIsActive(entity.getIsActive());
        product.setCreatedAt(entity.getCreatedAt());
        product.setUpdatedAt(entity.getUpdatedAt());
        
        return product;
    }
    
    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        // Convert BigDecimal to price_cents (INTEGER)
        entity.setPriceCents(product.getPrice() != null 
            ? product.getPrice().movePointRight(2).intValue() 
            : null);
        entity.setIngredients(product.getIngredients());
        entity.setStory(product.getStory());
        entity.setEmoji(product.getEmoji());
        entity.setSlug(product.getSlug());
        entity.setIsActive(product.getIsActive());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());
        
        return entity;
    }
}
