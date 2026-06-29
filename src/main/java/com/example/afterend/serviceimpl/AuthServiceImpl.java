package com.example.afterend.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.afterend.config.JwtUtil;
import com.example.afterend.dto.LoginDTO;
import com.example.afterend.dto.LoginVO;
import com.example.afterend.dto.RegisterDTO;
import com.example.afterend.entity.User;
import com.example.afterend.exception.BusinessException;
import com.example.afterend.mapper.UserMapper;
import com.example.afterend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    private static final String SALT = "library_salt_2026";

    @Override
    public LoginVO login(LoginDTO dto) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(qw);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被冻结，请联系管理员");
        }
        String encrypted = DigestUtils.md5DigestAsHex((dto.getPassword() + SALT).getBytes(StandardCharsets.UTF_8));
        if (!encrypted.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRoleCode());
        Long expire = System.currentTimeMillis() + 86400000L;
        return new LoginVO(token, user.getUserId(), user.getRoleCode(), expire);
    }

    @Override
    public Long register(RegisterDTO dto) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername, dto.getUsername());
        if (userMapper.selectCount(qw) > 0) {
            throw new BusinessException("该账号已注册");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex((dto.getPassword() + SALT).getBytes(StandardCharsets.UTF_8)));
        user.setRealName(dto.getRealName());
        user.setRoleCode(dto.getRole());
        user.setStatus(1);
        user.setMaxBorrow("teacher".equals(dto.getRole()) ? 10 : 5);
        user.setBorrowDays("teacher".equals(dto.getRole()) ? 60 : 30);
        userMapper.insert(user);
        return user.getUserId();
    }
}
