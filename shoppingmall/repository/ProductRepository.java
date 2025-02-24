package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Product> findTop6ByOrderByIdDesc();
    long countByStockLessThan(int threshold);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
} 