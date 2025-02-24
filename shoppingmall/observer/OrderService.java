package com.example.shoppingmall.observer;


import com.example.shoppingmall.entity.Order;

public class OrderService {

    public void placeOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("创建");

        // 注册观察者
        ShopStaff shopStaff = new ShopStaff();
        InventoryManager inventoryManager = new InventoryManager();
        order.registerObserver(shopStaff);
        order.registerObserver(inventoryManager);

        // 订单状态改变时通知观察者
        order.setStatus("已付款");


    }

    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        orderService.placeOrder(12345L);
    }
}

