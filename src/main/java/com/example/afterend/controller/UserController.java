package com.example.afterend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.common.PageResult;
import com.example.afterend.common.R;
import com.example.afterend.dto.*;
import com.example.afterend.entity.PasswordReset;
import com.example.afterend.entity.User;
import com.example.afterend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary ="获取个人信息")
    @GetMapping("/profile")
    public R<User> profile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(userService.getProfile(userId));
    }

    @Operation(summary ="修改个人信息")
    @PutMapping("/profile")
    public R<?> updateProfile(@RequestBody UserProfileDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateProfile(userId, dto);
        return R.ok("修改成功", null);
    }

    @Operation(summary ="修改密码")
    @PutMapping("/password")
    public R<?> updatePassword(@RequestBody PasswordDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updatePassword(userId, dto);
        return R.ok("密码修改成功", null);
    }

    @Operation(summary ="分页查询用户列表")
    @GetMapping("/page")
    public R<PageResult<User>> pageUsers(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String role,
                                          @RequestParam(required = false) String status) {
        Page<User> page = userService.pageUsers(pageNum, pageSize, keyword, role, status);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }

    @Operation(summary ="修改用户状态")
    @PutMapping("/{userId}/status")
    public R<?> updateStatus(@PathVariable Long userId, @RequestBody UserStatusDTO dto) {
        userService.updateStatus(userId, dto);
        return R.ok("状态修改成功", null);
    }

    @Operation(summary ="重置用户密码")
    @PostMapping("/{userId}/reset-password")
    public R<?> resetPassword(@PathVariable Long userId) {
        userService.resetPassword(userId);
        return R.ok("密码已重置为默认密码", null);
    }

    @Operation(summary ="申请密码重置")
    @PostMapping("/password-reset-apply")
    public R<?> applyPasswordReset(@RequestBody PasswordResetApplyDTO dto) {
        userService.applyPasswordReset(dto);
        return R.ok("申请已提交", null);
    }

    @Operation(summary ="密码重置申请列表")
    @GetMapping("/password-reset-list")
    public R<PageResult<PasswordReset>> resetList(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                                                    @RequestParam(required = false) String status) {
        Page<PasswordReset> page = userService.resetList(pageNum, pageSize, status);
        return R.ok(new PageResult<>(page.getTotal(), page.getRecords(), pageNum, pageSize, (int) page.getPages()));
    }
}
