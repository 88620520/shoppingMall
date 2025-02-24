package com.example.shoppingmall.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String userId;
    private LocalDateTime orderDate;
    private String status;
    private String paymentMethod;
    private double total;
    private List<OrderItemDto> items;
    private AddressDto shippingAddress;
    private String recipientName;
    private String phoneNumber;
} 