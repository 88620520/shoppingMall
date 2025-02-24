package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Cart")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private Long productId;
    private Integer quantity;
} 