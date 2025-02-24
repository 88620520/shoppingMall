package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.dto.CheckoutDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto createOrderFromCart(String username, CheckoutDto checkoutDto);
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByUserId(String username);
    List<OrderDto> getAllOrders();
    void updateOrderStatus(Long orderId, String status);
    long getTodayOrderCount();
} 