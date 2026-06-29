package com.example.afterend.controller;

import com.example.afterend.common.R;
import com.example.afterend.dto.LoginDTO;
import com.example.afterend.dto.LoginVO;
import com.example.afterend.dto.RegisterDTO;
import com.example.afterend.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证模块")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary ="登录")
    @PostMapping("/login")
    public R<LoginVO> login(@RequestBody LoginDTO dto) {
        return R.ok("登录成功", authService.login(dto));
    }

    @Operation(summary ="注册")
    @PostMapping("/register")
    public R<Long> register(@RequestBody RegisterDTO dto) {
        return R.ok("注册成功", authService.register(dto));
    }

    @Operation(summary ="退出登录")
    @PostMapping("/logout")
    public R<?> logout() {
        return R.ok("已退出登录", null);
    }
}
