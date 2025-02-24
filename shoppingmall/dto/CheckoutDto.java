package com.example.shoppingmall.dto;

import lombok.Data;

@Data
public class CheckoutDto {
    private Long addressId;
    private String paymentMethod;
} 