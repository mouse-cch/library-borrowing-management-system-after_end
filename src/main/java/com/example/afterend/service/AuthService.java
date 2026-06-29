package com.example.afterend.service;

import com.example.afterend.dto.LoginDTO;
import com.example.afterend.dto.LoginVO;
import com.example.afterend.dto.RegisterDTO;

public interface AuthService {
    LoginVO login(LoginDTO dto);
    Long register(RegisterDTO dto);
}
