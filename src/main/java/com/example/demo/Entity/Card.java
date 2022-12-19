package com.example.demo.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, // чтобы не грузить строчки продуктов
            cascade = {
                    CascadeType.ALL, // удалять строчки в промежуточной таблице при удалении карты
                    CascadeType.MERGE
            },
            mappedBy = "cards" // название поля в таблице-связке
    )

            @JsonIgnore
    private Set<Product> products = new HashSet<>();


}
