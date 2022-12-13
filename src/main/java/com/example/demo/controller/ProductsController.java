package com.example.demo.controller;

import com.example.demo.Entity.Product;
import com.example.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductsController<ProductRepository> {
    private ProductsRepository repository;
    private final ProductRepository productRepository;


    @Autowired
    public ProductsController(ProductsRepository repository,
                              ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    // напишите код, который возвратит все продукты по GET запросу
    // на url http://localhost:8080/all
    // до 20:52
    @GetMapping
    public List<Product> getAll()
    {
        List<Product> products = new ArrayList<>();

        repository.findAll().forEach(products::add);

        return products;
    }

    // GET http://localhost:8080/priceBetween?from=1&to=3
    @GetMapping("/priceBetween")
    public List<Product> priceBetween(
            @RequestParam(name = "from", defaultValue = "0.0") BigDecimal priceFrom,
            @RequestParam (name = "to", defaultValue = "0.0") BigDecimal priceTo
    )
    {
        return repository.getProductsWithPriceBetween(priceFrom, priceTo);
    }

    // GET http://localhost:8080/getByStatus?active=true
    // до 21:31
    @GetMapping("/getByStatus")
    public List<Product> getByStatus(
            @RequestParam (name = "active", defaultValue = "true") boolean status
    )
    {
        return repository.getProductWithStatus(status);
    }

    // GET http://localhost:8080/sort?column=price&direction=DESC
    @GetMapping("/sort")
    public List<Product> sort(
            @RequestParam (name = "column", defaultValue = "id") String column,
            @RequestParam (name = "direction", defaultValue = "ASC") String direction
    )
    {
        Sort.Direction dir = Sort.Direction.ASC; // по-возрастанию
        if(direction.equalsIgnoreCase("DESC"))
            dir = Sort.Direction.DESC; // по-убыванию

        return repository.getAll(Sort.by(dir, column));
    }

  @PostMapping("/save-Product")
    public List<Product> saveProduct(
          @RequestBody Product product
    )
    {

        return repository.saveProduct(product);
    }


}
