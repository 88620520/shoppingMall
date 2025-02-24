package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.dto.CartItemDto;
import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.dto.OrderItemDto;
import com.example.shoppingmall.dto.ProductDto;
import com.example.shoppingmall.dto.CheckoutDto;
import com.example.shoppingmall.dto.AddressDto;
import com.example.shoppingmall.entity.Order;
import com.example.shoppingmall.entity.OrderDetail;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.entity.Address;
import com.example.shoppingmall.repository.OrderRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.repository.AddressRepository;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUserId(orderDto.getUserId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setTotal(orderDto.getTotal());
        
        order = orderRepository.save(order);
        return convertToDto(order);
    }

    @Override
    @Transactional
    public OrderDto createOrderFromCart(String username, CheckoutDto checkoutDto) {
        // 获取购物车商品
        List<CartItemDto> cartItems = cartService.getCartByUserId(username);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        // 获取用户选择的地址
        Address address = addressRepository.findById(checkoutDto.getAddressId())
                .orElseThrow(() -> new RuntimeException("地址不存在"));

        // 创建订单
        Order order = new Order();
        order.setUserId(username);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setPaymentMethod(checkoutDto.getPaymentMethod());
        order.setTotal(calculateTotal(username));

        // 设置地址信息
        order.setAddressLine(address.getAddressLine());
        order.setCity(address.getCity());
        order.setState(address.getState());
        order.setPostalCode(address.getPostalCode());
        order.setCountry(address.getCountry());
        order.setRecipientName(address.getRecipientName());
        order.setPhoneNumber(address.getPhoneNumber());

        // 保存订单
        order = orderRepository.save(order);

        // 创建订单详情
        for (CartItemDto cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

            // 检查库存
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品 " + product.getName() + " 库存不足");
            }

            // 创建订单详情
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(cartItem.getQuantity());
            detail.setPrice(product.getPrice());
            order.getItems().add(detail);

            // 更新库存
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // 保存订单和订单详情
        order = orderRepository.save(order);

        // 返回订单DTO
        return convertToDto(order);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("订单不存在"));
        return convertToDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String username) {
        return orderRepository.findByUserId(username).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public long getTodayOrderCount() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return orderRepository.countByOrderDateBetween(startOfDay, endOfDay);
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setTotal(order.getTotal());
        
        // 转换订单详情
        dto.setItems(order.getItems().stream()
            .map(this::convertToOrderItemDto)
            .collect(Collectors.toList()));
        
        // 转换收货地址
        AddressDto addressDto = new AddressDto();
        addressDto.setAddressLine(order.getAddressLine());
        addressDto.setCity(order.getCity());
        addressDto.setState(order.getState());
        addressDto.setPostalCode(order.getPostalCode());
        addressDto.setCountry(order.getCountry());
        dto.setShippingAddress(addressDto);
        
        // 设置收货人信息
        dto.setRecipientName(order.getRecipientName());
        dto.setPhoneNumber(order.getPhoneNumber());
        
        return dto;
    }

    private OrderItemDto convertToOrderItemDto(OrderDetail detail) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(detail.getId());
        dto.setProductId(detail.getProduct().getId());
        dto.setProductName(detail.getProduct().getName());
        dto.setProductDescription(detail.getProduct().getDescription());
        dto.setProductImageUrl(detail.getProduct().getImageUrl());
        dto.setPrice(detail.getPrice());
        dto.setQuantity(detail.getQuantity());
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

    private double calculateTotal(String username) {
        List<CartItemDto> cartItems = cartService.getCartByUserId(username);
        return cartItems.stream()
            .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
            .sum();
    }
} 