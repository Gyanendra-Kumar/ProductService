package org.example.productservice.service;

import org.example.productservice.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getSingleProduct(Long productId);
    Product createProduct(Product product);
    Product replaceProduct(Long productId, Product product);
    Product deleteProduct(Long id);
}
