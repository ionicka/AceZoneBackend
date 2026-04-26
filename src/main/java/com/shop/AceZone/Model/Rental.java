package com.shop.AceZone.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private RentalStatus status = RentalStatus.PENDING;

    public enum RentalStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
}