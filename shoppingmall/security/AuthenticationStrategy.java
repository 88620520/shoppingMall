package com.example.shoppingmall.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationStrategy {

    Authentication authenticate(String username, String password) throws AuthenticationException;
}
