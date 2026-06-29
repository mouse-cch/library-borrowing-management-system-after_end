package com.example.afterend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.*;
import com.example.afterend.entity.PasswordReset;
import com.example.afterend.entity.User;

public interface UserService {
    User getProfile(Long userId);
    void updateProfile(Long userId, UserProfileDTO dto);
    void updatePassword(Long userId, PasswordDTO dto);
    Page<User> pageUsers(Integer pageNum, Integer pageSize, String keyword, String role, String status);
    void updateStatus(Long userId, UserStatusDTO dto);
    void resetPassword(Long userId);
    void applyPasswordReset(PasswordResetApplyDTO dto);
    Page<PasswordReset> resetList(Integer pageNum, Integer pageSize, String status);
}
