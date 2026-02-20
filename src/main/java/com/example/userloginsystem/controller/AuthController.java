package com.example.userloginsystem.controller;

import com.example.userloginsystem.common.ApiResponse;
import com.example.userloginsystem.dto.LoginRequest;
import com.example.userloginsystem.dto.LoginResponse;
import com.example.userloginsystem.dto.RegisterRequest;
import com.example.userloginsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ApiResponse.ok(null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req);
        return ApiResponse.ok(new LoginResponse(token));
    }
}