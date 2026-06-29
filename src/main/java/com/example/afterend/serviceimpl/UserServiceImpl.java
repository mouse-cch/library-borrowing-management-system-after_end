package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.afterend.dto.*;
import com.example.afterend.entity.PasswordReset;
import com.example.afterend.entity.User;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.PasswordResetMapper;
import com.example.afterend.mapper.UserMapper;
import com.example.afterend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordResetMapper passwordResetMapper;
    private static final String SALT = "library_salt_2026";
    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public User getProfile(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void updateProfile(Long userId, UserProfileDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (StringUtils.hasText(dto.getRealName())) user.setRealName(dto.getRealName());
        if (StringUtils.hasText(dto.getPhone())) user.setPhone(dto.getPhone());
        if (StringUtils.hasText(dto.getEmail())) user.setEmail(dto.getEmail());
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(Long userId, PasswordDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        String oldEncrypted = DigestUtils.md5DigestAsHex((dto.getOldPassword() + SALT).getBytes(StandardCharsets.UTF_8));
        if (!oldEncrypted.equals(user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        user.setPassword(DigestUtils.md5DigestAsHex((dto.getNewPassword() + SALT).getBytes(StandardCharsets.UTF_8)));
        userMapper.updateById(user);
    }

    @Override
    public Page<User> pageUsers(Integer pageNum, Integer pageSize, String keyword, String role, String status) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.and(w -> w.like(User::getRealName, keyword).or().like(User::getUsername, keyword));
        }
        if (StringUtils.hasText(role)) qw.eq(User::getRoleCode, role);
        if ("normal".equals(status)) qw.eq(User::getStatus, 1);
        else if ("frozen".equals(status)) qw.eq(User::getStatus, 0);
        qw.orderByDesc(User::getCreateTime);
        return userMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }

    @Override
    public void updateStatus(Long userId, UserStatusDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        user.setStatus(dto.getStatus());
        userMapper.updateById(user);
    }

    @Override
    public void resetPassword(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        user.setPassword(DigestUtils.md5DigestAsHex((DEFAULT_PASSWORD + SALT).getBytes(StandardCharsets.UTF_8)));
        userMapper.updateById(user);
    }

    @Override
    public void applyPasswordReset(PasswordResetApplyDTO dto) {
        PasswordReset pr = new PasswordReset();
        pr.setUserId(dto.getUserId());
        pr.setReason(dto.getReason());
        pr.setStatus("pending");
        pr.setApplyTime(LocalDateTime.now());
        passwordResetMapper.insert(pr);
    }

    @Override
    public Page<PasswordReset> resetList(Integer pageNum, Integer pageSize, String status) {
        LambdaQueryWrapper<PasswordReset> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status)) qw.eq(PasswordReset::getStatus, status);
        qw.orderByDesc(PasswordReset::getApplyTime);
        return passwordResetMapper.selectPage(new Page<>(pageNum, pageSize), qw);
    }
}
