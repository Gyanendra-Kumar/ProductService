package org.example.productservice.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Annotations to generate getter and setters. One more annotation which can do both - @Data
@Getter  // Generate all getter methods
@Setter  // Generate all setter methods
@ToString
public class Product extends BaseModel{
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private Category category;

}
