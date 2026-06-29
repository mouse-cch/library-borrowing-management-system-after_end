package com.example.afterend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_password_reset")
public class PasswordReset {
    @TableId(type = IdType.AUTO)
    private Long applyId;
    private Long userId;
    private String reason;
    private String status;
    private LocalDateTime applyTime;
    private LocalDateTime handleTime;
}
