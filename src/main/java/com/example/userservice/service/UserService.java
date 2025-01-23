package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    Page<UserEntity> getUserByAll(Pageable pageable);
    UserDto getUserDetailsByEmail(String email);
    UserDto updateUser(UserDto userDto);
    void deleteUser(String userId);
}