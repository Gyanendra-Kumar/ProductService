package org.example.productservice.inheritancedemo.singletable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "st_instructors")
@DiscriminatorValue(value = "3")
public class Instructor extends User {
    private String subject;
}
