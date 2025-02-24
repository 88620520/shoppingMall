package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(String userId);
    Cart findByUserIdAndProductId(String userId, Long productId);
    void deleteByUserId(String userId);
} 