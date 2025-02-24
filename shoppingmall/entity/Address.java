package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String recipientName;  // 收货人姓名
    private String phoneNumber;    // 电话号码
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country = "中国"; // 默认值为中国
}