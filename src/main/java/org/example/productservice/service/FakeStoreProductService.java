package org.example.productservice.service;

import lombok.extern.slf4j.Slf4j;
import org.example.productservice.dto.FakeStoreProductDto;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate;
//    private RestClient restClient;

    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
//    public FakeStoreProductService(RestClient restClient){
//        this.restClient = restClient;
//    }

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<FakeStoreProductDto[]> response = restTemplate.getForEntity("https://fakestoreapi.com/products/", FakeStoreProductDto[].class);
        FakeStoreProductDto[] responseBody = response.getBody();

        // 1. Safe check if API returned nothing
        if(responseBody == null){
            return List.of();
        }
        List<Product> products = Arrays.stream(responseBody).map(dto -> convertFakeStoreDtoTOProduct(dto)).toList();

        System.out.println("======== Products start =============");
        System.out.println(products);
        System.out.println("======== Products End ==============");

        return products;
    }

    @Override
    public Product getSingleProduct(Long productId) {
        // make an http call to fakestore api to get the product with the given productId.
        ResponseEntity<FakeStoreProductDto> responseEntity= restTemplate.getForEntity("https://fakestoreapi.com/products/" + productId, FakeStoreProductDto.class);
//        ResponseEntity<FakeStoreProductDto> responseEntity = restClient
//                                                                .get()
//                                                                .uri("https://fakestoreapi.com/products" + productId)
//                                                                .retrieve()
//                                                                .toEntity(FakeStoreProductDto.class);

        FakeStoreProductDto fakeStoreProductDto =  responseEntity.getBody();

        return convertFakeStoreDtoTOProduct(fakeStoreProductDto);
    }

    private Product convertFakeStoreDtoTOProduct(FakeStoreProductDto fakeStoreProductDto){
        if(fakeStoreProductDto == null){
            return null;
        }

        Product product = new Product();
        Category category = new Category();
        category.setTitle(fakeStoreProductDto.getCategory());
        category.setId(fakeStoreProductDto.getId());
        category.setCreatedAt(new Date());
        category.setLastModifiedAt(new Date());

        product.setTitle(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        product.setCategory(category);
        product.setId(fakeStoreProductDto.getId());
        product.setCreatedAt(new Date());
        product.setLastModifiedAt(new Date());
        return product;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product replaceProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product deleteProduct(Long id) {
        return null;
    }
}
