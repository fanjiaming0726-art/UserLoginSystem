package com.example.userloginsystem.controller;

import com.example.userloginsystem.common.ApiResponse;
import com.example.userloginsystem.dto.LoginRequest;
import com.example.userloginsystem.dto.LoginResponse;
import com.example.userloginsystem.dto.RegisterRequest;
import com.example.userloginsystem.service.AuthService;
import com.example.userloginsystem.service.WxAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final WxAuthService wxAuthService;

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ApiResponse.ok(null);
    }

    // @Valid注解代表启动校验，但是具体的校验规则在后面的具体类中，例如@NotBlank。所以@Valid和@NotBlank是相互配合的
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req);
        return ApiResponse.ok(new LoginResponse(token));
    }

    @PostMapping("/wx-login")
    public ApiResponse<LoginResponse> wxLogin(@RequestBody Map<String, String> body) {

        String code = body.get("code");
        String avatarUrl = body.get("avatarUrl");
        String wxNickname = body.get("wxNickname");

        String token = wxAuthService.login(code, avatarUrl, wxNickname);

        return ApiResponse.ok(new LoginResponse(token));
    }
}