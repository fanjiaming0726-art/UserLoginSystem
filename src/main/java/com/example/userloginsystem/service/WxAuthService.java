package com.example.userloginsystem.service;

import com.example.userloginsystem.common.JwtUtil;
import com.example.userloginsystem.entity.User;
import com.example.userloginsystem.mapper.UserMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WxAuthService {

    private final UserMapper userMapper;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expire-seconds}")
    private long jwtExpireSeconds;

    public String login(String code, String avatarUrl, String wxNickname) {
        String url =
                "https://api.weixin.qq.com/sns/jscode2session" +
                        "?appid=" + appid +
                        "&secret=" + secret +
                        "&js_code=" + code +
                        "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();

        // 通过restTemplate发送请求，并接收返回的结果为String形式
        String raw = restTemplate.getForObject(url, String.class);
        System.out.println("wx jscode2session raw = " + raw);

        try {

            // 序列化工具
            ObjectMapper mapper = new ObjectMapper();

            // 将String形式的raw转化为Map
            Map<String, Object> result = mapper.readValue(raw, new TypeReference<Map<String, Object>>() {});
            System.out.println("wx jscode2session map = " + result);

            Object errcode = result.get("errcode");
            Object errmsg = result.get("errmsg");
            if (errcode != null) {
                throw new IllegalArgumentException("微信登录失败：" + errcode + " " + errmsg);
            }

            String openid = (String) result.get("openid");
            if (openid == null || openid.isBlank()) {
                throw new IllegalArgumentException("微信登录失败：openid为空");
            }

            User user = userMapper.findByOpenid(openid);

            if (user == null) {
                user = new User();
                user.setOpenid(openid);
                user.setNickname("微信用户");
                user.setAvatarUrl(avatarUrl);
                user.setWxNickname(wxNickname);

                userMapper.insert(user);
            } else {
                // ✅ 已存在则更新头像/微信昵称（你换头像后能生效）
                userMapper.updateWxProfileByOpenid(openid, avatarUrl, wxNickname);
            }

            // 重新查一次，确保拿到 id（有些情况下 insert 后 id 可能没回填）
            User latest = userMapper.findByOpenid(openid);

            return JwtUtil.generateToken(jwtSecret, jwtExpireSeconds, latest.getId());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("微信登录响应解析失败：" + e.getMessage());
        }
    }
}