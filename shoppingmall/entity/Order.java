package com.example.shoppingmall.entity;

import com.example.shoppingmall.observer.InventoryManager;
import com.example.shoppingmall.observer.Observer;
import com.example.shoppingmall.observer.ShopStaff;
import com.example.shoppingmall.observer.Subject;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`Order`")
@Data
public class Order implements Subject {  // 确保实现了 Subject 接口

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private LocalDateTime orderDate;
    private String status;
    private String paymentMethod;
    private double total;

    // 收货地址信息
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String recipientName;
    private String phoneNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> items = new ArrayList<>();

    // 使用 transient 关键字，表示该字段不会被持久化到数据库
    @Transient
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    // 下单时调用该方法
    public void placeOrder() {
        // 订单创建后，通知所有观察者
        ShopStaff shopStaff = new ShopStaff();
        registerObserver(shopStaff);
        InventoryManager inventoryManager=new InventoryManager();
        registerObserver(inventoryManager);
        notifyObservers();
    }
}




