package org.example.productservice.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Category extends BaseModel{
    private String title;
}
