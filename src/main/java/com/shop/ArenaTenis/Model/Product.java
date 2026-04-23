package com.shop.ArenaTenis.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Double oldPrice;
    private String category;
    private String brand;
    private String imageUrl;
    private Integer stock;

    @Column(nullable = false)
    private Boolean available = true;
}