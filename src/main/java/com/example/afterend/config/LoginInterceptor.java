package com.example.afterend.config;

import com.alibaba.fastjson2.JSON;
import com.example.afterend.common.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器 - Token校验
 * @author cch
 * @since 2026-06-29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(R.fail(401, "未登录或Token已过期")));
            return false;
        }
        try {
            token = token.substring(7);
            Long userId = jwtUtil.getUserId(token);
            String roleCode = jwtUtil.getRoleCode(token);
            request.setAttribute("userId", userId);
            request.setAttribute("roleCode", roleCode);
            return true;
        } catch (Exception e) {
            log.warn("Token解析失败: {}", e.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(R.fail(401, "Token无效或已过期")));
            return false;
        }
    }
}
