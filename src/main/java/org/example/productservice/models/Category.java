package org.example.productservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "categories")
public class Category extends BaseModel{
    private String title;
}
