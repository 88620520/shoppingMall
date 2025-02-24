package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ProductDto;
import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    List<ProductDto> getAllProducts(int page, int size);
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    void updateStock(Long productId, Integer stock);
    List<ProductDto> getFeaturedProducts();
    long getLowStockCount();
    
    // 搜索相关方法
    List<ProductDto> searchProducts(String keyword, int page, int size);  // 分页搜索
    List<ProductDto> searchByPrice(double minPrice, double maxPrice);     // 价格区间搜索
} 