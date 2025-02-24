package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.StaffDto;
import com.example.shoppingmall.dto.UserDto;
import java.util.List;

public interface UserService {
    void registerUser(UserDto userDto);
    List<UserDto> getAllStaff();
    void addStaff(StaffDto staffDto);
    void updateStaff(Long id, StaffDto staffDto);
    void deleteStaff(Long id);
    UserDto createUser(UserDto userDto);
} 