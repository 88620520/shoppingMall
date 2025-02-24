package com.example.shoppingmall.dto;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String recipientName;  // 收货人姓名
    private String phoneNumber;    // 电话号码
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country = "中国"; // 默认值为中国
}