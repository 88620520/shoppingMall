package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
    long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);
} 