package com.example.demo.repository;

import com.example.demo.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findCardsByProductsId(Long productId);

}
