package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.CartItemDto;
import java.util.List;

public interface CartService {
    void addToCart(CartItemDto cartItemDto);
    List<CartItemDto> getCartByUserId(String username);
    void updateQuantity(Long itemId, Integer quantity);
    void removeFromCart(Long itemId);
    double calculateTotal(String username);
    void clearCart(String username);
} 