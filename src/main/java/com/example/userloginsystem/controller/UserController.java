package com.example.userloginsystem.controller;

import com.example.userloginsystem.common.ApiResponse;
import com.example.userloginsystem.common.UserContext;
import com.example.userloginsystem.dto.MeResponse;
import com.example.userloginsystem.entity.User;
import com.example.userloginsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/me")
    public ApiResponse<MeResponse> me() {
        Long userId = UserContext.getUserId();
        User user = userService.getById(userId);

        return ApiResponse.ok(
                new MeResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getNickname(),
                        user.getAvatarUrl(),
                        user.getWxNickname()
                )
        );
    }
}