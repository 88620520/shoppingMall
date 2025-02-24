package com.example.shoppingmall.observer;

import com.example.shoppingmall.entity.Order;

public class ShopStaff implements Observer {
    @Override
    public void update(Order order) {
        System.out.println("店铺职员收到通知，订单ID：" + order.getId() + " 状态：" + order.getStatus());

    }
}


