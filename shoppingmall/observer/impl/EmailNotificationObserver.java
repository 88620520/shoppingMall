package com.example.shoppingmall.observer.impl;

import com.example.shoppingmall.event.OrderEvent;
import com.example.shoppingmall.observer.OrderObserver;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements OrderObserver {
    @Override
    public void onOrderEvent(OrderEvent event) {
        switch (event.getEventType()) {
            case "CREATED":
                System.out.println("发送邮件: 订单创建成功，订单号：" + event.getOrder().getId());
                break;
            case "COMPLETED":
                System.out.println("发送邮件: 订单已完成，订单号：" + event.getOrder().getId());
                break;
        }
    }
} 