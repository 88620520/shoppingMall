package com.example.shoppingmall.observer;

import com.example.shoppingmall.event.OrderEvent;

public interface OrderObserver {
    void onOrderEvent(OrderEvent event);
} 