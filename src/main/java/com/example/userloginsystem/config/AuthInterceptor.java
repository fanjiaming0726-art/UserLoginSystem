package com.example.userloginsystem.config;

import com.example.userloginsystem.common.JwtUtil;
import com.example.userloginsystem.common.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 允许浏览器预检请求通过（小程序一般不需要，但加了更稳）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":4010,\"message\":\"未登录\",\"data\":null}");
            return false;
        }

        // 支持 "Bearer xxx" 或直接 "xxx"
        String token = auth.startsWith("Bearer ") ? auth.substring(7) : auth;

        try {
            Long userId = JwtUtil.parseUserId(jwtSecret, token);
            UserContext.setUserId(userId);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":4011,\"message\":\"登录已过期或token无效\",\"data\":null}");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}