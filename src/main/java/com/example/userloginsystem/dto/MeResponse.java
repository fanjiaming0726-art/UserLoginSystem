package com.example.userloginsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeResponse {
    private Long id;
    private String username;
    private String nickname;

    // ✅ 新增：头像与微信昵称
    private String avatarUrl;
    private String wxNickname;
}