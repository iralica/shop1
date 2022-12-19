package com.example.demo.controller;
import com.example.demo.Entity.Card;
import com.example.demo.Entity.Product;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {
    /*
    GET 	/cards 					retrieve all Cards
    GET 	/products/:id/card 		retrieve all Cards of a Product
    POST 	/products/:id/card 		create/add Card for a Product
    DELETE 	/products/:id/card/:id 	delete a Card from a Product by :id

    GET 	/card/:id/products 		retrieve all Products of a Card
    GET 	/products 				retrieve all Products
    GET 	/products/:id 			retrieve a Product with it Cards
    GET 	/cards/:id 				retrieve a Card by :id
    PUT 	/cards/:id 				update a Card by :id
    DELETE 	/cards/:id 				delete a Card by :id
    DELETE 	/products/:id 			delete a Product by :id
     */
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getAllCards()
    {
        List<Card> cards = cardRepository.findAll();
        if(cards.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(cards, HttpStatus.OK);
    }


    // GET 	/products/:id/card 		retrieve all Cards of a Product
    // GET 	/products/:id/card 		retrieve all Cards of a Product
    @GetMapping("/products/{productId}/card")
    public ResponseEntity<List<Card>> getAllCardsForAProduct(
            @PathVariable(name = "productId") Long productId
    )
    {
        // проверить есть ли продукт и выбросить исключение если его нет
        if(!productsRepository.existsById(productId))
            throw new IllegalArgumentException("No product with productId " + productId);

        // вернуть все его карты
        List<Card> cards = cardRepository.findCardsByProductsId(productId);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    // POST 	/products/:id/card 		create/add Card for a Product
    // если карты нет, то она создастся и добавиться к продукту
    // если карта есть она добавиться к продукту
    @PostMapping("/products/{productId}/card")
    public ResponseEntity<Card> addCard(
            @PathVariable(name = "productId") Long productId,
            @RequestBody Card cardRequest
    )
    {
        // если карта уже есть в репозитории то добавить ее в продукт
        // если карты нет, то создаем из реквеста и добавляем в продукт
        Card card = productsRepository.findById(productId).map(
                product -> {
                    Long cardId = cardRequest.getId();
                    if(cardId != null)
                    {
                        Card _card = cardRepository.findById(cardId).orElseThrow(
                                () -> new IllegalArgumentException("No card with cardId " + cardId)
                        );
                        product.addCard(_card);
                        productsRepository.save(product);
                        return _card;
                    }
                    product.addCard(cardRequest);
                    return cardRepository.save(cardRequest);
                }
        ).orElseThrow(
                () -> new IllegalArgumentException("Product with productId " + productId + " not found"  )
        );

        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    // DELETE 	/products/:id/card/:id 	delete a Card from a Product by :id

    @DeleteMapping("/products/{productId}/card/{cardId}")
    public ResponseEntity<HttpStatus> deleteCardFromProduct(
            @PathVariable(name = "productId") Long productId,
            @PathVariable(name = "cardId") Long cardId
    )
    {
        // выбросить исключение если продукта нет
        Product product = productsRepository.findById(productId).orElseThrow(
                () -> new LayerInstantiationException("No product with productId " + productId)
        );
        // нужно убрать карту из продукта
        product.removeCard(cardId);
        // сохранить продукт
        productsRepository.save(product);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
