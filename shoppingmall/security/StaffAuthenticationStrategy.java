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
public class StaffAuthenticationStrategy implements AuthenticationStrategy {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;  // 注入 PasswordEncoder

    @Override
    public Authentication authenticate(String username, String password) throws AuthenticationException {
        // 获取用户的详细信息
        UserDetails user = userDetailsService.loadUserByUsername(username);

        // 使用 PasswordEncoder 的 matches 方法来比对加密后的密码
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }

        // 如果用户名或密码错误，抛出认证异常
        throw new BadCredentialsException("Invalid username or password");
    }
}
