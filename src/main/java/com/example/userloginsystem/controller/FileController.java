package com.example.userloginsystem.controller;

import com.example.userloginsystem.common.ApiResponse;
import com.example.userloginsystem.service.CosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final CosService cosService;

    @PostMapping("/upload")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        // 简单校验：只允许图片
        String ct = file.getContentType();
        if (ct == null || (!ct.startsWith("image/"))) {
            return ApiResponse.fail(4000, "只允许上传图片");
        }

        File temp = File.createTempFile("avatar_", ".tmp");
        try {
            file.transferTo(temp);
            String url = cosService.uploadAvatar(temp, ct);
            return ApiResponse.ok(url);
        } finally {
            temp.delete();
        }
    }
}