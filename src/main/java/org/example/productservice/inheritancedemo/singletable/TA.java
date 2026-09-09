package org.example.productservice.inheritancedemo.singletable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "st_tas")
@DiscriminatorValue(value = "1")
public class TA extends User {
    private int noOfHrs;
    private Double avgRating;
}
