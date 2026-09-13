package org.example.productservice.service;

import org.example.productservice.exceptions.ProductNotFoundException;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.example.productservice.repositories.CategoryRepository;
import org.example.productservice.repositories.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("selfProductService")
//@Primary
public class SelfProductService implements ProductService{
    // To connect and work with DB, you can use productRepository.
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException(productId);
        }
        return optionalProduct.get();
    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product deleteProduct(Long id) {
        return null;
    }



    @Override
    public Product createProduct(Product product) {
        Category category = product.getCategory();
        Optional<Category> optionalCategory = categoryRepository.findByTitle(category.getTitle());

        List<Product> existingProducts = productRepository.findByTitleContainsIgnoreCase(product.getTitle().trim());
        if(!existingProducts.isEmpty()){
            throw new RuntimeException("Product with title " + product.getTitle() + " already exists.");
        }

        Category managedCategory = optionalCategory.orElseGet(() -> categoryRepository.save(category));
        product.setCategory(managedCategory);

        if(product.getId() != null && productRepository.findById(product.getId()).isPresent()){
            throw new RuntimeException("Product with id " + product.getId() + " already exists");
        }
        return productRepository.save(product);
    }

    public List<Product> getProductByTitle(String title){
        return productRepository.findByTitle(title);
    }

}
