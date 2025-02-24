package com.example.shoppingmall.factory.impl;

import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.factory.RoleFactory;
import org.springframework.stereotype.Component;

@Component
public class RoleFactoryImpl implements RoleFactory {
    @Override
    public Role createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        
        switch (roleName) {
            case "CUSTOMER":
                // 设置普通用户的默认权限
                break;
            case "STAFF":
                // 设置店员的默认权限
                break;
            case "MANAGER":
                // 设置经理的默认权限
                break;
            case "BOSS":
                // 设置老板的默认权限
                break;
            default:
                throw new IllegalArgumentException("未知角色类型: " + roleName);
        }
        
        return role;
    }
} 