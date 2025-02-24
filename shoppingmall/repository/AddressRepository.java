package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(String userId);
}