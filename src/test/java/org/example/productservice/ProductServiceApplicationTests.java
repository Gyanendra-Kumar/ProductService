package org.example.productservice;

import org.example.productservice.models.Product;
import org.example.productservice.projections.ProductWithTitleAndPrice;
import org.example.productservice.repositories.ProductRepository;
import org.example.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductServiceApplicationTests {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testQuery(){
        List<ProductWithTitleAndPrice> productWithTitleAndPrices = productRepository.findTitleAndPriceById();

        for(ProductWithTitleAndPrice productWithTitleAndPrice : productWithTitleAndPrices){
            System.out.println(productWithTitleAndPrice.getTitle() + " - " + productWithTitleAndPrice.getPrice());
        }

        Optional<Product> optionalProduct =  productRepository.findByCategory_Title("mobile");
        System.out.println(optionalProduct.get().getPrice());
    }
}
