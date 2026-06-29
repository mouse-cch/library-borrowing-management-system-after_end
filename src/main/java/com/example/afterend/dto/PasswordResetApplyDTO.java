package com.example.afterend.dto;

import lombok.Data;

@Data
public class PasswordResetApplyDTO {
    private Long userId;
    private String reason;
}
