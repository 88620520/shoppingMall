package com.example.shoppingmall.factory;

import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.entity.Customer;
import com.example.shoppingmall.entity.Staff;
import com.example.shoppingmall.entity.Manager;
import com.example.shoppingmall.entity.Boss;

public class UserFactory {
    public static User createUser(String role) {
        switch (role) {
            case "CUSTOMER":
                return new Customer();
            case "STAFF":
                return new Staff();
            case "MANAGER":
                return new Manager();
            case "BOSS":
                return new Boss();
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}

 