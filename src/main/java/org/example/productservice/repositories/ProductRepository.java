package org.example.productservice.repositories;

import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.example.productservice.projections.ProductWithTitleAndPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Declared queries
    Optional<Product> findById(Long id);

    List<Product> findAll();


    // select * from products where title=""
    List<Product> findByTitle(String title);

    // select * from products where lower(title) LIKE '%str%'
    List<Product> findByTitleContainsIgnoreCase(String str);

    // select * from products where price >= start and price <= end;
    List<Product> findByPriceBetween(Double start, Double end);

    List<Product> findByTitleIgnoreCaseAndPriceBetween(String title, Date start, Date end);

    List<Product> findByCreatedAtBetween(Date start, Date end);

    void deleteById(Long id);

    // Done till now - CRUD: Read and Delete
    Product save(Product product);

    // HQL - Hibernate Query Language
    // Query:- Find the title and price of the product with id = 10;
    // select title, price from product where id=10;

//    @Query(value = "SELECT p.title, p.price FROM products p WHERE p.id=302", nativeQuery = true)
@Query(value = "SELECT p.title, p.price FROM Product p WHERE p.id=302")
    List<ProductWithTitleAndPrice> findTitleAndPriceById();

    Optional<Product> findByCategory(Category category);

    Optional<Product> findByCategory_Title(String title);
}
