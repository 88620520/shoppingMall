package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.role.name IN :roles")
    List<User> findByRoleNameIn(List<String> roles);
} 