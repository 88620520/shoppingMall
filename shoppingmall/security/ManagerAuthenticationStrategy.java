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
public class ManagerAuthenticationStrategy implements AuthenticationStrategy {

    @Autowired
    private UserDetailsService userDetailsService;  // 自定义的 UserDetailsService

    @Autowired
    private PasswordEncoder passwordEncoder;  // 注入 PasswordEncoder

    @Override
    public Authentication authenticate(String username, String password) throws AuthenticationException {
        // 加载库存经理的用户信息
        UserDetails user = userDetailsService.loadUserByUsername(username);

        // 使用 PasswordEncoder 进行密码匹配
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 用户名和密码匹配成功，返回认证的Token
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }

        // 如果用户名或密码不匹配，抛出认证异常
        throw new BadCredentialsException("Invalid username or password");
    }
}
