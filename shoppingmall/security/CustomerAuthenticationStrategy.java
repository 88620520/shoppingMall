package com.example.shoppingmall.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthenticationStrategy implements AuthenticationStrategy {

    @Autowired
    private UserDetailsService userDetailsService;  // 自定义的 UserDetailsService

    @Autowired
    private PasswordEncoder passwordEncoder;  // 注入 PasswordEncoder

    @Override
    public Authentication authenticate(String username, String password) throws AuthenticationException {
        // 假设顾客的认证逻辑
        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 如果解密后的密码匹配
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }

        // 密码不匹配时抛出异常
        throw new BadCredentialsException("Invalid username or password");
    }
}
