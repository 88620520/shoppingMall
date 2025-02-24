package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.dto.StaffDto;
import com.example.shoppingmall.dto.UserDto;
import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.factory.RoleFactory;
import com.example.shoppingmall.repository.RoleRepository;
import com.example.shoppingmall.repository.UserRepository;
import com.example.shoppingmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleFactory roleFactory;

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        // 设置默认角色为 CUSTOMER
        user.setRole(roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("默认角色不存在")));
        
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAllStaff() {
        return userRepository.findByRoleNameIn(Arrays.asList("STAFF", "MANAGER", "BOSS"))
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addStaff(StaffDto staffDto) {
        if (userRepository.findByUsername(staffDto.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        Role role = roleRepository.findByName(staffDto.getRole())
            .orElseThrow(() -> new RuntimeException("角色不存在"));

        User user = new User();
        user.setUsername(staffDto.getUsername());
        user.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        user.setEmail(staffDto.getEmail());
        user.setRole(role);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateStaff(Long id, StaffDto staffDto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        Role role = roleRepository.findByName(staffDto.getRole())
            .orElseThrow(() -> new RuntimeException("角色不存在"));

        user.setEmail(staffDto.getEmail());
        user.setRole(role);
        if (staffDto.getPassword() != null && !staffDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteStaff(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!Arrays.asList("STAFF", "MANAGER").contains(user.getRole().getName())) {
            throw new RuntimeException("只能删除职员或经理账号");
        }

        userRepository.delete(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        
        // 使用工厂创建角色
        Role role = roleFactory.createRole(userDto.getRoleName());
        user.setRole(role);
        
        return convertToDto(userRepository.save(user));
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoleName(user.getRole().getName());  // 使用 roleName
        return dto;
    }
} 