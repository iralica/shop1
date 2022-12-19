package com.example.demo.controller;

import com.example.demo.Entity.Product;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductsController<ProductRepository> {
    private ProductsRepository repository;


    @Autowired
    public ProductsController(ProductsRepository repository) {
        this.repository = repository;
    }

    // РЅР°РїРёС€РёС‚Рµ РєРѕРґ, РєРѕС‚РѕСЂС‹Р№ РІРѕР·РІСЂР°С‚РёС‚ РІСЃРµ РїСЂРѕРґСѓРєС‚С‹ РїРѕ GET Р·Р°РїСЂРѕСЃСѓ
    // РЅР° url http://localhost:8080/all
    @GetMapping("/all")
    public List<Product> getAll()
    {
        List<Product> products = new ArrayList<>();

        repository.findAll().forEach(products::add);

        return products;
    }

    // GET http://localhost:8080/priceBetween?from=1&to=3
    @GetMapping("/priceBetween")
    public List<Product> priceBetween(
            @RequestParam (name = "from", defaultValue = "0.0") BigDecimal priceFrom,
            @RequestParam (name = "to", defaultValue = "0.0") BigDecimal priceTo
    )
    {
        return repository.getProductsWithPriceBetween(priceFrom, priceTo);
    }

    // GET http://localhost:8080/getByStatus?active=true
    // РґРѕ 21:31
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
            @RequestParam (name = "column", defaultValue = "id") String column, // РЅР°Р·Р°РЅРёРµ РєРѕР»РѕРЅРєРё РІ С‚Р°Р±Р»РёС†Рµ
            @RequestParam (name = "direction", defaultValue = "ASC") String direction // РІРѕР·СЂР°СЃС‚Р°РЅРёРµ РёР»Рё СѓР±С‹РІР°РЅРёРµ
    )
    {
        Sort.Direction dir = Sort.Direction.ASC; // РїРѕ-РІРѕР·СЂР°СЃС‚Р°РЅРёСЋ
        if(direction.equalsIgnoreCase("DESC"))
            dir = Sort.Direction.DESC; // РїРѕ-СѓР±С‹РІР°РЅРёСЋ

        return repository.getAll(Sort.by(dir, column));
    }


    // GET http://localhost:8080/page?page=0&size=5
    // page - РЅРѕРјРµСЂ СЃС‚СЂР°РЅРёС†С‹
    // size - СЃРєРѕР»СЊРєРѕ СЌР»РµРјРµРЅС‚РѕРІ РЅР° СЃС‚СЂР°РЅРёС†Рµ
    @GetMapping("/page")
    public List<Product> getPage(
            @RequestParam (name = "size", defaultValue = "5") int pageSize,
            @RequestParam (name = "page", defaultValue = "0") int pageNumber
    )
    {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        return repository
                .getPage(pageable)
                .get()
                .collect(Collectors.toList());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct
            (
                    @RequestBody Product productRequest
            )
    {
        repository.save(productRequest);
        return new ResponseEntity<>(productRequest, HttpStatus.CREATED);
    }


    @DeleteMapping("/products/{productId}")
    public ResponseEntity<HttpStatus> deleteProductById(
            @PathVariable (name = "productId") Long productId
    )
    {
        if(!repository.existsById(productId))
            throw new IllegalArgumentException("No Product with id " + productId);
        repository.deleteById(productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
