package com.example.userloginsystem.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    public static String generateToken(String secret, long expireSeconds, Long userId) {
        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var now = new Date();
        var exp = new Date(now.getTime() + expireSeconds * 1000);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    /***
     * 解析token并返回userId
     * @param secret
     * @param token
     * @return
     */
    public static Long parseUserId(String secret, String token) {

        // 将secret以UTF-8的格式转换为字节数组，并将字节数组转换为secretKey
        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}