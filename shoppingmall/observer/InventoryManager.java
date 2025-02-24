package com.example.shoppingmall.observer;

import com.example.shoppingmall.entity.Order;

public class InventoryManager implements Observer {
    @Override
    public void update(Order order) {
        System.out.println("库存经理收到通知，订单ID：" + order.getId() + " 状态：" + order.getStatus());

    }
}

