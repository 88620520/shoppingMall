package com.example.shoppingmall.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        
        String redirectURL = "/";  // 默认跳转到首页
        
        // 根据角色决定跳转路径
        if (roles.contains("ROLE_BOSS") || roles.contains("ROLE_STAFF") || roles.contains("ROLE_MANAGER")) {
            redirectURL = "/admin/dashboard";  // 管理员跳转到后台
        }
        
        response.sendRedirect(redirectURL);
    }
} 