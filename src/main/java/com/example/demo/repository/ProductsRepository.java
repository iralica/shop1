package com.example.demo.repository;

import com.example.demo.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductsRepository extends CrudRepository<Product, Long> {
    // нужны продукты с ценой от ... до ...
    @Query(
            value = "SELECT * FROM PRODUCTS where PRODUCT_PRICE > :priceFrom and PRODUCT_PRICE < :priceTo"
            , nativeQuery = true // native - используется SQL базы данных
    )
    public List<Product> getProductsWithPriceBetween(BigDecimal priceFrom, BigDecimal priceTo);

    // "select p from Product p where p.price > :priceFrom and p.price < :priceTo" // JPQL
    @Query("select p from Product p where p.isActive = :isActive")
    public List<Product> getProductWithStatus(boolean isActive);

    @Query("select p from Product p")
    public List<Product> getAll(Sort sort);

    @Query("select p from Product p ORDER BY p.id")
    Page<Product> getPage(Pageable pageable); // Pageable - какая страница, сколько записей на странице

   @Query("insert into Product (name, price, is active) values (?, ?, ?)")
    public List<Product> saveProduct(Product product);
}


