package com.example.shoppingmall.dto;

import lombok.Data;

@Data
public class StaffDto {
    private String username;
    private String password;
    private String email;
    private String role;  // STAFF, MANAGER, BOSS

    // 构造函数
    public StaffDto() {}

    public StaffDto(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
} 