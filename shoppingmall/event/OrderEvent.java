package com.example.shoppingmall.event;

import com.example.shoppingmall.entity.Order;
import lombok.Getter;

@Getter
public class OrderEvent {
    private final Order order;
    private final String eventType;

    public OrderEvent(Order order, String eventType) {
        this.order = order;
        this.eventType = eventType;
    }

    public static OrderEvent orderCreated(Order order) {
        return new OrderEvent(order, "CREATED");
    }

    public static OrderEvent orderCompleted(Order order) {
        return new OrderEvent(order, "COMPLETED");
    }
} 