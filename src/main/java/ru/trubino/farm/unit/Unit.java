package ru.trubino.farm.unit;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.trubino.farm.product.Product;

import java.util.Set;

@Setter
@Getter
@Entity(name = "units")
@NoArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;

    @Builder
    public Unit(String name){
        this.name = name;
    }
}
