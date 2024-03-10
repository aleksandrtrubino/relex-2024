package ru.trubino.farm.product;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.trubino.farm.unit.Unit;

@Setter
@Getter
@Entity(name = "products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @ManyToOne
    @JoinColumn(name="unit_id", nullable=false)
    private Unit unit;

    @Builder
    public Product(String name, Unit unit){
        this.name = name;
        this.unit = unit;
    }

}
