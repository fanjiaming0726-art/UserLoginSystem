package com.example.userloginsystem.service;

import com.example.userloginsystem.common.JwtUtil;
import com.example.userloginsystem.dto.LoginRequest;
import com.example.userloginsystem.dto.RegisterRequest;
import com.example.userloginsystem.entity.User;
import com.example.userloginsystem.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expire-seconds}")
    private long jwtExpireSeconds;

    public void register(RegisterRequest req) {
        var existed = userMapper.findByUsername(req.getUsername());
        if (existed != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        user.setNickname(req.getNickname());

        userMapper.insert(user);
    }

    public String login(LoginRequest req) {
        var user = userMapper.findByUsername(req.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        return JwtUtil.generateToken(jwtSecret, jwtExpireSeconds, user.getId());
    }
}