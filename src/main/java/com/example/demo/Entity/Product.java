package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // РІ С‚Р°Р±Р»РёС†Рµ РєР»СЋС‡ Р±СѓРґРµС‚ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё СѓРІРµР»РёС‡РёРІР°С‚СЊСЃСЏ
    private Long id;

    @Column(name = "PRODUCT_NAME", length = 50, nullable = false, unique = false)
    private String name;

    @Column(name = "PRODUCT_PRICE", columnDefinition = "Decimal(10,2) default '0.00' ", nullable = false)
    private BigDecimal price;

    @Column(name = "PRODUCT_IS_ACTIVE", columnDefinition = "Boolean default 'true' ", nullable = false)
    private boolean isActive;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL, CascadeType.MERGE}
    )
    @JoinTable(
            name = "product_cards", // РЅР°Р·РІР°РЅРёРµ С‚Р°Р±Р»РёС†С‹ РІ Р±Р°Р·Рµ РґР°РЅРЅС‹С…
            joinColumns = {@JoinColumn(name = "product_id")}, // РЅР°Р·РІР°РЅРёРµ РєРѕР»РѕРЅРѕРє РІ С‚Р°Р±Р»РёС†Рµ
            inverseJoinColumns = {@JoinColumn(name = "card_id")} // РЅР°Р·РІР°РЅРёРµ РєРѕР»РѕРЅРѕРє РІ С‚Р°Р±Р»РёС†Рµ
    )
    private Set<Card> cards = new HashSet<>();


    public Product(String name, BigDecimal price, boolean isActive) {
        this.name = name;
        this.price = price;
        this.isActive = isActive;
    }

    public void addCard(Card card)
    {
        this.cards.add(card);
        card.getProducts().add(this);
    }

    public void removeCard(Long cardId)
    {
        Card card = cards.stream().filter(c -> c.getId().equals(cardId)).findFirst().orElse(null);
        if(card != null)
        {
            cards.remove(card);
            card.getProducts().remove(this);
        }
    }
}
