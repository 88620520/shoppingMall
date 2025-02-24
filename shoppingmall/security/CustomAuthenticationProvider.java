package com.example.shoppingmall.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthenticationContext authenticationContext;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // 角色是从数据库中获取的
        String role = getRoleForUser(username); // 根据用户名获取角色

        return authenticationContext.authenticate(role, username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private String getRoleForUser(String username) {
        // 根据用户名获取角色的逻辑，假设通过用户名来获取角色
         if (username.equals("staff")) {
            return "Staff";
        } else if (username.equals("admin")) {
            return "Boss";
        }else if (username.equals("manager")) {
            return "Manager";
        }
        return "Customer";  // 默认为 Customer 角色
    }
}

