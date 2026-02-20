package com.example.userloginsystem.service;

import com.example.userloginsystem.entity.User;
import com.example.userloginsystem.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public User getById(Long id) {
        return userMapper.findById(id);
    }
}