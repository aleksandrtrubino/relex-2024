package ru.trubino.farm.production;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.trubino.farm.product.Product;
import ru.trubino.farm.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Setter
@Getter
@Entity(name = "production_entries")
@NoArgsConstructor
public class ProductionEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;
    @ManyToOne
    @JoinColumn(name="producer_id", nullable=false)
    private User producer;
    private Long quantity;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;


    public ProductionEntry(Product product, User producer, Long quantity) {
        this.product = product;
        this.producer = producer;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
    }

    @Builder
    public ProductionEntry(Product product, User producer, Long quantity, LocalDateTime timestamp) {
        this.product = product;
        this.producer = producer;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }
}
