package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.dto.CartItemDto;
import com.example.shoppingmall.dto.ProductDto;
import com.example.shoppingmall.entity.Cart;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.repository.CartRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public void addToCart(CartItemDto cartItemDto) {
        // 检查商品是否存在
        Product product = productRepository.findById(cartItemDto.getProductId())
            .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 检查库存
        if (product.getStock() < cartItemDto.getQuantity()) {
            throw new RuntimeException("商品库存不足");
        }

        // 检查购物车是否已有该商品
        Cart existingItem = cartRepository.findByUserIdAndProductId(
            cartItemDto.getUserId(), cartItemDto.getProductId());
        
        if (existingItem != null) {
            // 更新数量
            int newQuantity = existingItem.getQuantity() + cartItemDto.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("商品库存不足");
            }
            existingItem.setQuantity(newQuantity);
            cartRepository.save(existingItem);
        } else {
            // 创建新购物车项
            Cart cart = new Cart();
            cart.setUserId(cartItemDto.getUserId());
            cart.setProductId(cartItemDto.getProductId());
            cart.setQuantity(cartItemDto.getQuantity());
            cartRepository.save(cart);
        }
    }

    @Override
    public List<CartItemDto> getCartByUserId(String username) {
        return cartRepository.findByUserId(username).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateQuantity(Long itemId, Integer quantity) {
        Cart cart = cartRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("购物车项不存在"));
        
        Product product = productRepository.findById(cart.getProductId())
            .orElseThrow(() -> new RuntimeException("商品不存在"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }

        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long itemId) {
        cartRepository.deleteById(itemId);
    }

    @Override
    public double calculateTotal(String username) {
        return cartRepository.findByUserId(username).stream()
            .mapToDouble(item -> {
                Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在"));
                return product.getPrice() * item.getQuantity();
            })
            .sum();
    }

    @Override
    @Transactional
    public void clearCart(String username) {
        cartRepository.deleteByUserId(username);
    }

    private CartItemDto convertToDto(Cart cart) {
        Product product = productRepository.findById(cart.getProductId())
            .orElseThrow(() -> new RuntimeException("商品不存在"));
        
        CartItemDto dto = new CartItemDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setProductId(cart.getProductId());
        dto.setQuantity(cart.getQuantity());
        dto.setProduct(convertToProductDto(product));
        return dto;
    }

    private ProductDto convertToProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }
} 