package com.vzdolci.backend.application.dto;

import com.vzdolci.backend.domain.model.Product;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Product responses.
 * DTOs in the application layer define the shape of data for APIs.
 */
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String ingredients;
    private String story;
    private String emoji;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description, BigDecimal price, 
                          String ingredients, String story, String emoji) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.story = story;
        this.emoji = emoji;
    }

    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getIngredients(),
            product.getStory(),
            product.getEmoji()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
