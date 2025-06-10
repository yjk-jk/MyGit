package com.itheima.springboot.intercept;

import com.itheima.springboot.resp.ResultData;
import com.itheima.springboot.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去除 "Bearer " 前缀
        }

        if (token == null || token.isEmpty()) {
            sendErrorResponse(response, 401, "请先登录");
            return false;
        } else {
            try {
                String username = JwtUtil.extractUsername(token);
                String storedToken = redisTemplate.opsForValue().get(username);

                if (storedToken == null || !storedToken.equals(token)) {
                    sendErrorResponse(response, 401, "请先登录");
                    return false;
                } else { // 登录成功
                    return true;
                }
            } catch (IllegalArgumentException e) {
                sendErrorResponse(response, 401, "无效的JWT Token");
                return false;
            }
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write("{\"code\":" + statusCode + ",\"message\":\"" + message + "\"}");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可在此处理返回视图之前的逻辑
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 可在此处理请求完成后的逻辑
    }
}
