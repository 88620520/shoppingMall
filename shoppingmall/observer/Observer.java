package com.example.shoppingmall.observer;

import com.example.shoppingmall.entity.Order;

public interface Observer {
    void update(Order order);
}
