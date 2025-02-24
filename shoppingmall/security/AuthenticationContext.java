package com.example.shoppingmall.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AuthenticationContext {

    private final Map<String, AuthenticationStrategy> strategies;

    @Autowired
    public AuthenticationContext(List<AuthenticationStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(strategy -> strategy.getClass().getSimpleName(), Function.identity()));
    }

    public Authentication authenticate(String role, String username, String password) {
        AuthenticationStrategy strategy = strategies.get(role + "AuthenticationStrategy");
        if (strategy == null) {
            throw new IllegalArgumentException("No authentication strategy found for role: " + role);
        }
        return strategy.authenticate(username, password);
    }
}

