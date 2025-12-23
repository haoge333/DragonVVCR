package com.dragonvvcr.interceptor;

import com.dragonvvcr.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String path = request.getRequestURI();

        // 登录、注册等接口不需要验证token
        if (path.contains("/api/users/login") ||
            path.contains("/api/users/register") ||
            path.contains("/api/complaints") && request.getMethod().equals("GET")) {
            return true;
        }

        // 获取Authorization头
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Unauthorized - Token is required");

            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(result));
            return false;
        }

        // 验证token
        if (userService.getUserByToken(token) == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Unauthorized - Invalid token");

            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(result));
            return false;
        }

        return true;
    }
}